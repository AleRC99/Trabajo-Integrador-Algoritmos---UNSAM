package Home;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion concreta de RepositorioEstadisticas.
 * Guarda cada modulo como un archivo .dat usando serializacion Java.
 */
public class RepositorioEstadisticasArchivo implements RepositorioEstadisticas {

    private final String directorio;

    public RepositorioEstadisticasArchivo(String directorio) {
        this.directorio = directorio;
        // Crear directorio si no existe
        File dir = new File(directorio);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void guardar(String nombreModulo, EstadisticasGenerales stats) throws PersistenciaException {
        File archivo = obtenerArchivo(nombreModulo);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
            oos.writeObject(stats);
            oos.close();
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar estadisticas de " + nombreModulo + ": " + e.getMessage());
        }
    }

    public EstadisticasGenerales cargar(String nombreModulo) throws PersistenciaException {
        File archivo = obtenerArchivo(nombreModulo);
        if (!archivo.exists()) {
            return null; // No hay estadisticas guardadas para este modulo
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
            EstadisticasGenerales stats = (EstadisticasGenerales) ois.readObject();
            ois.close();
            return stats;
        } catch (IOException e) {
            throw new PersistenciaException("Error al cargar estadisticas de " + nombreModulo + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new PersistenciaException("Archivo corrupto para " + nombreModulo);
        }
    }

    public List<EstadisticasGenerales> cargarTodas() throws PersistenciaException {
        List<EstadisticasGenerales> todas = new ArrayList<EstadisticasGenerales>();
        File dir = new File(directorio);
        File[] archivos = dir.listFiles();
        if (archivos == null) {
            return todas;
        }
        for (File archivo : archivos) {
            if (archivo.getName().endsWith(".dat")) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
                    EstadisticasGenerales stats = (EstadisticasGenerales) ois.readObject();
                    ois.close();
                    todas.add(stats);
                } catch (Exception e) {
                    // Archivo corrupto, lo salteamos
                    System.err.println("Advertencia: no se pudo leer " + archivo.getName());
                }
            }
        }
        return todas;
    }

    private File obtenerArchivo(String nombreModulo) {
        // Sanitizar nombre para usarlo como nombre de archivo
        String nombreArchivo = nombreModulo.replaceAll("[^a-zA-Z0-9]", "_") + ".dat";
        return new File(directorio, nombreArchivo);
    }
}
