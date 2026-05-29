package Aviones.etendard;

import Home.EstadisticasGenerales;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de Historial de Sesión.
 * Encapsula la acumulación de estadísticas en una lista histórica.
 * Actúa como la única fuente de verdad para el historial analítico del juego.
 */
public class SE_HistorialSesion {
    private final List<SE_EstadisticasPartida> registroPartidas = new ArrayList<>();
    private SE_EstadisticasPartida partidaActual;

    public SE_HistorialSesion() {
    }

    /**
     * Instancia un nuevo registro para la partida en curso.
     */
    public void iniciarNuevaPartida() {
        this.partidaActual = new SE_EstadisticasPartida();
    }

    public SE_EstadisticasPartida getPartidaActual() {
        return partidaActual;
    }

    /**
     * Consolida la partida activa configurando su resultado final y archivándola en la lista.
     */
    public void finalizarPartidaActual(boolean gano, long tiempoJugadoSegundos) {
        if (partidaActual != null) {
            partidaActual.setVictoria(gano);
            partidaActual.setTiempoJugadoSegundos(tiempoJugadoSegundos);
            registroPartidas.add(partidaActual);
        }
    }

    /**
     * Calcula dinámicamente y exporta el acumulado total para que el Lobby actualice sus estadísticas generales.
     */
    public EstadisticasGenerales exportarEstadisticas(String nombreModulo) {
        int puntajeTotal = 0;
        int ganadas = 0;
        int destruidos = 0;
        long tiempoTotal = 0;

        for (SE_EstadisticasPartida p : registroPartidas) {
            puntajeTotal += p.getScore();
            destruidos += p.getTotalEnemigosDestruidos();
            tiempoTotal += p.getTiempoJugadoSegundos();
            if (p.isVictoria()) {
                ganadas++;
            }
        }

        int jugadas = registroPartidas.size();
        int perdidas = jugadas - ganadas;

        return new EstadisticasGenerales(
            nombreModulo,
            puntajeTotal,
            jugadas,
            ganadas,
            perdidas,
            destruidos,
            tiempoTotal
        );
    }

    public List<SE_EstadisticasPartida> getRegistroPartidas() {
        return registroPartidas;
    }
}
