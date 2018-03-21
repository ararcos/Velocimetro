package velo.uned.velocimetro.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import velo.uned.velocimetro.R;
import velo.uned.velocimetro.SettingsActivity;
import velo.uned.velocimetro.controladores.GpsServices;
import velo.uned.velocimetro.databinding.ActivityActividadPrincipalBinding;
import velo.uned.velocimetro.modelo.Medicion;
import velo.uned.velocimetro.modelo.RangoVelocidad;
import velo.uned.velocimetro.servicios.MedicionServicio;
import velo.uned.velocimetro.servicios.RutaServicios;

public class ActividadPrincipal extends  AppCompatActivity  implements LocationListener, GpsStatus.Listener {
    MedicionServicio medicionServicio;
    RutaServicios rutaServicios;
    private String accuracy;
    private boolean firstfix;
    //private String status;



    private SharedPreferences sharedPreferences;
    //private Medicion.onGpsServiceUpdate onGpsServiceUpdate;
    private static Medicion data;
    //private String currentSpeed;
    private String maxSpeed="";
    private String averageSpeed;
    //private String distance;
    private Chronometer time;
    private LocationManager mLocationManager;
    private String satellite;

    //Crea el link a las preferencias
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mostrarSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Crea las preferencias
    private void mostrarSettings(){
        Intent intentSettings =new Intent(this,SettingsActivity.class);
        startActivity(intentSettings);
    }

