package Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Contrato.ModuloJuego;

/**
 * Administra la coleccion de modulos disponibles.
 * Punto unico para registrar, buscar y listar modulos.
 */
public class GestorModulos {

    private List<ModuloJuego> modulos;

    public GestorModulos() {
        this.modulos = new ArrayList<ModuloJuego>();
    }

    public void agregarModulo(ModuloJuego modulo) {
        modulos.add(modulo);
    }

    public boolean eliminarModulo(String nombreModulo) {
        for (int i = 0; i < modulos.size(); i++) {
            if (modulos.get(i).getNombreModulo().equals(nombreModulo)) {
                modulos.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un modulo por nombre.
     * @throws ModuloNoEncontradoException si no existe
     */
    public ModuloJuego buscarModulo(String nombreModulo) throws ModuloNoEncontradoException {
        for (ModuloJuego modulo : modulos) {
            if (modulo.getNombreModulo().equals(nombreModulo)) {
                return modulo;
            }
        }
        throw new ModuloNoEncontradoException(nombreModulo);
    }

    /**
     * Devuelve una lista no modificable de los modulos registrados.
     */
    public List<ModuloJuego> listarModulos() {
        return Collections.unmodifiableList(modulos);
    }
}
