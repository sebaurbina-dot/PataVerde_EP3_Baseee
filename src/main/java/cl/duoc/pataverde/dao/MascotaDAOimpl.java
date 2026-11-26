package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Mascota;

import java.util.List;

public class MascotaDAOimpl implements  MascotaDAO{
    @Override
    public List<Mascota> obtenerTodos() throws PersistenciaException {
        return List.of();
    }

    @Override
    public void insertar(Mascota mascota) throws PersistenciaException {

    }

    @Override
    public void actualizar(Mascota mascota) throws PersistenciaException {

    }

    @Override
    public void eliminar(int id) throws PersistenciaException {

    }
}
