package Home;

/**
 * Evento que un modulo envia al Home via el patron Observer.
 * Contiene el tipo de evento, el nombre del modulo y un mensaje opcional.
 */
public class ModuloEvento {

    /** Tipos de evento posibles */
    public enum Tipo {
        INICIADO,
        PAUSADO,
        REANUDADO,
        FINALIZADO,
        ERROR
    }

    private final Tipo tipo;
    private final String nombreModulo;
    private final String mensaje;

    public ModuloEvento(Tipo tipo, String nombreModulo, String mensaje) {
        this.tipo = tipo;
        this.nombreModulo = nombreModulo;
        this.mensaje = mensaje;
    }

    public ModuloEvento(Tipo tipo, String nombreModulo) {
        this(tipo, nombreModulo, "");
    }

    public Tipo getTipo() { return tipo; }
    public String getNombreModulo() { return nombreModulo; }
    public String getMensaje() { return mensaje; }
}
