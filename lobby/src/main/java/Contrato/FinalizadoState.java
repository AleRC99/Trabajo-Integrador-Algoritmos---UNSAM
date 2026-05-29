package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado terminal: el modulo finalizo.
 * No permite ninguna transicion.
 */
public class FinalizadoState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya finalizo");
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya finalizo");
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya finalizo");
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya finalizo");
    }

    public String getNombre() {
        return "FINALIZADO";
    }
}