    //Crea el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;


    }

    //Crea la actividad principal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        medicionServicio=new MedicionServicio(this);
        rutaServicios=new RutaServicios(this);
        //setContentView(R.layout.activity_actividad_principal);

        //Toolbar toolBar = (Toolbar)findViewById(R.id.toolBar);
        //setSupportActionBar(toolBar);




        data = new Medicion();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());




        ///Overiide update



        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        time = (Chronometer) findViewById(R.id.time);

        time.setText("00:00:00");
        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;

            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if (data.isEnEjecucion()) {
                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    data.setTime(time);
                } else {
                    time = data.getTime();
                }

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chrono.setText(hh + ":" + mm + ":" + ss);

                if (data.isEnEjecucion()) {
                    chrono.setText(hh + ":" + mm + ":" + ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh + ":" + mm + ":" + ss);
                    } else {
                        isPair = true;
                        chrono.setText("");
                    }
                }

            }
        });

        ActivityActividadPrincipalBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_actividad_principal);
        //med = new Medicion();
        binding.setDbMedicion(data);
        data.setSatelites("prueba");
    }

    //Indentifica que el gps tenga satelites
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        firstfix = true;
        if (!data.isEnEjecucion()){
            Gson gson = new Gson();
            String json = sharedPreferences.getString("data", "");
            //data = gson.fromJson(json, Medicion.class);
        }
        if (data == null){
            data = new Medicion();
        }else{
            //data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }

        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        200);
            }



            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //showGpsDisabledDialog();
        }

        mLocationManager.addGpsStatusListener(this);
    }

    //Iniciar una medición
    public void iniciarMedicion(View view) {

        if (!data.isEnEjecucion()) {

            List<RangoVelocidad> rangos =new ArrayList<RangoVelocidad>();
            double valorIni = (double)sharedPreferences.getInt("estado_parado", 0);
            double valorFin = valorIni-0.01;
            RangoVelocidad rango = new RangoVelocidad(1,0,valorFin, "PARADO");
            valorIni = (double)sharedPreferences.getInt("estado_parado", 0);
            valorFin = (double)sharedPreferences.getInt("estado_caminando", 0);
            //RangoVelocidad rango1 = new RangoVelocidad(2,1,3.99, "CAMINANDO");
            RangoVelocidad rango1 = new RangoVelocidad(2,valorIni,valorFin-0.000001, "CAMINANDO");
            valorIni = (double)sharedPreferences.getInt("estado_caminando", 0);
            valorFin = (double)sharedPreferences.getInt("estado_marchando", 0);
            //RangoVelocidad rango2 = new RangoVelocidad(3,4,5.99, "MARCHANDO");
            RangoVelocidad rango2 = new RangoVelocidad(3,valorIni,valorFin-0.000001, "MARCHANDO");
            valorIni = (double)sharedPreferences.getInt("estado_marchando", 0);
            valorFin = (double)sharedPreferences.getInt("estado_corriendo", 0);
            //RangoVelocidad rango3 = new RangoVelocidad(4,6,11.99, "CORRIENDO");

            RangoVelocidad rango3 = new RangoVelocidad(4,valorIni,valorFin-0.000001, "CORRIENDO");

            valorIni = (double)sharedPreferences.getInt("estado_corriendo", 0);
            valorFin = (double)sharedPreferences.getInt("estado_sprint", 0);

            RangoVelocidad rango4 = new RangoVelocidad(5,valorIni,valorFin-0.000001, "SPRINT");
            valorIni = (double)sharedPreferences.getInt("estado_sprint", 0);
            valorFin = (double)sharedPreferences.getInt("estado_motor_terrestre", 0);
            //RangoVelocidad rango5 = new RangoVelocidad(5,25,169.99, "ESTADO VEH. MOTOR TERRESTRE");
            RangoVelocidad rango5 = new RangoVelocidad(5,valorIni,valorFin-0.000001, "ESTADO VEH. MOTOR TERRESTRE");
            valorIni = (double)sharedPreferences.getInt("estado_aereo", 0);
            valorFin = 700;
            //RangoVelocidad rango6 = new RangoVelocidad(5,1i0,700, "ESTADO VEH. MOTOR AÉREO");
            RangoVelocidad rango6 = new RangoVelocidad(5,valorIni,valorFin, "ESTADO VEH. MOTOR AÉREO");

            //fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
            rangos.add(rango);
            rangos.add(rango1);
            rangos.add(rango2);
            rangos.add(rango3);
            rangos.add(rango4);
            rangos.add(rango5);
            rangos.add(rango6);
            data.setRangos(rangos);
            data.setVelocidad(0);
            data.setVelocidadMaxima(0);
            data.setDistancia(0);
            data.setEnEjecucion(true);
            time.setBase(SystemClock.elapsedRealtime() - data.getTime());
            time.start();
            data.setFirstTime(true);
            startService(new Intent(getBaseContext(), GpsServices.class));
            //refresh.setVisibility(View.INVISIBLE);
        }
        //else {
            //fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
        //    data.setEnEjecucion(false);
        //    data.setEstado("");
        //    stopService(new Intent(getBaseContext(), GpsServices.class));
        //  refresh.setVisibility(View.VISIBLE);
        //}
    }
    //Detener una medición
    public void detenerMedicion(View view){
        if (medicionServicio.addMedicion(data)) {
            if(rutaServicios.addAllRuta(data.getRutalist(),data.getId())) {
                Toast.makeText(this, "Guardado Correctamente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ocurrio Un error al guardar!", Toast.LENGTH_SHORT).show();
        }
        data.setVelocidadMaxima(0);
        data.setDistancia(0);
        data.setVelocidad(0);
        data.setEnEjecucion(false);
        data.setEstado("");
        stopService(new Intent(getBaseContext(), GpsServices.class));
    }
    //Evento que localiza el cambio de el estado del GPS
    @Override
    public void onGpsStatusChanged(int i) {
        switch (i) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

                @SuppressLint("MissingPermission") GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                int satsInView = 0;
                int satsUsed = 0;
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                for (GpsSatellite sat : sats) {
                    satsInView++;
                    if (sat.usedInFix()) {
                        satsUsed++;
                    }
                }
                satellite = (String.valueOf(satsUsed) + "/" + String.valueOf(satsInView));
                data.setSatelites(satellite);
                if (satsUsed == 0) {
                    //fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                    data.setEnEjecucion(false);
                    //data.setEstado("");
                    //stopService(new Intent(getBaseContext(), GpsServices.class));
                    //accuracy =("");
                    data.setEstado(getResources().getString(R.string.waiting_for_fix));

                    firstfix = true;
                }
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Corregir
                    //showGpsDisabledDialog();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }
    //Evento que indentifica un cambio de ubicación
    @Override
    public void onLocationChanged(Location location) {
        if (location.hasAccuracy()) {

            if (firstfix){
                data.setListoParaEjecutarse(true);


                firstfix = false;
            }
        }else{
            firstfix = true;
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static Medicion getData() {
        return data;
    }

    public static void setData(Medicion data) {
        ActividadPrincipal.data = data;
    }

    public void resetData(){
        //fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
        //refresh.setVisibility(View.INVISIBLE);
        time.stop();
        maxSpeed="";
        averageSpeed="";
        data.setDistanciaString("");
        time.setText("00:00:00");
        data = new Medicion();
    }
}
