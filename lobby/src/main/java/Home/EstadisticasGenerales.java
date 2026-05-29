package Home;

import java.io.Serializable;

/**
 * Datos de estadisticas que un modulo le devuelve al Home al finalizar.
 * Es inmutable: solo tiene getters.
 * Implementa Serializable para poder guardarse en archivos.
 */
public class EstadisticasGenerales implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombreModulo;
    private final int puntajeTotal;
    private final int partidasJugadas;
    private final int partidasGanadas;
    private final int partidasPerdidas;
    private final int enemigosDestruidos;
    private final long tiempoJugadoSegundos;

    public EstadisticasGenerales(String nombreModulo, int puntajeTotal,
                                  int partidasJugadas, int partidasGanadas,
                                  int partidasPerdidas, int enemigosDestruidos,
                                  long tiempoJugadoSegundos) {
        this.nombreModulo = nombreModulo;
        this.puntajeTotal = puntajeTotal;
        this.partidasJugadas = partidasJugadas;
        this.partidasGanadas = partidasGanadas;
        this.partidasPerdidas = partidasPerdidas;
        this.enemigosDestruidos = enemigosDestruidos;
        this.tiempoJugadoSegundos = tiempoJugadoSegundos;
    }

    public String getNombreModulo() { return nombreModulo; }
    public int getPuntajeTotal() { return puntajeTotal; }
    public int getPartidasJugadas() { return partidasJugadas; }
    public int getPartidasGanadas() { return partidasGanadas; }
    public int getPartidasPerdidas() { return partidasPerdidas; }
    public int getEnemigosDestruidos() { return enemigosDestruidos; }
    public long getTiempoJugadoSegundos() { return tiempoJugadoSegundos; }
}
