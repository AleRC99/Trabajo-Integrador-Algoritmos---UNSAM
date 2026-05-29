package Home;

/**
 * Excepcion base del juego 1982.
 * Todas las excepciones del dominio heredan de esta.
 */
public abstract class JuegoException extends Exception {

    public JuegoException(String mensaje) {
        super(mensaje);
    }
}
