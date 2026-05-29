package Home;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Punto de entrada de la aplicacion.
 * Extiende PApplet de Processing.
 * Delega toda la logica a HomeJuego.
 */
public class GameApp extends PApplet {

    private HomeJuego homeJuego;
    private PFont fuentePixel;

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        // Cargar fuente retro
        fuentePixel = createFont("fonts/PressStart2P-Regular.ttf", 24);
        textFont(fuentePixel);

        // Crear Home y registrar modulos
        homeJuego = new HomeJuego(this);

        // Registrar el modulo de prueba
        homeJuego.registrarModulo(new Aviones.AvionPrueba.ModuloPrueba());

        // Registrar el modulo de Super Etendard (1942)
        homeJuego.registrarModulo(new Aviones.etendard.SE_GestorPrincipal(this));

        // Inicializar
        homeJuego.iniciarHome();
    }

    public void draw() {
        homeJuego.dibujar();
    }

    public void keyPressed() {
        homeJuego.manejarTecla(key, keyCode);
    }

    public void keyReleased() {
        homeJuego.manejarTeclaLiberada(key, keyCode);
    }

    public void mousePressed() {
        homeJuego.manejarClick(mouseX, mouseY);
    }

    // Punto de entrada
    public static void main(String[] args) {
        PApplet.main("Home.GameApp");
    }
}
