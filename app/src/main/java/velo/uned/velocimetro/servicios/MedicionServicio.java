package velo.uned.velocimetro.servicios;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import velo.uned.velocimetro.dao.MedicionDAO;
import velo.uned.velocimetro.modelo.Medicion;

/**
 * Created by alexa on 19/03/2018.
 */

public class MedicionServicio {
    private MedicionDAO medicionDAO;

    public MedicionServicio(Context context) {
        medicionDAO = new MedicionDAO(context);
    }

    public boolean addMedicion(Medicion medicion) {

       return  medicionDAO.insertar(medicion);

    }
    public boolean updateMedicion(Medicion medicion) {

        return medicionDAO.alterar(medicion);

    }
    public boolean deleteMedicion(Medicion medicion) {

        return medicionDAO.borrar(medicion);

    }
    public Medicion getMedicion(Medicion medicion) {
        Medicion nuMedicion = medicionDAO.getMedicion(medicion.getId());
        return nuMedicion;
    }
    public ArrayList<Medicion> getallMedicion() {
        ArrayList<Medicion> medicionList = medicionDAO.listar();
        return medicionList;
    }
}
