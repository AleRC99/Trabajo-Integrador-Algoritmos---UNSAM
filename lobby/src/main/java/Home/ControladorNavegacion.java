package Home;

import Home.ui.Pantalla;
import Contrato.ModuloJuego;

/**
 * Concentra las transiciones entre pantallas.
 * Mantiene la pantalla actual como un enum.
 */
public class ControladorNavegacion {

    private Pantalla pantallaActual;

    public ControladorNavegacion() {
        this.pantallaActual = Pantalla.INICIO;
    }

    public void irHome() {
        pantallaActual = Pantalla.INICIO;
    }

    public void irSeleccionModulo() {
        pantallaActual = Pantalla.SELECCION;
    }

    public void irEstadisticas() {
        pantallaActual = Pantalla.ESTADISTICAS;
    }

    public void iniciarModulo(ModuloJuego modulo) {
        pantallaActual = Pantalla.JUEGO;
    }

    public Pantalla getPantallaActual() {
        return pantallaActual;
    }
}
