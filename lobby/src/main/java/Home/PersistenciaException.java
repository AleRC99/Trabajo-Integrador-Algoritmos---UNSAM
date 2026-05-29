package Home;

/**
 * Se lanza cuando hay un error al guardar o cargar datos.
 */
public class PersistenciaException extends JuegoException {

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
}
