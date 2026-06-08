import processing.core.PApplet;
import processing.core.PImage;
import java.util.Map;
import java.util.HashMap;

public abstract class SE_Enemigo extends SE_Objeto {

    // Mapping sprite identifiers to initial life points
    private static final Map<String, Integer> VIDA_POR_SPRITE = new HashMap<>();
    static {
        // Define life per sprite type
        VIDA_POR_SPRITE.put("FragataTipo21", 2);
        VIDA_POR_SPRITE.put("AtlanticConveyor", 3);
        VIDA_POR_SPRITE.put("SeaHarrier", 2);
        VIDA_POR_SPRITE.put("WestlandLynx", 1);
        VIDA_POR_SPRITE.put("SeaKing", 3);
        // Add more mappings as needed
    }

    public final String identificadorSprite;
    public final int puntajeAlMorir;
    public final boolean isBoss;

    public SE_Enemigo(SE_GestorDeEntidades gestor,
                     float x, float y,
                     String sprite,
                     int puntaje,
                     boolean boss) {
        super(gestor, x, y);
        this.identificadorSprite = sprite;
        this.puntajeAlMorir = puntaje;
        this.isBoss = boss;
        // Assign default vida based on sprite type (fallback 1)
        this.vida = VIDA_POR_SPRITE.getOrDefault(sprite, 1);
    }

    public boolean puedeColisionarConNave() {
        return true;
    }

    @Override
    public abstract void actualizar();

    public void comportarseComoBuscador() {
        PApplet app = gestor.gp.getApp();
        SE_Objeto objetivo = null;
        if (!gestor.getNaves().isEmpty()) {
            for (SE_Nave n : gestor.getNaves()) {
                if (n.vivo) objetivo = n;
            }
        }
        if (objetivo != null) {
            float ang = PApplet.atan2(objetivo.y - y, objetivo.x - x);
            x += PApplet.cos(ang) * velocidad;
            y += PApplet.sin(ang) * velocidad;
        } else {
            y += velocidad;
        }
    }

    @Override
    public void dibujar() {
        PApplet app = gestor.gp.getApp();
        app.pushMatrix();
        app.translate(x, y);

        PImage sprite = (gfx != null) ? gfx.getImagen(identificadorSprite) : null;
        if (sprite != null) {
            if (isBoss) {
                gfx.dibujarConBorde(sprite, 0, 0, app.color(255, 0, 0));
            } else {
                gfx.dibujarConBorde(sprite, 0, 0, app.color(255, 50, 50));
            }
        } else {
            app.fill(isBoss ? app.color(180, 50, 50) : app.color(120, 120, 140));
            app.ellipse(0, 0, ancho, alto);
        }
        app.popMatrix();
    }
}