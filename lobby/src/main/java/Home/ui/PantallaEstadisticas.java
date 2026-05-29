package Home.ui;

import processing.core.PApplet;
import Home.EstadisticasGenerales;
import java.util.List;

/**
 * Pantalla de estadisticas generales.
 * Muestra una tabla con las estadisticas de todos los modulos jugados.
 */
public class PantallaEstadisticas {

    private Boton botonVolver;

    public PantallaEstadisticas(float anchoPantalla, float altoPantalla) {
        botonVolver = new Boton("VOLVER", anchoPantalla / 2, altoPantalla - 60, 300, 45);
    }

    public void dibujar(PApplet app, List<EstadisticasGenerales> estadisticas, int puntajeTotal) {
        app.background(0);

        // Titulo
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(24);
        app.text("ESTADISTICAS", app.width / 2, 40);

        // Linea
        app.stroke(0, 120, 0);
        app.line(50, 70, app.width - 50, 70);
        app.noStroke();

        if (estadisticas.isEmpty()) {
            // Sin datos
            app.fill(0, 180, 0);
            app.textSize(12);
            app.text("No hay estadisticas todavia.", app.width / 2, app.height / 2 - 20);
            app.text("Juga un modulo primero!", app.width / 2, app.height / 2 + 10);
        } else {
            // Encabezados de tabla
            app.fill(0, 255, 0);
            app.textSize(10);
            app.textAlign(PApplet.LEFT, PApplet.TOP);

            float y = 90;
            float col1 = 50;   // Modulo
            float col2 = 220;  // Puntaje
            float col3 = 320;  // Partidas
            float col4 = 420;  // Ganadas
            float col5 = 520;  // Enemigos
            float col6 = 640;  // Tiempo

            app.text("MODULO", col1, y);
            app.text("PUNTAJE", col2, y);
            app.text("PARTIDAS", col3, y);
            app.text("GANADAS", col4, y);
            app.text("ENEMIGOS", col5, y);
            app.text("TIEMPO", col6, y);

            y += 5;
            app.stroke(0, 80, 0);
            app.line(col1, y + 15, app.width - 50, y + 15);
            app.noStroke();

            // Filas de datos
            app.fill(0, 200, 0);
            y += 25;
            for (EstadisticasGenerales stats : estadisticas) {
                app.text(stats.getNombreModulo(), col1, y);
                app.text("" + stats.getPuntajeTotal(), col2, y);
                app.text("" + stats.getPartidasJugadas(), col3, y);
                app.text("" + stats.getPartidasGanadas(), col4, y);
                app.text("" + stats.getEnemigosDestruidos(), col5, y);
                app.text(stats.getTiempoJugadoSegundos() + "s", col6, y);
                y += 25;
            }

            // Puntaje total
            y += 15;
            app.fill(0, 255, 0);
            app.textSize(12);
            app.text("PUNTAJE TOTAL: " + puntajeTotal, col1, y);
        }

        // Boton volver
        botonVolver.dibujar(app);
    }

    public boolean clicEnVolver(float mouseX, float mouseY) {
        return botonVolver.estaEncima(mouseX, mouseY);
    }
}
