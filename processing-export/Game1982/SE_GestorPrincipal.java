
import processing.core.PApplet;
import processing.data.IntDict;
import java.util.ArrayList;
import java.util.List;

/**
 * GestorPrincipal (Core Engine)
 * Orquesta la lógica interna del juego 1942 (Super Etendard).
 * Ya no implementa ModuloJuego, se comunica con el Lobby a través de la fachada SE_ModuloEtendard.
 */
public class SE_GestorPrincipal {

    // Constantes de estado del juego 1942 (usadas en GestorGrafico)
    public static final int ESTADO_GAMEOVER = 2;
    public static final int ESTADO_WIN      = 3;

    // Sub-sistemas del juego
    protected SE_GestorDeEntidades entidades;
    protected SE_GestorDeNivel     nivel;
    protected SE_GestorGrafico     graficos;
    
    // Estadísticas e Input
    private SE_HistorialSesion historial;
    private SE_InputManager inputManager;

    // Fachada para comunicación hacia arriba
    private SE_ModuloEtendard fachada;

    public EstadisticasGenerales statsGenerales;
    // Instancia de PApplet para Processing
    private final PApplet app;
    public boolean victoriaRegistrada = false;
    
    // Estado interno para la lógica de ejecución (ahora lo pasa la fachada)
    private EstadoJuego estadoCicloVida;

    public SE_GestorPrincipal(PApplet app, SE_ModuloEtendard fachada) {
        this.app = app;
        this.fachada = fachada;
        this.estadoCicloVida = new NoIniciadoState();

        // Inicializar componentes desacoplados
        this.historial = new SE_HistorialSesion();
        this.inputManager = new SE_InputManager();

        // Inicializar sub-sistemas
        this.entidades = new SE_GestorDeEntidades(this);
        this.nivel = new SE_GestorDeNivel();
        this.graficos = new SE_GestorGrafico(this);

        // Carga de imágenes
        this.graficos.cargarRecursos();

        // Registrar eventos de teclado
        this.app.registerMethod("keyEvent", this);
    }

    public EstadisticasGenerales getEstadisticasGenerales(String nombreModulo) {
        if (historial == null) {
            return new EstadisticasGenerales(nombreModulo, 0, 0, 0, 0, 0, 0);
        }
        return historial.exportarEstadisticas(nombreModulo);
    }
    
    public boolean isVictoriaRegistrada() {
        return victoriaRegistrada;
    }

    public void setEstadoCicloVida(EstadoJuego estado) {
        this.estadoCicloVida = estado;
    }

    public EstadoJuego getEstado() {
        return estadoCicloVida;
    }

    // --- Notificación a la Fachada ---
    public void notificar(ModuloEvento.Tipo tipo, String mensaje) {
        if (fachada != null) {
            fachada.notificarDesdeCore(tipo, mensaje);
        }
    }

    // ── Métodos para la ejecución interceptada desde el Home ────────────────────────

    // Método invocado automáticamente por Processing para delegar los eventos de teclado
    public void keyEvent(processing.event.KeyEvent event) {
        if (estadoCicloVida instanceof NoIniciadoState || estadoCicloVida instanceof FinalizadoState) {
            return; // Ignorar teclas si no somos el módulo activo
        }

        char k = event.getKey();
        int kCode = event.getKeyCode();
        
        if (event.getAction() == processing.event.KeyEvent.PRESS) {
            procesarKeyPressed(k, kCode);
        } else if (event.getAction() == processing.event.KeyEvent.RELEASE) {
            procesarKeyReleased(k, kCode);
        }
    }

    public void procesarKeyPressed(char k, int kCode) {
        inputManager.gestionarKeyPressed(k, kCode, estadoCicloVida, this);
    }

    public void procesarKeyReleased(char k, int kCode) {
        inputManager.gestionarKeyReleased(k, kCode, estadoCicloVida, this);
    }

    // ── Lógica interna del juego ────────────────────────────

    public void initGameVariables() {
        entidades.vaciarTodo();
        historial.iniciarNuevaPartida();
        nivel.resetear();
        victoriaRegistrada = false;
        entidades.agregarNave(new SE_Nave(entidades, app.width / 2.0f, 500));
    }

    public void initGame() {
        initGameVariables();
        estadoCicloVida = new EnEjecucionState();
        if (fachada != null) {
            fachada.comenzarEjecucion();
        }
    }

    public void registrarVictoria() {
        if (!victoriaRegistrada) {
            consolidarPartida(true);
            victoriaRegistrada = true;
        }
    }

    public void consolidarPartida(boolean gano) {
        historial.finalizarPartidaActual(gano, nivel.getTiempoNivel() / 60);

        // Pasamos un string porque ya no tenemos acceso a getNombreModulo
        this.statsGenerales = getEstadisticasGenerales("super_etendard");
    }

    // --- Getters / Setters Auxiliares ---
    
    public PApplet getApp() {
        return app;
    }

    public SE_GestorGrafico getGraficos() {
        return graficos;
    }

    public SE_GestorDeNivel getNivel() {
        return nivel;
    }

    public SE_HistorialSesion getHistorial() {
        return historial;
    }

    // Las estadísticas ahora se acceden directamente a través del historial de sesión
}