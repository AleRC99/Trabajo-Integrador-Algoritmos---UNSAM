package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado de ejecucion: el modulo esta corriendo.
 * Permite: pausar(), finalizar()
 */
public class EnEjecucionState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya esta en ejecucion");
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: pasa a Pausado
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya esta en ejecucion");
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: pasa a Finalizado
    }

    public String getNombre() {
        return "EN_EJECUCION";
    }
}
