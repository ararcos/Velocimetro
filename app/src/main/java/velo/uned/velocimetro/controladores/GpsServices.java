package velo.uned.velocimetro.controladores;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import java.util.Locale;

import velo.uned.velocimetro.main.ActividadPrincipal;
import velo.uned.velocimetro.R;
import velo.uned.velocimetro.modelo.Medicion;
import velo.uned.velocimetro.modelo.Ruta;
import velo.uned.velocimetro.servicios.RutaServicios;

public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {


    //Definición para extraer la configuración
    RutaServicios rutaServicios;
    Ruta ruta;
    private SharedPreferences sharedPreferences;
    Location previousLocation = null;
    float previousTime = 0;
    float velocity = 0;


    private LocationManager mLocationManager;

    Location lastlocation = new Location("last");
    Medicion data;

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;

    PendingIntent contentIntent;
    //Se crea el servicio
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {

        Intent notificationIntent = new Intent(this, ActividadPrincipal.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        updateNotification(false);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        mLocationManager.addGpsStatusListener(this);
        rutaServicios=new RutaServicios(this);
        ruta= new Ruta();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }
    //Evento de cambio de ubicación
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onLocationChanged(Location location) {
        data = ActividadPrincipal.getData();
        if (data.isEnEjecucion()){
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();
            ruta.setLongitud(currentLon);
            ruta.setLatitud(currentLat);
            rutaServicios.addRuta(ruta);
            ruta=new Ruta();
            if (data.isFirstTime()){
                lastLat = currentLat;
                lastLon = currentLon;
                data.setFirstTime(false);
            }

            lastlocation.setLatitude(lastLat);
            lastlocation.setLongitude(lastLon);
            double distance = lastlocation.distanceTo(location);

            if (location.getAccuracy() < distance){
                data.addDistancia(distance);

                lastLat = currentLat;
                lastLon = currentLon;
            }

            if (location.hasSpeed()) {
                //data.setVelocidadActual(location.getSpeed());
                //Actualiza la velocidad
                data.setVelocidad(location.getSpeed() * 3.6);
                String speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) + "km/h";
                SpannableString s = new SpannableString(speed);
                s.setSpan(new RelativeSizeSpan(0.25f), s.length()-4, s.length(), 0);

                data.setVelocidadActualString(speed);
                speed = String.format(Locale.ENGLISH, "%.0f", data.getVelocidadMaxima()) + "km/h";
                s = new SpannableString(speed);
                s.setSpan(new RelativeSizeSpan(0.25f), s.length()-4, s.length(), 0);
                data.setVelocidadMaximaString(s.toString());
                s = new SpannableString(String.format("%.2f", data.getDistancia()) + "m");
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 2, s.length(), 0);
                //Actualiza la distancia

                data.setDistanciaString(s.toString());
                //data.setOtraVelocidad(otraVelocidad(location));
                if(location.getSpeed() == 0){
                    new isStillStopped().execute();
                }
            }
            //data.update();
            updateNotification(true);
        }
    }

    //Otra forma de calcular la velocidad - No se utiliza
    private double otraVelocidad(Location loc){
        boolean hasPrevious = true;
        if (previousLocation == null || previousTime == 0) {
            hasPrevious = false;
        }
        float currentTime = System.currentTimeMillis();
        if (hasPrevious) {
            float timeElapsed = (currentTime - previousTime)/1000;
            velocity = loc.distanceTo(previousLocation)/timeElapsed;
            //data.setMensaje(new Double(velocity).toString());
        }
        storeToPrevious(loc, currentTime);

        return velocity;
    }
    //Complemento de otra forma de calcular la velocidad
    private void storeToPrevious(Location l, float time) {
        previousLocation = new Location(l);
        previousTime = time;
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void updateNotification(boolean asData){
        Notification.Builder builder = new Notification.Builder(getBaseContext())
                .setContentTitle(getString(R.string.running))
                //.setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(contentIntent);

        if(asData){
            builder.setContentText(String.format(getString(R.string.notification), String.valueOf(data.getVelocidadMaxima()), String.valueOf(data.getDistancia())));
        }else{
            builder.setContentText(String.format(getString(R.string.notification), '-', '-'));
        }
        Notification notification = builder.build();
        startForeground(R.string.noti_id, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }   
       
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
   
    /* Remove the locationlistener updates when Services is stopped */
    @Override
    public void onDestroy() {
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        stopForeground(true);
    }

    @Override
    public void onGpsStatusChanged(int event) {}

    @Override
    public void onProviderDisabled(String provider) {}
   
    @Override
    public void onProviderEnabled(String provider) {}
   
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    class isStillStopped extends AsyncTask<Void, Integer, String> {
        int timer = 0;
        @Override
        protected String doInBackground(Void... unused) {
            try {
                while (data.getVelocidadActual() == 0) {
                    Thread.sleep(1000);
                    timer++;
                }
            } catch (InterruptedException t) {
                return ("The sleep operation failed");
            }
            return ("return object when task is finished");
        }

        @Override
        protected void onPostExecute(String message) {
            data.setFechafin(timer);
        }
    }
}
