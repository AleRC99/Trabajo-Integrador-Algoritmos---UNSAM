package Aviones.etendard;

import Contrato.EstadoJuego;
import processing.core.PApplet;

/**
 * Gestor de Entradas de Teclado.
 * Centraliza la captura de teclado de Processing y la direcciona según el
 * estado actual de la máquina de estados del contrato del Lobby.
 */
public class SE_InputManager {

    public SE_InputManager() {
    }

    public void gestionarKeyPressed(char k, int kCode, EstadoJuego estadoActual, SE_GestorPrincipal gp) {
        if (estadoActual == null || gp == null) return;
        PApplet app = gp.getApp();
        String nombre = estadoActual.getNombre();

        switch (nombre) {
            case "NO_INICIADO":
                if (k == 'w' || k == 'W') gp.setSelNivel(2);
                if (k == 'e' || k == 'E') gp.setSelNivel(3);
                if (k == ' ') gp.initGame();
                if (kCode == PApplet.ESC || k == 'q' || k == 'Q') {
                    app.key = 0; // Evita cierre por defecto de Processing
                    gp.volverAlLobby();
                }
                break;

            case "EN_EJECUCION":
                for (SE_Nave n : gp.entidades.getNaves()) {
                    if (!n.isVivo()) continue;
                    if (kCode == PApplet.RIGHT) n.setYendoDerecha(true);
                    if (kCode == PApplet.LEFT)  n.setYendoIzquierda(true);
                    if (kCode == PApplet.UP)    n.setYendoArriba(true);
                    if (kCode == PApplet.DOWN)  n.setYendoAbajo(true);
                    if (k == ' ')               n.setDisparando(true);
                }
                if (k == 'p' || k == 'P') {
                    gp.pausar();
                }
                break;

            case "PAUSADO":
                if (k == 'p' || k == 'P') {
                    gp.reanudar();
                }
                break;

            case "FINALIZADO":
                if (k == 'r' || k == 'R') {
                    gp.volverAlMenu();
                }
                if (kCode == PApplet.ESC || k == 'q' || k == 'Q') {
                    app.key = 0;
                    gp.volverAlLobby();
                }
                break;
        }
    }

    public void gestionarKeyReleased(char k, int kCode, EstadoJuego estadoActual, SE_GestorPrincipal gp) {
        if (estadoActual == null || gp == null) return;
        String nombre = estadoActual.getNombre();

        if (nombre.equals("EN_EJECUCION")) {
            for (SE_Nave n : gp.entidades.getNaves()) {
                if (kCode == PApplet.RIGHT) n.setYendoDerecha(false);
                if (kCode == PApplet.LEFT)  n.setYendoIzquierda(false);
                if (kCode == PApplet.UP)    n.setYendoArriba(false);
                if (kCode == PApplet.DOWN)  n.setYendoAbajo(false);
                if (k == ' ')               n.setDisparando(false);
            }
        }
    }
}
