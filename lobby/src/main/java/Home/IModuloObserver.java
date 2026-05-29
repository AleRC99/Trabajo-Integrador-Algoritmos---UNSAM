package Home;

/**
 * Interfaz Observer: el Home la implementa para recibir
 * eventos de los modulos de juego.
 */
public interface IModuloObserver {

    void onEventoModulo(ModuloEvento evento);
}
