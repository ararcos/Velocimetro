package velo.uned.velocimetro.servicios;

import android.content.Context;

import java.util.ArrayList;

import velo.uned.velocimetro.dao.RutaDAO;
import velo.uned.velocimetro.modelo.Ruta;

/**
 * Created by alexa on 19/03/2018.
 */

public class RutaServicios {
    private RutaDAO rutaDAO;

    public RutaServicios(Context context) {
        rutaDAO = new RutaDAO(context);
    }

    public boolean addRuta(Ruta ruta) {

        return  rutaDAO.insertar(ruta);
    }
    public boolean addAllRuta(ArrayList<Ruta> rutalist,Long id) {

        return  rutaDAO.insertar(rutalist,id);
    }
    public boolean updateRuta(Ruta ruta) {

        return  rutaDAO.alterar(ruta);
    }public boolean deleteRuta(Ruta ruta) {

        return  rutaDAO.borrar(ruta);
    }
    public Ruta getRuta(Ruta ruta) {
        Ruta nuRuta = rutaDAO.getRuta(ruta.getId());
        return nuRuta;
    }
    public ArrayList<Ruta> getallMedicion() {
        ArrayList<Ruta> rutalist = rutaDAO.listar();
        return rutalist;
    }
}
