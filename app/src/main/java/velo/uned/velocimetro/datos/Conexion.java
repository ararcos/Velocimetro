package velo.uned.velocimetro.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import velo.uned.velocimetro.modelo.Medicion;
import velo.uned.velocimetro.modelo.Ruta;
import velo.uned.velocimetro.modelo.Users;

/**
 * Created by alexa on 13/03/2018.
 */

public class Conexion extends SQLiteOpenHelper {
    private static final String Nombre_Base = "base.bd";
    private static final int version =1;


    public Conexion(Context context) {
        super(context, Nombre_Base, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crear_tabla_persona = "CREATE TABLE " + Medicion.tabla  + "("
                + Medicion.campo_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Medicion.campo_distancia + " DOUBLE, "
                + Medicion.campo_fecha_Inicio + " DATE, "
                + Medicion.campo_fecha_Fin + " DOUBLE, "
                + Medicion.campo_velocidad + " DOUBLE )";
        String crear_tabla_ruta = "CREATE TABLE " + Ruta.tabla  + "("
                + Ruta.campo_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Ruta.campo_latitud + " DOUBLE, "
                + Ruta.campo_longitud + " DOUBLE, "
                + Ruta.campo_id_medicion + " INTEGER )";
        String crear_tabla_users = "CREATE TABLE " + Users.tabla  + "("
                + Users.campo_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Users.campo_usuario + " TEXT, "
                + Users.campo_contraseña + " TEXT )";
        db.execSQL(crear_tabla_persona);
        db.execSQL(crear_tabla_ruta);
        db.execSQL(crear_tabla_users);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS medicion");
        db.execSQL("DROP TABLE IF EXISTS ruta");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
}
