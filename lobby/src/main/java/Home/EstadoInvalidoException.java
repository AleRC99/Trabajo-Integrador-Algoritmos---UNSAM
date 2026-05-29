package Home;

/**
 * Se lanza cuando se intenta una transicion de estado invalida.
 */
public class EstadoInvalidoException extends JuegoException {

    public EstadoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
