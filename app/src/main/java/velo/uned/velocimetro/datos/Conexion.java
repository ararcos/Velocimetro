package velo.uned.velocimetro.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import velo.uned.velocimetro.modelo.Medicion;

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
        db.execSQL(crear_tabla_persona);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS medicion");
        db.execSQL("DROP TABLE IF EXISTS ruta");
        onCreate(db);
    }
}
