package com.example.carolinapinzon.micampusuniandes;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateSuferenciasDia();
        populateListView();
        registerClickCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateSuferenciasDia() {
        Instancia instancia = Instancia.darInstancia();

        instancia.agregarSugerencia(new SugerenciaDia("ml", 50, 8, R.drawable.ml));
        instancia.agregarSugerencia(new SugerenciaDia("ml", 45, 9, R.drawable.sd));
        instancia.agregarSugerencia(new SugerenciaDia("ml", 70, 10, R.drawable.w));
    }

    public void populateListView() {
        Instancia instancia = Instancia.darInstancia();
        ArrayAdapter<SugerenciaDia> adapter = new MyListAdapter(instancia);
        ListView list = (ListView) findViewById(R.id.sugerenciasListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.sugerenciasListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                        int position, long id) {

                    Intent intent = new Intent(MainActivity.this,OtrasRecomendaciones.class);
                    intent.putExtra("position", ""+position);
                    startActivity(intent);
                }
        });
    }

    public class MyListAdapter extends ArrayAdapter<SugerenciaDia> {

        public MyListAdapter(Instancia instancia) {
            super(MainActivity.this,R.layout.sugerencia_view,instancia.getSugerencias());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.sugerencia_view, parent, false);
            }

            Instancia instancia = Instancia.darInstancia();
            //Encontrar Sugerencia dia
            SugerenciaDia sugerenciaActual = instancia.darSugerencia(position);

            //Llenar la vista
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imgEdificio);
            imageView.setImageResource(sugerenciaActual.getIconId());

            TextView textEdificio = (TextView) itemView.findViewById(R.id.txtEdificio);
            textEdificio.setText(sugerenciaActual.getEdificio().toUpperCase());

            TextView textHora = (TextView) itemView.findViewById(R.id.txtHora);
            textHora.setText(sugerenciaActual.getHora() + ":00");

            int ruido = sugerenciaActual.getRuido();
            String nivel = "";
            if (ruido < 30) {
                nivel = "Muy Bajo";
            }
            else if (ruido < 50) {
                nivel = "Bajo";
            }
            else if (ruido < 70) {
                nivel = "Medio";
            }
            else if (ruido < 90) {
                nivel = "Alto";
            }
            else {
                nivel = "Muy Alto";
            }
            TextView textSonido = (TextView) itemView.findViewById(R.id.txtSonido);
            textSonido.setText("Sonido: " + ruido + "dBA - " + nivel);

            return itemView;
        }
    }
}
