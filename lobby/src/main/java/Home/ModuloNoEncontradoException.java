package Home;

/**
 * Se lanza cuando se busca un modulo que no existe.
 */
public class ModuloNoEncontradoException extends JuegoException {

    public ModuloNoEncontradoException(String nombreModulo) {
        super("Modulo no encontrado: " + nombreModulo);
    }
}
