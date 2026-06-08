import processing.core.PApplet;

/**
 * Gestor de Entradas de Teclado Simplificado.
 */
public class SE_InputManager {

    public SE_InputManager() {
    }

    public void gestionarKeyPressed(char k, int kCode, EstadoJuego estadoActual, SE_GestorPrincipal gp) {
        if (estadoActual == null || gp == null)
            return;
        PApplet app = gp.getApp();
        String nombre = estadoActual.getNombre();

        switch (nombre) {
            case "INICIANDO":
                if (k == ' ')
                    gp.initGame();
                if (kCode == PApplet.ESC || k == 'q' || k == 'Q') {
                    app.key = 0; // Evitar que Processing se cierre solo
                    gp.notificar(ModuloEvento.Tipo.FINALIZADO, "Volviendo al lobby principal");
                }
                break;

            case "EN_EJECUCION":
                for (SE_Nave n : gp.entidades.getNaves()) {
                    if (!n.vivo)
                        continue;
                    if (kCode == PApplet.RIGHT || k == 'd' || k == 'D')
                        n.yendoDerecha = true;
                    if (kCode == PApplet.LEFT || k == 'a' || k == 'A')
                        n.yendoIzquierda = true;
                    if (kCode == PApplet.UP || k == 'w' || k == 'W')
                        n.yendoArriba = true;
                    if (kCode == PApplet.DOWN || k == 's' || k == 'S')
                        n.yendoAbajo = true;
                    if (k == ' ')
                        n.disparando = true;
                }
                if (k == 'p' || k == 'P') {
                    gp.notificar(ModuloEvento.Tipo.PAUSADO, "Pausa solicitada por jugador");
                }
                break;

            case "PAUSADO":
                if (k == 'p' || k == 'P') {
                    gp.notificar(ModuloEvento.Tipo.REANUDADO, "Juego reanudado");
                }
                break;

            case "FINALIZADO":
                if (k == 'r' || k == 'R') {
                    gp.setEstadoCicloVida(new IniciandoState());
                }
                if (kCode == PApplet.ESC || k == 'q' || k == 'Q') {
                    app.key = 0;
                    gp.notificar(ModuloEvento.Tipo.FINALIZADO, "Saliendo desde Game Over");
                }
                break;
        }
    }

    public void gestionarKeyReleased(char k, int kCode, EstadoJuego estadoActual, SE_GestorPrincipal gp) {
        if (estadoActual == null || gp == null)
            return;
        String nombre = estadoActual.getNombre();

        if (nombre.equals("EN_EJECUCION")) {
            for (SE_Nave n : gp.entidades.getNaves()) {
                if (kCode == PApplet.RIGHT || k == 'd' || k == 'D')
                    n.yendoDerecha = false;
                if (kCode == PApplet.LEFT || k == 'a' || k == 'A')
                    n.yendoIzquierda = false;
                if (kCode == PApplet.UP || k == 'w' || k == 'W')
                    n.yendoArriba = false;
                if (kCode == PApplet.DOWN || k == 's' || k == 'S')
                    n.yendoAbajo = false;
                if (k == ' ')
                    n.disparando = false;
            }
        }
    }
}