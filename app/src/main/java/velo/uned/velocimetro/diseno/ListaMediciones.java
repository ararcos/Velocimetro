package velo.uned.velocimetro.diseno;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import velo.uned.velocimetro.R;
import velo.uned.velocimetro.controladores.AdaptadorListMedicion;

public class ListaMediciones extends AppCompatActivity {
    AdaptadorListMedicion adaptadorListMedicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mediciones);
        ListView listView = (ListView) findViewById(R.id.listview);
        adaptadorListMedicion = new AdaptadorListMedicion(this);
        listView.setAdapter(adaptadorListMedicion);
        }
    @Override
    public void onResume(){
        super.onResume();
        adaptadorListMedicion.notifyDataSetChanged();
    }
}
