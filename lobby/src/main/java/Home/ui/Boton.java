package Home.ui;

import processing.core.PApplet;

/**
 * Boton reutilizable para la interfaz del Home.
 * Detecta hover, seleccion por teclado y clics.
 * Se dibuja con estilo retro.
 */
public class Boton {

    private float x;       // posicion X del centro
    private float y;       // posicion Y del centro
    private float ancho;
    private float alto;
    private String texto;
    private boolean seleccionado; // para navegacion por teclado

    public Boton(String texto, float x, float y, float ancho, float alto) {
        this.texto = texto;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.seleccionado = false;
    }

    /**
     * Dibuja el boton. Resaltado si esta seleccionado o si el mouse esta encima.
     */
    public void dibujar(PApplet app) {
        boolean resaltado = seleccionado || estaEncima(app.mouseX, app.mouseY);

        // Fondo del boton
        if (resaltado) {
            app.fill(0, 200, 0);       // verde claro
        } else {
            app.fill(0, 80, 0);        // verde oscuro
        }
        app.noStroke();
        app.rectMode(PApplet.CENTER);
        app.rect(x, y, ancho, alto, 5);

        // Indicador de seleccion por teclado
        if (seleccionado) {
            app.fill(0, 255, 0);
            app.textAlign(PApplet.RIGHT, PApplet.CENTER);
            app.textSize(14);
            app.text(">", x - ancho / 2 + 20, y - 2);
        }

        // Texto del boton
        app.fill(255);                  // blanco
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(14);
        app.text(texto, x, y - 2);
    }

    /**
     * Verifica si el mouse esta dentro del boton.
     */
    public boolean estaEncima(float mouseX, float mouseY) {
        return mouseX > x - ancho / 2 && mouseX < x + ancho / 2
            && mouseY > y - alto / 2 && mouseY < y + alto / 2;
    }

    public void setSeleccionado(boolean sel) { this.seleccionado = sel; }
    public boolean isSeleccionado() { return seleccionado; }
    public String getTexto() { return texto; }
}
