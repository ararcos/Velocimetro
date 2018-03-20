package velo.uned.velocimetro.modelo;

/**
 * Created by alexa on 19/03/2018.
 */

public class Ruta {
    public static final String tabla="ruta";
    public static final String campo_id="id_rut";
    public static final String campo_latitud="latitud_rut";
    public static final String campo_longitud="longitud_rut";
    public static final String campo_id_medicion="id_med";


    private Long id;
    private Double latitud;
    private Double longitud;
    private Long id_medicion;



    public Ruta() {

    }
    public Long getId_medicion() {
        return id_medicion;
    }

    public void setId_medicion(Long id_medicion) {
        this.id_medicion = id_medicion;
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
