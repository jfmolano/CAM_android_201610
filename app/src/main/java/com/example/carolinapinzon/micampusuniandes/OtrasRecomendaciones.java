package com.example.carolinapinzon.micampusuniandes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class OtrasRecomendaciones extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otras_recomendaciones);

        Intent i = getIntent();
        System.out.println("Intent: ");
        System.out.println(i);
        System.out.println("i.getStringExtra(\"position\"): ");
        System.out.println(i.getStringExtra("position"));
        int index = Integer.parseInt(i.getStringExtra("position"));

        Instancia instancia = Instancia.darInstancia();
        SugerenciaDia s = instancia.darSugerencia(index);
        if (s != null) {
            TextView txtOpcion1 = (TextView)findViewById(R.id.txtOpcion1);
            txtOpcion1.setText(s.getEdificio());

            TextView txtSonido1 = (TextView)findViewById(R.id.txtSonido1);
            txtSonido1.setText(""+s.getRuido());

            ImageView imgOpcion1 = (ImageView)findViewById(R.id.imgOpcion1);
            imgOpcion1.setImageResource(s.getIconId());

            //Sug1

            TextView txtOpcion2 = (TextView)findViewById(R.id.txtOpcion2);
            txtOpcion2.setText(s.getSug1().getEdificio());

            TextView txtSonido2 = (TextView)findViewById(R.id.txtSonido2);
            txtSonido2.setText(""+s.getSug1().getRuido());

            ImageView imgOpcion2 = (ImageView)findViewById(R.id.imgOpcion2);
            imgOpcion2.setImageResource(s.getSug1().getIconId());

            //Sug2

            TextView txtOpcion3 = (TextView)findViewById(R.id.txtOpcion3);
            txtOpcion3.setText(s.getSug2().getEdificio());

            TextView txtSonido3 = (TextView)findViewById(R.id.txtSonido3);
            txtSonido3.setText(""+s.getSug2().getRuido());

            ImageView imgOpcion3 = (ImageView)findViewById(R.id.imgOpcion3);
            imgOpcion3.setImageResource(s.getSug2().getIconId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_otras_recomendaciones, menu);
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
}
