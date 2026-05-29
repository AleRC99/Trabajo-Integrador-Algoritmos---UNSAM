package Aviones.AvionPrueba;

import Contrato.*;
import Home.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Modulo de prueba (stub/fake) para testear el Home.
 * NO es un avion real del juego. Solo sirve para demostrar
 * que el contrato ModuloJuego funciona correctamente.
 *
 * Genera estadisticas aleatorias al finalizar.
 */
public class ModuloPrueba implements ModuloJuego {

    private EstadoJuego estado;
    private ContextoJuego contexto;
    private List<IModuloObserver> observers;

    // Datos de la partida simulada
    private long tiempoInicio;

    public ModuloPrueba() {
        this.estado = new NoIniciadoState();
        this.observers = new ArrayList<IModuloObserver>();
    }

    // --- Identidad ---

    public String getNombreModulo() {
        return "modulo_prueba";
    }

    public String getDescripcion() {
        return "Modulo de prueba para testing del Home";
    }

    public String getNombreAvion() {
        return "AVION DE PRUEBA";
    }

    // --- Contexto ---

    public void inicializarContexto(ContextoJuego ctx) {
        this.contexto = ctx;
    }

    // --- Ciclo de vida ---

    public void iniciar() {
        estado = new EnEjecucionState();
        tiempoInicio = System.currentTimeMillis();
        notificar(new ModuloEvento(ModuloEvento.Tipo.INICIADO, getNombreModulo()));
    }

    public void pausar() {
        estado = new PausadoState();
        notificar(new ModuloEvento(ModuloEvento.Tipo.PAUSADO, getNombreModulo()));
    }

    public void reanudar() {
        estado = new EnEjecucionState();
        notificar(new ModuloEvento(ModuloEvento.Tipo.REANUDADO, getNombreModulo()));
    }

    public void finalizar() {
        estado = new FinalizadoState();
        notificar(new ModuloEvento(ModuloEvento.Tipo.FINALIZADO, getNombreModulo()));
    }

    // --- Estado y estadisticas ---

    public EstadoJuego getEstado() {
        return estado;
    }

    /**
     * Genera estadisticas simuladas al finalizar.
     */
    public EstadisticasGenerales getEstadisticasGenerales() {
        long tiempoJugado = (System.currentTimeMillis() - tiempoInicio) / 1000;
        int puntaje = (int) (Math.random() * 5000) + 1000;
        int enemigos = (int) (Math.random() * 20) + 5;
        boolean gano = Math.random() > 0.5;

        return new EstadisticasGenerales(
            getNombreModulo(),
            puntaje,
            1,                          // partidasJugadas
            gano ? 1 : 0,              // partidasGanadas
            gano ? 0 : 1,              // partidasPerdidas
            enemigos,
            tiempoJugado
        );
    }

    // --- Observer ---

    public void agregarObserver(IModuloObserver obs) {
        observers.add(obs);
    }

    public void removerObserver(IModuloObserver obs) {
        observers.remove(obs);
    }

    private void notificar(ModuloEvento evento) {
        for (IModuloObserver obs : observers) {
            obs.onEventoModulo(evento);
        }
    }
}
