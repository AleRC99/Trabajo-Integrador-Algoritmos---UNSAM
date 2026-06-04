import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class SE_ModuloEtendard implements ModuloJuego {

    private EstadoJuego estadoCicloVida;
    private final List<IModuloObserver> observers;
    private ContextoJuego contexto;
    private SE_GestorPrincipal coreJuego;

    public SE_ModuloEtendard(PApplet app) {
        this.observers = new ArrayList<>();
        this.estadoCicloVida = new NoIniciadoState();
        this.coreJuego = new SE_GestorPrincipal(app, this);
    }

    @Override 
    public String getNombreModulo() { 
        return "super_etendard"; 
    }

    @Override 
    public String getDescripcion() { 
        return "Shooter aereo de la Guerra de Malvinas. Destruye la flota britanica."; 
    }

    @Override 
    public String getNombreAvion() { 
        return "Super Etendard"; 
    }

    @Override
    public void inicializarContexto(ContextoJuego ctx) {
        this.contexto = ctx;
        try {
            this.estadoCicloVida.iniciar(this);
            this.estadoCicloVida = new IniciandoState();
            this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
            this.coreJuego.initGameVariables(); // No iniciar ejecución aún
            notificar(ModuloEvento.Tipo.INICIADO, "Juego 1942 listo");
        } catch (EstadoInvalidoException e) {
            this.estadoCicloVida = new ErrorState();
            this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
            notificar(ModuloEvento.Tipo.ERROR, e.getMessage());
        }
    }

    public void comenzarEjecucion() {
        this.estadoCicloVida = new EnEjecucionState();
        this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
    }

    @Override
    public void pausar() throws EstadoInvalidoException {
        this.estadoCicloVida.pausar(this);
        this.estadoCicloVida = new PausadoState();
        this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
        notificar(ModuloEvento.Tipo.PAUSADO, "Juego pausado");
    }

    @Override
    public void reanudar() throws EstadoInvalidoException {
        this.estadoCicloVida.reanudar(this);
        this.estadoCicloVida = new EnEjecucionState();
        this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
        notificar(ModuloEvento.Tipo.REANUDADO, "Juego reanudado");
    }

    @Override
    public void finalizar() throws EstadoInvalidoException {
        this.estadoCicloVida.finalizar(this);
        if (!(estadoCicloVida instanceof FinalizadoState)) {
             // Si el juego no registró victoria, forzamos derrota al salir manualmente
             if (!coreJuego.isVictoriaRegistrada()) {
                 coreJuego.consolidarPartida(false);
             }
             this.estadoCicloVida = new FinalizadoState();
             this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
             notificar(ModuloEvento.Tipo.FINALIZADO, "Finalizado");
        }
    }

    @Override
    public EstadoJuego getEstado() {
        return estadoCicloVida;
    }

    @Override
    public EstadisticasGenerales getEstadisticasGenerales() {
        return coreJuego.getEstadisticasGenerales(getNombreModulo());
    }

    @Override
    public void agregarObserver(IModuloObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removerObserver(IModuloObserver obs) {
        observers.remove(obs);
    }

    @Override
    public void actualizar(PApplet app) {
        // En este diseño, el coreJuego actualiza todo en dibujar
    }

    @Override
    public void dibujar(PApplet app) {
        coreJuego.getGraficos().renderizarSegunEstado(estadoCicloVida);
    }

    @Override
    public void iniciar() throws EstadoInvalidoException {
        // La inicialización real ocurre en inicializarContexto,
        // pero proveemos esta implementación para cumplir la interfaz.
        if (estadoCicloVida instanceof NoIniciadoState) {
            estadoCicloVida.iniciar(this);
            this.estadoCicloVida = new IniciandoState();
            this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
            coreJuego.initGameVariables();
        }
    }

    @Override
    public void reset() {
        this.estadoCicloVida = new NoIniciadoState();
        this.coreJuego.setEstadoCicloVida(this.estadoCicloVida);
        this.coreJuego.initGameVariables();
    }

    // --- Método para que el Core notifique a la fachada ---
    public void notificarDesdeCore(ModuloEvento.Tipo tipo, String mensaje) {
        if (tipo == ModuloEvento.Tipo.FINALIZADO) {
            try {
                finalizar();
            } catch (EstadoInvalidoException e) {}
        } else {
            notificar(tipo, mensaje);
        }
    }

    private void notificar(ModuloEvento.Tipo tipo, String mensaje) {
        ModuloEvento ev = new ModuloEvento(tipo, getNombreModulo(), mensaje);
        for (IModuloObserver obs : new ArrayList<>(observers)) {
            obs.onEventoModulo(ev);
        }
    }
}
