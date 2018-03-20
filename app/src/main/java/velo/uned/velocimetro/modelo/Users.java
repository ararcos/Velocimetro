package velo.uned.velocimetro.modelo;

/**
 * Created by alexa on 20/03/2018.
 */

public class Users {
    public static final String tabla="usuarios";
    public static final String campo_id="id_rut";
    public static final String campo_contraseña="latitud_rut";
    public static final String campo_usuario="longitud_rut";

    private Long id;
    private String user;
    private String pass;
    public Users() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
