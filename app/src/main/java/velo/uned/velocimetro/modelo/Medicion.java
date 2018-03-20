package velo.uned.velocimetro.modelo;


/**
 * Created by Alvaro on 7/2/2018.
 */

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import velo.uned.velocimetro.BR;

public class Medicion extends BaseObservable implements Parcelable {
    public static final String tabla="medicion";
    public static final String campo_id="id_med";
    public static final String campo_fecha_Inicio="fechaInicio_med";
    public static final String campo_fecha_Fin="fechaFin_med";
    public static final String campo_distancia="distancia_med";
    public static final String campo_velocidad="velocidad_med";

    private Long id;
    private ArrayList<Ruta> rutalist;
    @Bindable
    private Date  fechaInicio;
    @Bindable
    private double fechafin;
    @Bindable
    private String observacion;
    @Bindable
    private double distancia;
    private String distanciaString;
    @Bindable
    private double velocidadActual;

    //Enlace con la interfaz
    @Bindable
    private String velocidadActualString;

    @Bindable
    private double velocidadMaxima;
    //Enlace con la interfaz
    @Bindable
    private String velocidadMaximaString;
    //Se esta ejecutando una medición
    private boolean enEjecucion;
    //El dispositivo esta recibiendo señal de satélites
    private boolean listoParaEjecutarse;
    //Para uso del cronometro
    private long time;
    //Satelites conectados
    @Bindable
    private String satelites;
    //Estado informativo de la conexión "Esperando por un GPS..."
    private String estado;
    //Bandas de velocidad, extraidas de archivo de preferencias
    private List<RangoVelocidad> rangos;
    //Rango de velocidad actual
    private RangoVelocidad rangoActual;

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    private boolean isFirstTime;

    public ArrayList<Ruta> getRutalist() {
        return rutalist;
    }

    public void setRutalist(ArrayList<Ruta> rutalist) {
        this.rutalist = rutalist;
    }

    public void setId(Long id){this.id=id;}
public Long getId(){return this.id;}
    //private onGpsServiceUpdate onGpsServiceUpdate;

    //public interface onGpsServiceUpdate{
    //    public void update();
    //}
    public Medicion(){
        this.enEjecucion=false;
        this.listoParaEjecutarse=false;



        rangos=new ArrayList<RangoVelocidad>();
        RangoVelocidad rango = new RangoVelocidad(1,0,0.99, "PARADO");

    rangos.add(rango);
    this.rangoActual=rangos.get(0);
    }
    public void update(){
        //onGpsServiceUpdate.update();
    }
    //Algoritmo para determinar la banda actual
    private void  calcularBanda(double velocidad){
        for(RangoVelocidad rango:this.rangos){
            if (velocidad>= rango.getVelocidadMinima() && velocidad <=rango.getVelocidadMaxima()) {
                this.rangoActual = rango;
                notifyPropertyChanged(BR.rangoActual);
            }
        }


    }

    //public void setOnGpsServiceUpdate(onGpsServiceUpdate onGpsServiceUpdate){
        //this.onGpsServiceUpdate = onGpsServiceUpdate;
    //}


    //public Medicion() {
    //    this.observacion="Hello mundo2";
    //}

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    @Bindable
    public String getObservacion() {
        return observacion;
    }

    @Bindable
    public boolean isEnEjecucion() {
        return enEjecucion;
    }



    public void setEnEjecucion(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
        notifyPropertyChanged(BR.enEjecucion);
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
        notifyPropertyChanged(BR.observacion);
    }

    protected Medicion(Parcel in) {
        long tmpFechaInicio = in.readLong();
        fechaInicio = tmpFechaInicio != -1 ? new Date(tmpFechaInicio) : null;
        long tmpFechafin = in.readLong();

        observacion = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(fechaInicio != null ? fechaInicio.getTime() : -1L);

        parcel.writeString(observacion);
    }



    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Medicion> CREATOR = new Parcelable.Creator<Medicion>() {
        @Override
        public Medicion createFromParcel(Parcel in) {
            return new Medicion(in);
        }

        @Override
        public Medicion[] newArray(int size) {
            return new Medicion[size];
        }
    };

    public double getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(double velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    //public double getMovimientoPromedio() {
    //    return movimientoPromedio;
    //}

    //public void setMovimientoPromedio(double movimientoPromedio) {
    //    this.movimientoPromedio = movimientoPromedio;
    //}

    //public double getVelocidadPromedio() {
    //    return velocidadPromedio;
    //}

    //public void setVelocidadPromedio(double velocidadPromedio) {
    //    this.velocidadPromedio = velocidadPromedio;
    //}

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public void addDistancia(double distance){
        distancia = distancia + distance;
    }

    public double getDistance(){
        return distancia;
    }

    @Bindable
    public double getVelocidadActual() {
        return velocidadActual;
    }



    public double getFechafin() {
        return fechafin;
    }

    public void setFechafin(double fechafin) {
        this.fechafin = fechafin;
    }




    public void setVelocidad(double velocidadActual) {
        this.velocidadActual = velocidadActual;
        this.calcularBanda(this.velocidadActual);
        //Determinar la velocidad maxima
        if (velocidadActual > velocidadMaxima){
            velocidadMaxima = velocidadActual;
        }
        //notifyPropertyChanged(BR.velocidadActual);
    }
    @Bindable
    public String getSatelites() {
        return satelites;
    }

    public void setSatelites(String satelites) {
        this.satelites = satelites;
        notifyPropertyChanged(BR.satelites);
    }
    @Bindable
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        notifyPropertyChanged(BR.estado);
     }
    @Bindable
    public String getVelocidadActualString() {
        return velocidadActualString;
    }

    public void setVelocidadActualString(String velocidadActualString) {
        this.velocidadActualString = velocidadActualString;
        notifyPropertyChanged(BR.velocidadActualString);
    }
    @Bindable
    public String getDistanciaString() {
        return distanciaString;
    }

    public void setDistanciaString(String distanciaString) {
        this.distanciaString = distanciaString;
        notifyPropertyChanged(BR.distanciaString);
    }
    @Bindable
    public RangoVelocidad getRangoActual() {
        return rangoActual;
    }

    public void setRangoActual(RangoVelocidad rangoActual) {
        this.rangoActual = rangoActual;
    }

    //@Bindable
    //public String getMensaje() {
//        return mensaje;
  //  }

    //public void setMensaje(String mensaje) {
    //    this.mensaje = mensaje;
    //    notifyPropertyChanged(BR.mensaje);
    //}
    @Bindable
    public String getVelocidadMaximaString() {
        return velocidadMaximaString;
    }

    public void setVelocidadMaximaString(String velocidadMaximaString) {
        this.velocidadMaximaString = velocidadMaximaString;
        notifyPropertyChanged(BR.velocidadMaximaString);
    }
    @Bindable
    public boolean isListoParaEjecutarse() {
        return listoParaEjecutarse;
    }

    public void setListoParaEjecutarse(boolean listoParaEjecutarse) {
        this.listoParaEjecutarse = listoParaEjecutarse;
        notifyPropertyChanged(BR.listoParaEjecutarse);
    }

    public List<RangoVelocidad> getRangos() {
        return rangos;
    }

    public void setRangos(List<RangoVelocidad> rangos) {
        this.rangos = rangos;
    }
}