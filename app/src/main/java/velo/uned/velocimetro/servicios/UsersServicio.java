package velo.uned.velocimetro.servicios;

import android.content.Context;

import velo.uned.velocimetro.dao.UsersDAO;
import velo.uned.velocimetro.modelo.Users;

/**
 * Created by alexa on 20/03/2018.
 */

public class UsersServicio {
    private UsersDAO usersDAO;

    public UsersServicio(Context context) {
        usersDAO = new UsersDAO(context);
    }

    public boolean addUser(Users users) {

        return usersDAO.insertar(users);
    }

    public boolean updateUser(Users users) {

        return usersDAO.alterar(users);
    }

    public boolean deleteUser(Users users) {

        return usersDAO.borrar(users);
    }

    public boolean getUser(Users users) {
        return usersDAO.getUser(users);
    }
    public boolean getUserO(Users users) {
        return usersDAO.getUserO(users);
    }
}
