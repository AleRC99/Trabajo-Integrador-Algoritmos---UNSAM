package Contrato;

import Home.EstadoInvalidoException;

/**
 * Interfaz del patron State para el ciclo de vida del modulo.
 * Cada estado concreto decide que transiciones son validas.
 */
public interface EstadoJuego {

    void iniciar(ModuloJuego modulo) throws EstadoInvalidoException;
    void pausar(ModuloJuego modulo) throws EstadoInvalidoException;
    void reanudar(ModuloJuego modulo) throws EstadoInvalidoException;
    void finalizar(ModuloJuego modulo) throws EstadoInvalidoException;
    String getNombre();
}
