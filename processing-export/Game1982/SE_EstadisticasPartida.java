
import processing.data.IntDict;

/**
 * Estadísticas de Partida.
 * Actúa como un DTO (Data Transfer Object) para almacenar los resultados 
 * de un intento/partida específico.
 */
public class SE_EstadisticasPartida {

    private int score;
    private int totalEnemigosDestruidos;
    private IntDict detalleEnemigos;
    private boolean victoria;
    private long tiempoJugadoSegundos;

    public SE_EstadisticasPartida() {
        this.score = 0;
        this.totalEnemigosDestruidos = 0;
        this.detalleEnemigos = new IntDict();
        this.victoria = false;
        this.tiempoJugadoSegundos = 0;
    }

    public void addScore(int puntos) {
        this.score += puntos;
    }

    public void registrarMuerte(SE_Enemigo e) {
        totalEnemigosDestruidos++;
        detalleEnemigos.increment(e.identificadorSprite);
    }

    // Getters / Setters
    public int getScore() {
        return score;
    }

    public int getTotalEnemigosDestruidos() {
        return totalEnemigosDestruidos;
    }

    public IntDict getDetalleEnemigos() {
        return detalleEnemigos;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    public long getTiempoJugadoSegundos() {
        return tiempoJugadoSegundos;
    }

    public void setTiempoJugadoSegundos(long segundos) {
        this.tiempoJugadoSegundos = segundos;
    }
}