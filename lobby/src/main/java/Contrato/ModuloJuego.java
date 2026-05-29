package Contrato;

import Home.EstadisticasGenerales;
import Home.IModuloObserver;

/**
 * Interfaz principal del contrato de integracion.
 * Todo modulo de avion debe implementar esta interfaz
 * para poder conectarse al Home/Lobby del juego 1982.
 */
public interface ModuloJuego {

    // --- Identidad ---
    String getNombreModulo();
    String getDescripcion();
    String getNombreAvion();

    // --- Contexto ---
    void inicializarContexto(ContextoJuego ctx);

    // --- Ciclo de vida ---
    void iniciar();
    void pausar();
    void reanudar();
    void finalizar();

    // --- Estado y estadisticas ---
    EstadoJuego getEstado();
    EstadisticasGenerales getEstadisticasGenerales();

    // --- Observer ---
    void agregarObserver(IModuloObserver obs);
    void removerObserver(IModuloObserver obs);
}
