import processing.core.PApplet;

/**
 * Gestor de Nivel Simplificado.
 * Encapsula la línea temporal (tiempoNivel) y la lógica de spawning de oleadas
 * de enemigos y el boss final.
 */
public class SE_GestorDeNivel {
    private int tiempoNivel = 0;
    private boolean bossSpawned = false;

    // Constantes de balanceo
    private final int SCORE_PARA_BOSS = 2000;
    private final int INTERVALO_SPAWN_BASE = 90;
    // Probability (0.0‑1.0) that a spawned enemy is aerial.
    private final float PROB_AEREO = 0.65f; // 60 % aerial, 40 % naval

    public SE_GestorDeNivel() {
    }

    public void resetear() {
        this.tiempoNivel = 0;
        this.bossSpawned = false;
    }

    public void actualizar(SE_GestorPrincipal sesion, SE_GestorDeEntidades entidades) {
        this.tiempoNivel++;
        PApplet app = sesion.getApp();

        // Intervalo fijo Arcade
        int intervaloActual = 60;

        // Spawneo de Enemigos si aún no llegamos al Boss
        if (!bossSpawned) {
            if (tiempoNivel % intervaloActual == 0) {
                // Probabilidad configurada para tipo de enemigo
                if (app.random(1.0f) < PROB_AEREO) {
                    // Enemigo aéreo
                    entidades.agregarEnemigo(new SE_EnemigoAereo(entidades, app.random(50, app.width - 50), -30));
                } else {
                    // Enemigo naval dentro del rango de agua
                    entidades.agregarEnemigo(
                            new SE_EnemigoNaval(entidades, app.random(app.width * 0.25f, app.width * 0.75f), -30));
                }
            }

            // ¿Llegamos al umbral para el Boss?
          SE_EstadisticasPartida stats = sesion.getHistorial().getPartidaActual();
            int scoreActual = (stats != null) ? stats.getScore() : 0;
            if (scoreActual >= SCORE_PARA_BOSS) {
                // Limpiamos la pantalla de enemigos menores para la épica pelea
                entidades.vaciarEnemigos();
                // Spawn the boss relative to the current viewport so it's visible
                float spawnY = app.height * 0.15f; // 15% from top of the screen
                entidades.agregarEnemigo(new SE_HmsSheffield(entidades, app.width / 2.0f, spawnY));
                bossSpawned = true;
                System.out.println("[GestorDeNivel] Boss spawneado al alcanzar " + SCORE_PARA_BOSS + " puntos.");
            }
        }
    }

    // Getters
    public int getTiempoNivel() {
        return tiempoNivel;
    }

    public boolean isBossSpawned() {
        return bossSpawned;
    }
}