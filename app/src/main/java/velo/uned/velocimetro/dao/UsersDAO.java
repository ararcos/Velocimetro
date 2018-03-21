package velo.uned.velocimetro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import velo.uned.velocimetro.datos.Conexion;
import velo.uned.velocimetro.modelo.Users;

/**
 * Created by alexa on 20/03/2018.
 */

public class UsersDAO {
    Context context;
    Conexion dbsqLite;
    public static ArrayList<Users> listaUsers;

    public UsersDAO(Context context) {
        this.context = context;
        dbsqLite = new Conexion(context);
    }

    public boolean insertar(Users user) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user.campo_usuario, user.getUser());
        values.put(user.campo_contraseña, user.getPass());
        Long id = db.insert(user.tabla, null, values);
        db.close();
        if (id > 0) {
            user.setId(id);
        }
        return id > 0;
    }
    public boolean alterar(Users user) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(user.campo_usuario, user.getUser());
        values.put(user.campo_contraseña, user.getPass());
        String where = user.campo_id + " = ?";

        int id = db.update(user.tabla, values, where, new String[]{String.valueOf(user.getId())});
        db.close();
        return id > 0;
    }


    public boolean borrar(Users user ) {
        SQLiteDatabase db = dbsqLite.getWritableDatabase();
        String where = user.campo_id + " = ?";
        int ret = db.delete(user.tabla, where, new String[]{String.valueOf(user.getId())});
        db.close();
        return ret > 0;
    }
    public Boolean getUser(Users user){
        SQLiteDatabase db =dbsqLite.getReadableDatabase();
        String [] campos={user.campo_usuario,user.campo_contraseña};
        String [] parametro={user.getUser().toString(),user.getPass().toString()};
        String where = user.campo_usuario + " = ?"+" AND "+user.campo_contraseña+" = ?";
        Cursor cursor = db.query(user.tabla,campos,where,parametro,null,null,null);
        return cursor!=null;
    }
    public  boolean getUserO(Users users){
        SQLiteDatabase db = dbsqLite.getReadableDatabase();
        Users nuUser=new Users();
        String selectQuery = "SELECT  " +
                users.campo_id + "," +
                users.campo_usuario + "," +
                users.campo_contraseña +
                " FROM " + users.tabla +
                " WHERE " + users.campo_usuario +
                "='" + users.getUser() +
                "' AND " + users.campo_contraseña +
                "='" + users.getPass();

        Cursor cursor = db.rawQuery(selectQuery, null);
        db.close();
        return cursor.moveToFirst();
    }
}
