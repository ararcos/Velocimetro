package velo.uned.velocimetro.controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

import velo.uned.velocimetro.modelo.Medicion;
import velo.uned.velocimetro.servicios.MedicionServicio;
import velo.uned.velocimetro.R;
/**
 * Created by alexa on 22/03/2018.
 */

public class AdaptadorListMedicion extends BaseAdapter {
    LayoutInflater layoutInflater;
    MedicionServicio medicionServicio;
    ArrayList<Medicion> listmedicion;

    public AdaptadorListMedicion(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        medicionServicio = new MedicionServicio(context);
        listmedicion=medicionServicio.getallMedicion();
    }
    @Override
    public int getCount() {
        return listmedicion.size();
    }

    @Override
    public Object getItem(int position) {
        return listmedicion.get(position);
    }

    @Override
    public long getItemId(int position) {
        Medicion unaMedicion = listmedicion.get(position);
        return unaMedicion.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_list_mediciones, null);
        }

        TextView tvDistancia = convertView.findViewById(R.id.tvdistancia);
        TextView tvFechaIni = convertView.findViewById(R.id.tvFechaini);
        TextView tvFechaFin = convertView.findViewById(R.id.tvFechafin);
        TextView tvvelocidad = convertView.findViewById(R.id.tvvelocidad);


        Medicion unaMedicion = listmedicion.get(position);

        tvDistancia.setText(String.valueOf(unaMedicion.getDistancia()));
        tvFechaIni.setText(String.valueOf(unaMedicion.getFechaInicio()));
        tvFechaFin.setText(String.valueOf(unaMedicion.getFechafin()));
        tvvelocidad.setText(String.valueOf(unaMedicion.getVelocidadMaxima()));
        return convertView;
    }
}
