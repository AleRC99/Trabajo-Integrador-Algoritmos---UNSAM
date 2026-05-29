package Aviones.etendard;

import processing.core.PApplet;
import processing.data.IntDict;
import Contrato.ModuloJuego;
import Contrato.ContextoJuego;
import Contrato.EstadoJuego;
import Home.EstadisticasGenerales;
import Home.IModuloObserver;
import Home.ModuloEvento;
import java.util.ArrayList;
import java.util.List;

/**
 * ADAPTADOR: GestorPrincipal implements ModuloJuego.
 * Conecta el juego 1942 (Super Etendard) con el sistema de lobby multi-juego.
 * Es el "Adapter" entre el contrato del lobby y la lógica interna del juego.
 */
public class SE_GestorPrincipal implements ModuloJuego {

    // Constantes de estado del juego 1942 (usadas en GestorGrafico)
    public static final int ESTADO_GAMEOVER = 2;
    public static final int ESTADO_WIN      = 3;

    // Constantes de fase del nivel 1942
    public static final int FASE_INICIO = 1;
    public static final int FASE_MEDIO  = 2;
    public static final int FASE_FINAL  = 3;

    // Configuración de la sesión (setters con validación)
    protected int selNivel         = 2;
    
    public void setSelNivel(int n) { 
        if (app != null) {
            selNivel = (int) app.constrain(n, 1, 3);
        } else {
            selNivel = Math.max(1, Math.min(3, n));
        }
    }

    // Sub-sistemas del juego
    protected SE_GestorDeEntidades entidades;
    protected SE_GestorDeNivel     nivel;
    protected SE_GestorGrafico     graficos;
    
    // Campo de estadísticas expuesto para compatibilidad con el motor de renderizado
    public EstadisticasGenerales statsGenerales;

    // Nuevas clases desacopladas (SRP)
    private SE_HistorialSesion historial;
    private SE_InputManager inputManager;

    // Contexto recibido desde el lobby
    private ContextoJuego contexto;

    // Patrón Observer: lista de observadores del lobby
    private final List<IModuloObserver> observers;

    // Estado del ciclo de vida según el contrato del lobby
    private Contrato.EstadoJuego estadoCicloVida;

    // Instancia de PApplet para Processing
    private final PApplet app;

    public SE_GestorPrincipal(PApplet app) {
        this.app = app;
        this.observers = new ArrayList<>();
        this.estadoCicloVida = new Contrato.NoIniciadoState();

        // Inicializar componentes desacoplados
        this.historial = new SE_HistorialSesion();
        this.inputManager = new SE_InputManager();

        // Inicializar sub-sistemas
        this.entidades = new SE_GestorDeEntidades(this);
        this.nivel = new SE_GestorDeNivel();
        this.graficos = new SE_GestorGrafico(this);

        // Inicializar objeto de estadísticas expuesto
        this.statsGenerales = getEstadisticasGenerales();

        // Carga de imágenes específicas del nivel y del lobby delegada al gestor gráfico
        this.graficos.cargarRecursos();
    }

    // ── Contrato ModuloJuego ────────────────────────────────

    @Override 
    public String getNombreModulo() { 
        return "super_etendard"; 
    }

    @Override 
    public String getDescripcion() { 
        return "Shooter aereo de la Guerra de Malvinas. Destruye la flota britanica."; 
    }

    @Override 
    public String getNombreAvion() { 
        return "Super Etendard"; 
    }

    @Override
    public void inicializarContexto(ContextoJuego ctx) {
        this.contexto = ctx;
        System.out.println("[SE_GestorPrincipal] Contexto recibido: jugador=" + ctx.getNombreJugador());
    }

    @Override
    public void iniciar() {
        this.estadoCicloVida = new Contrato.NoIniciadoState();
        this.historial = new SE_HistorialSesion();
        this.statsGenerales = getEstadisticasGenerales();
        notificar(ModuloEvento.Tipo.INICIADO, "Módulo iniciado");
    }

    @Override
    public void pausar() {
        this.estadoCicloVida = new Contrato.PausadoState();
        notificar(ModuloEvento.Tipo.PAUSADO, "Juego pausado");
    }

