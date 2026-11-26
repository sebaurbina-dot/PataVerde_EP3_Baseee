package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Mascota;

import java.util.List;

/**
 * Operaciones de almacenamiento que utiliza la ventana.
 * <p>
 * Si una operación no puede completarse, debe lanzar PersistenciaException con un
 * mensaje que indique qué operación falló; la ventana muestra ese mensaje en el
 * área de mensajes.
 */
public interface MascotaDAO {

    /**
     * @return todas las mascotas almacenadas, cada una como objeto de su tipo
     *         (Perro o Gato), con todos sus datos y su identificador.
     */
    List<Mascota> obtenerTodos() throws PersistenciaException;

    /**
     * Guarda una mascota nueva con todos sus datos, incluidos los propios de su tipo.
     * El identificador lo genera la base de datos.
     */
    void insertar(Mascota mascota) throws PersistenciaException;

    /**
     * Modifica todos los datos de la mascota que tiene el mismo identificador,
     * incluidos los propios de su tipo.
     */
    void actualizar(Mascota mascota) throws PersistenciaException;

    /**
     * Elimina la mascota con el identificador indicado.
     */
    void eliminar(int id) throws PersistenciaException;
}
