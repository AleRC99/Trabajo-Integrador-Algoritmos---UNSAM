package Contrato;

import Home.EstadoInvalidoException;

/**
 * Estado inicial: el modulo fue creado pero no se inicio.
 * Permite: iniciar(), finalizar()
 */
public class NoIniciadoState implements EstadoJuego {

    public void iniciar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: pasa a EnEjecucion
    }

    public void pausar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("No se puede pausar: el modulo no fue iniciado");
    }

    public void reanudar(ModuloJuego modulo) throws EstadoInvalidoException {
        throw new EstadoInvalidoException("No se puede reanudar: el modulo no fue iniciado");
    }

    public void finalizar(ModuloJuego modulo) throws EstadoInvalidoException {
        // Transicion valida: pasa a Finalizado
    }

    public String getNombre() {
        return "NO_INICIADO";
    }
}
