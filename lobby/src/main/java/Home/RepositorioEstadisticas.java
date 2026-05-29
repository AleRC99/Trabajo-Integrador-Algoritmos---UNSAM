package Home;

import java.util.List;

/**
 * Interfaz para persistencia de estadisticas.
 * Abstrae como se guardan y cargan los datos.
 */
public interface RepositorioEstadisticas {

    void guardar(String nombreModulo, EstadisticasGenerales stats) throws PersistenciaException;

    EstadisticasGenerales cargar(String nombreModulo) throws PersistenciaException;

    List<EstadisticasGenerales> cargarTodas() throws PersistenciaException;
}
