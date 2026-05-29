package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado pausado: el modulo esta pausado.
 * Permite: reanudar(), finalizar()
 */
public class PausadoState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("No se puede iniciar: el modulo esta pausado");
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya esta pausado");
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: vuelve a EnEjecucion
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: pasa a Finalizado
    }

    public String getNombre() {
        return "PAUSADO";
    }
}
