
import processing.core.PApplet;

/**
 * Gestor de Nivel.
 * Encapsula la línea temporal (tiempoNivel), el timer de peligro de bosses,
 * la fase actual (Inicio, Medio, Final) y la lógica de spawning de oleadas 
 * de enemigos y el boss final.
 */
public class SE_GestorDeNivel {
    private int tiempoNivel = 0;
    private int pausaPeligroTimer = 0;
    private int faseActual = 1;
    private boolean bossSpawned = false;

    // Constantes como campos de instancia final
    private final int INTERVALO_LYNX         = 400;
    private final int INTERVALO_HARRIER      = 150;
    private final int INTERVALO_SEAKING      = 600;
    private final int INTERVALO_FRAGATA      = 800;
    private final int INTERVALO_CONVEYOR     = 1500;
    private final int TIEMPO_FASE_MEDIO_BASE = 1500;
    private final int TIEMPO_FASE_FINAL_BASE = 3000;

    public SE_GestorDeNivel() {
    }

    public void resetear() {
        this.tiempoNivel = 0;
        this.pausaPeligroTimer = 0;
        this.faseActual = SE_GestorPrincipal.FASE_INICIO;
        this.bossSpawned = false;
    }

    public void actualizar(SE_GestorPrincipal sesion, SE_GestorDeEntidades entidades) {
        this.tiempoNivel++;

        if (this.pausaPeligroTimer > 0) {
            this.pausaPeligroTimer--;
            return;
        }

        int t = this.tiempoNivel;
        PApplet app = sesion.getApp();

        // Calcular fase actual según dificultad
        int paseMedio = TIEMPO_FASE_MEDIO_BASE + (sesion.getDificultad() * 300);
        int paseFinal = TIEMPO_FASE_FINAL_BASE + (sesion.getDificultad() * 600);

        if      (t < paseMedio) this.faseActual = SE_GestorPrincipal.FASE_INICIO;
        else if (t < paseFinal) this.faseActual = SE_GestorPrincipal.FASE_MEDIO;
        else                    this.faseActual = SE_GestorPrincipal.FASE_FINAL;

        // --- OLEADAS FASE INICIO Y MEDIO ---
        if (this.faseActual != SE_GestorPrincipal.FASE_FINAL) {
            if (t % INTERVALO_LYNX == 0 && t > 0) {
                float px = app.random(50, app.width - 50);
                entidades.agregarEnemigo(new SE_Enemigo(entidades, px,       -30, "WestlandLynx"));
                entidades.agregarEnemigo(new SE_Enemigo(entidades, px + 40,  -60, "WestlandLynx"));
                entidades.agregarEnemigo(new SE_Enemigo(entidades, px - 40,  -90, "WestlandLynx"));
            }
            if (t % INTERVALO_HARRIER == 0 && t > 0) {
                entidades.agregarEnemigo(new SE_Enemigo(entidades, app.random(100, app.width - 100), -30, "SeaHarrier"));
            }
            if (t % INTERVALO_SEAKING == 0 && t > 0) {
                float dir = (app.random(1) < 0.5f) ? 1 : -1;
                float sx  = (dir == 1) ? -50 : app.width + 50;
                SE_Enemigo sk = new SE_Enemigo(entidades, sx, app.random(100, 300), "SeaKing");
                sk.dirX = dir;
                entidades.agregarEnemigo(sk);
            }
        }

        // --- OLEADAS SOLO EN FASE MEDIO ---
        if (this.faseActual == SE_GestorPrincipal.FASE_MEDIO) {
            if (t % INTERVALO_FRAGATA == 0 && t > 0) {
                entidades.agregarEnemigo(new SE_Enemigo(entidades, app.random(100, app.width - 100), -100, "FragataTipo21"));
            }
            if (t % INTERVALO_CONVEYOR == 0 && t > 0) {
                entidades.agregarEnemigo(new SE_Enemigo(entidades, app.width / 2.0f + app.random(-100, 100), -150, "AtlanticConveyor"));
            }
        }

        // --- BOSS EN FASE FINAL ---
        if (this.faseActual == SE_GestorPrincipal.FASE_FINAL && !this.bossSpawned) {
            if (entidades.cantEnemigos() <= 1) {
                entidades.vaciarEnemigos(); // limpia el ultimo rezagado si lo hay
                entidades.agregarEnemigo(new SE_Enemigo(entidades, app.width / 2.0f, -200, "HmsSheffield"));
                this.bossSpawned = true;
                System.out.println("[GestorDeNivel] Boss spawneado en t=" + t);
            }
        }
    }

    // Getters & Setters
    public int getTiempoNivel() { return tiempoNivel; }
    public int getFaseActual() { return faseActual; }
    public boolean isBossSpawned() { return bossSpawned; }
    public int getPausaPeligroTimer() { return pausaPeligroTimer; }

    public void setPausaPeligroTimer(int t) { this.pausaPeligroTimer = t; }
    public void marcarBossSpawned() { this.bossSpawned = true; }
}