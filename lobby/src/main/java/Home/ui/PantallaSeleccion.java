package Home.ui;

import processing.core.PApplet;
import Contrato.ModuloJuego;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de seleccion de modulos.
 * Muestra un boton por cada modulo registrado, mas botones de
 * ESTADISTICAS y VOLVER.
 *
 * Controles: W/S para navegar, ENTER para seleccionar, ESC para volver.
 */
public class PantallaSeleccion {

    private List<Boton> botonesModulos;
    private List<String> nombresModulos;
    private Boton botonEstadisticas;
    private Boton botonVolver;

    // Todos los botones en orden para navegacion por teclado
    private List<Boton> todosBotones;
    private int indiceSeleccion;

    /**
     * Construye la pantalla con los modulos disponibles.
     */
    public PantallaSeleccion(List<ModuloJuego> modulos, float anchoPantalla, float altoPantalla) {
        botonesModulos = new ArrayList<Boton>();
        nombresModulos = new ArrayList<String>();
        todosBotones = new ArrayList<Boton>();

        float centroX = anchoPantalla / 2;
        float inicioY = 160;

        // Un boton por cada modulo
        for (int i = 0; i < modulos.size(); i++) {
            ModuloJuego modulo = modulos.get(i);
            String nombre = modulo.getNombreAvion();
            float y = inicioY + i * 60;
            Boton boton = new Boton(nombre, centroX, y, 300, 45);
            botonesModulos.add(boton);
            nombresModulos.add(modulo.getNombreModulo());
            todosBotones.add(boton);
        }

        // Boton estadisticas
        float yStats = inicioY + modulos.size() * 60 + 20;
        botonEstadisticas = new Boton("ESTADISTICAS", centroX, yStats, 300, 45);
        todosBotones.add(botonEstadisticas);

        // Boton volver
        botonVolver = new Boton("VOLVER", centroX, yStats + 60, 300, 45);
        todosBotones.add(botonVolver);

        // Seleccionar el primer boton por defecto
        indiceSeleccion = 0;
        actualizarSeleccion();
    }

    /** Actualiza cual boton esta seleccionado visualmente */
    private void actualizarSeleccion() {
        for (int i = 0; i < todosBotones.size(); i++) {
            todosBotones.get(i).setSeleccionado(i == indiceSeleccion);
        }
    }

    /** Mueve la seleccion hacia arriba (W) */
    public void moverArriba() {
        indiceSeleccion--;
        if (indiceSeleccion < 0) {
            indiceSeleccion = todosBotones.size() - 1; // wrap around
        }
        actualizarSeleccion();
    }

    /** Mueve la seleccion hacia abajo (S) */
    public void moverAbajo() {
        indiceSeleccion++;
        if (indiceSeleccion >= todosBotones.size()) {
            indiceSeleccion = 0; // wrap around
        }
        actualizarSeleccion();
    }

    /**
     * Confirma la seleccion actual (ENTER).
     * Devuelve: nombre del modulo, "ESTADISTICAS", "VOLVER", o null.
     */
    public String confirmarSeleccion() {
        Boton seleccionado = todosBotones.get(indiceSeleccion);

        // Es un boton de modulo?
        for (int i = 0; i < botonesModulos.size(); i++) {
            if (botonesModulos.get(i) == seleccionado) {
                return nombresModulos.get(i);
            }
        }

        // Es estadisticas o volver?
        return seleccionado.getTexto(); // "ESTADISTICAS" o "VOLVER"
    }

    public void dibujar(PApplet app) {
        app.background(0);

        // Titulo
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(24);
        app.text("SELECCIONAR AVION", app.width / 2, 60);

        // Linea decorativa
        app.stroke(0, 120, 0);
        app.line(100, 95, app.width - 100, 95);
        app.noStroke();

        // Botones de modulos
        for (Boton boton : botonesModulos) {
            boton.dibujar(app);
        }

        // Boton estadisticas y volver
        botonEstadisticas.dibujar(app);
        botonVolver.dibujar(app);

        // Instrucciones de controles
        app.fill(0, 120, 0);
        app.textSize(8);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.text("W/S: navegar  |  ENTER: seleccionar  |  ESC: volver", app.width / 2, app.height - 20);
    }

    /**
     * Verifica si se hizo clic en algun modulo.
     * Devuelve el nombre del modulo o null si no se hizo clic en ninguno.
     */
    public String clicEnModulo(float mouseX, float mouseY) {
        for (int i = 0; i < botonesModulos.size(); i++) {
            if (botonesModulos.get(i).estaEncima(mouseX, mouseY)) {
                return nombresModulos.get(i);
            }
        }
        return null;
    }

    public boolean clicEnEstadisticas(float mouseX, float mouseY) {
        return botonEstadisticas.estaEncima(mouseX, mouseY);
    }

    public boolean clicEnVolver(float mouseX, float mouseY) {
        return botonVolver.estaEncima(mouseX, mouseY);
    }
}
