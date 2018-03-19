package velo.uned.velocimetro.modelo;

/**
 * Created by alexa on 19/03/2018.
 */

public class Ruta {
    public static final String tabla="ruta";
    public static final String campo_id="id_rut";
    public static final String campo_latitud="latitud_rut";
    public static final String campo_longitud="longitud_rut";


    private Long id;
    private Double latitud;
    private Double longitud;

    public Ruta() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
