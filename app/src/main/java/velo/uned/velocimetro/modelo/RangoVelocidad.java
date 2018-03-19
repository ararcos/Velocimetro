package velo.uned.velocimetro.modelo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import velo.uned.velocimetro.BR;

/**
 * Created by Alvaro on 8/2/2018.
 */

public class RangoVelocidad extends BaseObservable {
    private int codigo;
    private double velocidadMinima;
    private double velocidadMaxima;
    private String descripcion;
    public RangoVelocidad(){

    }
    public RangoVelocidad(int codigo, double minimo, double maximo, String descripcion){
        this.codigo=codigo;
        this.velocidadMinima=minimo;
        this.velocidadMaxima=maximo;
        this.descripcion=descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public double getVelocidadMinima() {
        return velocidadMinima;
    }

    public void setVelocidadMinima(double velocidadMinima) {
        this.velocidadMinima = velocidadMinima;
    }

    public double getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(double velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }
    @Bindable
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        notifyPropertyChanged(BR.distanciaString);
    }
}
