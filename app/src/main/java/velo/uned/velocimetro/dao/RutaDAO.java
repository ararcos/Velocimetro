package velo.uned.velocimetro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;

import velo.uned.velocimetro.datos.Conexion;
import velo.uned.velocimetro.modelo.Ruta;

/**
 * Created by alexa on 19/03/2018.
 */

public class RutaDAO {
    Context context;
    Conexion dbsqLite;
    public static ArrayList<Ruta> listaRutas;

    public RutaDAO(Context context) {
        this.context = context;
        dbsqLite = new Conexion(context);
        listar();
    }

    public boolean insertar(Ruta ruta) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ruta.campo_latitud, ruta.getLatitud());
        values.put(ruta.campo_longitud, ruta.getLongitud());
        values.put(ruta.campo_id_medicion,ruta.getId_medicion());
        Long id = db.insert(ruta.tabla, null, values);
        db.close();
        if (id > 0) {
            ruta.setId(id);
        }
        return id > 0;
    }
    public boolean insertar(ArrayList<Ruta> rutalist,Long id_med) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ContentValues values;
        Long id = null;
        for (Ruta nuRuta : listaRutas){
            values = new ContentValues();
            values.put(nuRuta.campo_latitud, nuRuta.getLatitud());
            values.put(nuRuta.campo_longitud, nuRuta.getLongitud());
            values.put(nuRuta.campo_id_medicion,id_med);
            id= db.insert(nuRuta.tabla, null, values);
            if (id > 0) {
            nuRuta.setId(id);
            }
        }
        db.close();
        return id > 0;
    }

    public boolean alterar(Ruta ruta) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ruta.campo_latitud, ruta.getLatitud());
        values.put(ruta.campo_longitud, ruta.getLongitud());
        values.put(ruta.campo_id_medicion,ruta.getId_medicion());
        String where = ruta.campo_id + " = ?";

        int id = db.update(ruta.tabla, values, where, new String[]{String.valueOf(ruta.getId())});
        db.close();
        return id > 0;
    }


    public boolean borrar(Ruta ruta ) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        String where = ruta.campo_id + " = ?";
        int ret = db.delete(ruta.tabla, where, new String[]{String.valueOf(ruta.getId())});
        db.close();
        return ret > 0;
    }
    public Ruta getRuta(Long id){
        SQLiteDatabase db =dbsqLite.getReadableDatabase();
        Ruta ruta  =new Ruta();
        String [] campos={Ruta.campo_longitud,Ruta.campo_longitud,Ruta.campo_id_medicion};
        String [] parametro={id.toString()};
        String where = Ruta.campo_id + " = ?";
        Cursor cursor = db.query(Ruta.tabla, campos, where, parametro, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ruta.setId(id);
        ruta.setLatitud(cursor.getDouble(0));
        ruta.setLongitud(cursor.getDouble(1));
        ruta.setId_medicion(cursor.getLong(2));
        return ruta;
    }
    public ArrayList<Ruta> listar() {
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        listaRutas = new ArrayList<>();

        String selectQuery = "SELECT  " +
                Ruta.campo_id + "," +
                Ruta.campo_latitud + "," +
                Ruta.campo_longitud + "," +
                Ruta.campo_id_medicion +
                " FROM " + Ruta.tabla;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Ruta nuRuta;

        if (cursor.moveToFirst()) {
            do {
                nuRuta = new Ruta();
                nuRuta.setId(cursor.getLong(0));
                nuRuta.setLatitud(cursor.getDouble(1));
                nuRuta.setLongitud(cursor.getDouble(2));
                nuRuta.setId_medicion(cursor.getLong(3));
                listaRutas.add(nuRuta);
            } while (cursor.moveToNext());
            db.close();
        }
        return listaRutas;
    }

    public Ruta localizarRuta(long id) {
        for (Ruta nuRuta : listaRutas)
            if (nuRuta.getId() == id)
                return nuRuta;
        return null;
    }
}
