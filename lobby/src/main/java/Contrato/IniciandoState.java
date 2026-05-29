package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado de carga: el modulo se esta inicializando.
 * Permite: finalizar() (por si hay error en la carga)
 */
public class IniciandoState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("El modulo ya se esta iniciando");
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("No se puede pausar durante la carga");
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("No se puede reanudar durante la carga");
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: abortar carga
    }

    public String getNombre() {
        return "INICIANDO";
    }
}
