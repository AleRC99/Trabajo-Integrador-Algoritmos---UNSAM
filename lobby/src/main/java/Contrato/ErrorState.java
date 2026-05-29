package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado de error: ocurrio un error en el modulo.
 * Solo permite finalizar() para limpieza.
 */
public class ErrorState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo esta en estado de error");
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo esta en estado de error");
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo esta en estado de error");
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: limpieza tras error
    }

    public String getNombre() {
        return "ERROR";
    }
}
