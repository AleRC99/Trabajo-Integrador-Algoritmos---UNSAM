import processing.core.PApplet;
import processing.core.PImage;

/**
 * Simple visual effect for an explosion. It is NOT a game entity (no collision,
 * no health). It lives for a short number of frames and then disappears.
 */
public class SE_Explosion {
    private final SE_GestorDeEntidades gestor;
    private final float x, y;
    private int timer = 15; // frames remaining
    private final float maxRadius = 15f;

    public SE_Explosion(SE_GestorDeEntidades gestor, float x, float y) {
        this.gestor = gestor;
        this.x = x;
        this.y = y;
    }

    public void actualizar() {
        timer--;
    }

    public void dibujar() {
        if (timer <= 0) return;

        PApplet app = gestor.gp.getApp();

        float progress = 1f - (float) timer / 15f; // 0 -> 1

        float radioExterior = 4 + progress * maxRadius;
        float radioInterior = radioExterior * 0.6f;

        int alpha = (int)(255 * (1f - progress));

        app.pushStyle();
        app.pushMatrix();

        app.translate(x, y);
        app.noStroke();

        // =========================
        // NUCLEO BLANCO
        // =========================
        app.fill(255, 255, 255, alpha);
        dibujarCirculoPixelado(app, 0, 0, radioInterior * 0.4f, 3);

        // =========================
        // AMARILLO
        // =========================
        app.fill(255, 230, 0, alpha);
        dibujarCirculoPixelado(app, 0, 0, radioInterior, 4);

        // =========================
        // NARANJA
        // =========================
        app.fill(255, 120, 0, alpha);
        dibujarCirculoPixelado(app, 0, 0, radioExterior, 4);

        // =========================
        // CHISPAS
        // =========================
        app.fill(255, 180, 50, alpha);

        for (int i = 0; i < 8; i++) {
            float ang = PApplet.TWO_PI * i / 8f;
            float dist = radioExterior * 1.2f;
            float px = PApplet.cos(ang) * dist;
            float py = PApplet.sin(ang) * dist;
            app.rect(px - 2, py - 2, 4, 4);
        }

        // =========================
        // HUMO AL FINAL
        // =========================
        if (progress > 0.5f) {
            int humoAlpha = (int)(120 * (1f - progress));
            app.fill(80, humoAlpha);

            for (int i = 0; i < 6; i++) {
                float ang = PApplet.TWO_PI * i / 6f;
                float dist = radioExterior * 0.7f;
                float px = PApplet.cos(ang) * dist;
                float py = PApplet.sin(ang) * dist;
                app.rect(px - 4, py - 4, 8, 8);
            }
        }

        app.popMatrix();
        app.popStyle();
    }

    /**
     * Dibuja un círculo aproximado con rectángulos de tamaño pixelSize,
     * para lograr el efecto pixelado/retro de la explosión.
     */
    private void dibujarCirculoPixelado(PApplet app, float cx, float cy, float radio, int pixelSize) {
        for (float ang = 0; ang < PApplet.TWO_PI; ang += 0.3f) {
            float px = cx + PApplet.cos(ang) * radio;
            float py = cy + PApplet.sin(ang) * radio;
            app.rect(px - pixelSize / 2f, py - pixelSize / 2f, pixelSize, pixelSize);
        }
        // Relleno interior
        for (float r = 0; r < radio; r += pixelSize) {
            for (float ang = 0; ang < PApplet.TWO_PI; ang += 0.4f) {
                float px = cx + PApplet.cos(ang) * r;
                float py = cy + PApplet.sin(ang) * r;
                app.rect(px - pixelSize / 2f, py - pixelSize / 2f, pixelSize, pixelSize);
            }
        }
    }

    public boolean estaViva() {
        return timer > 0;
    }
}