    @Override
    public void reanudar() {
        this.estadoCicloVida = new Contrato.EnEjecucionState();
        notificar(ModuloEvento.Tipo.REANUDADO, "Juego reanudado");
    }

    @Override
    public void finalizar() {
        this.estadoCicloVida = new Contrato.FinalizadoState();
        System.out.println("[SE_GestorPrincipal] Módulo finalizado.");
    }

    @Override
    public EstadoJuego getEstado() {
        return estadoCicloVida;
    }

    @Override
    public EstadisticasGenerales getEstadisticasGenerales() {
        if (historial == null) {
            return new EstadisticasGenerales(getNombreModulo(), 0, 0, 0, 0, 0, 0);
        }
        return historial.exportarEstadisticas(getNombreModulo());
    }

    @Override
    public void agregarObserver(IModuloObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removerObserver(IModuloObserver obs) {
        observers.remove(obs);
    }

    // ── Métodos para la ejecución interceptada desde el Home ────────────────────────

    public void actualizarYDibujar() {
        graficos.renderizarSegunEstado(estadoCicloVida);
    }

    public void procesarKeyPressed(char k, int kCode) {
        inputManager.gestionarKeyPressed(k, kCode, estadoCicloVida, this);
    }

    public void procesarKeyReleased(char k, int kCode) {
        inputManager.gestionarKeyReleased(k, kCode, estadoCicloVida, this);
    }

    // ── Lógica interna del juego ────────────────────────────

    public void initGame() {
        entidades.vaciarTodo();
        historial.iniciarNuevaPartida();
        nivel.resetear();

        estadoCicloVida = new Contrato.EnEjecucionState();

        entidades.agregarNave(new SE_Nave(entidades, app.width / 2.0f, 500));
    }

    public void ganarPartida() {
        if (estadoCicloVida instanceof Contrato.FinalizadoState) return; // Guard
        
        consolidarPartida(true);
        estadoCicloVida = new Contrato.FinalizadoState();
    }

    public void perderPartida() {
        if (estadoCicloVida instanceof Contrato.FinalizadoState) return; // Guard
        
        consolidarPartida(false);
        estadoCicloVida = new Contrato.FinalizadoState();
    }

    private void consolidarPartida(boolean gano) {
        historial.finalizarPartidaActual(gano, nivel.getTiempoNivel() / 60);

        // Mantener estadísticas actualizadas
        this.statsGenerales = getEstadisticasGenerales();
    }

    public void volverAlMenu() {
        estadoCicloVida = new Contrato.NoIniciadoState();
    }

    public void volverAlLobby() {
        // Notificamos al lobby que el módulo ha finalizado
        notificar(ModuloEvento.Tipo.FINALIZADO, "Volviendo al lobby principal");
    }

    // --- Getters / Setters Auxiliares ---
    
    public PApplet getApp() {
        return app;
    }

    public SE_GestorGrafico getGraficos() {
        return graficos;
    }

    public int getScore() {
        if (historial.getPartidaActual() == null) return 0;
        return historial.getPartidaActual().getScore();
    }

    public SE_GestorDeNivel getNivel() {
        return nivel;
    }

    public SE_HistorialSesion getHistorial() {
        return historial;
    }

    public int getDificultad() {
        return selNivel;
    }

    public int getTotalEnemigosDestruidos() {
        if (historial.getPartidaActual() == null) return 0;
        return historial.getPartidaActual().getTotalEnemigosDestruidos();
    }

    public IntDict getDetalleEnemigos() {
        if (historial.getPartidaActual() == null) return new IntDict();
        return historial.getPartidaActual().getDetalleEnemigos();
    }

    public void addScore(int puntos) {
        if (historial.getPartidaActual() != null) {
            historial.getPartidaActual().addScore(puntos);
        }
    }

    public void registrarMuerte(SE_Enemigo e) {
        if (historial.getPartidaActual() != null) {
            historial.getPartidaActual().registrarMuerte(e);
        }
    }

    // Patrón Observer: notificación al lobby
    private void notificar(ModuloEvento.Tipo tipo, String mensaje) {
        ModuloEvento ev = new ModuloEvento(tipo, getNombreModulo(), mensaje);
        for (IModuloObserver obs : new ArrayList<>(observers)) {
            obs.onEventoModulo(ev);
        }
    }
}
