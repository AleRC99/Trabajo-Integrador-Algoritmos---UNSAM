package Home.ui;

import processing.core.PApplet;

/**
 * Pantalla de inicio: muestra el titulo "1982" y un boton START.
 * Controles: ENTER o clic para iniciar.
 */
public class PantallaInicio {

    private Boton botonStart;

    public PantallaInicio(float anchoPantalla, float altoPantalla) {
        this.botonStart = new Boton("START", anchoPantalla / 2, altoPantalla / 2 + 80, 200, 50);
        this.botonStart.setSeleccionado(true); // siempre seleccionado (unico boton)
    }

    public void dibujar(PApplet app) {
        app.background(0);  // fondo negro

        // Titulo "1982" grande y verde
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(64);
        app.text("1982", app.width / 2, app.height / 2 - 60);

        // Subtitulo
        app.fill(0, 200, 0);
        app.textSize(12);
        app.text("GUERRA DE MALVINAS", app.width / 2, app.height / 2);

        // Boton START
        botonStart.dibujar(app);

        // Texto parpadeante
        if (app.frameCount % 60 < 40) {
            app.fill(0, 180, 0);
            app.textSize(10);
            app.text(">>> PRESS ENTER <<<", app.width / 2, app.height / 2 + 140);
        }
    }

    /**
     * Devuelve true si se hizo clic en START.
     */
    public boolean clicEnStart(float mouseX, float mouseY) {
        return botonStart.estaEncima(mouseX, mouseY);
    }
}
