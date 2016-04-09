package com.example.carolinapinzon.micampusuniandes;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.AsyncTask;
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
import android.content.Intent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    private MediaRecorder mRecorder = null;
    private double ruido;
    private int lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ruido = 0;
        new TareaAudio().execute();
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

    //JSON ejemplo: {"dia":"6","hora":"12","ruido":"78","lugar":"3"}

    private class TareaRed extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                String url = "http://157.253.205.30/api/registroAdd";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject objetoJSON = new JSONObject();
                System.out.println("Objeto a mandar:");

                objetoJSON.put("dia", "8");
                objetoJSON.put("hora", "13");
                int ruidoInt = (int)ruido;
                objetoJSON.put("ruido", ""+ruidoInt);
                objetoJSON.put("lugar", "3");

                System.out.println(objetoJSON);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(objetoJSON.toString());
                wr.flush();

//display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                System.out.println("Respuesta:");
                System.out.println(HttpResult);
                System.out.println("Respuesta esperada:");
                System.out.println(HttpURLConnection.HTTP_OK);
                if (HttpResult == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    br.close();

                    System.out.println("- - - - - - - - - - - - - - - - - -");
                    System.out.println("" + sb.toString());
                    System.out.println("- - - - - - - - - - - - - - - - - -");

                } else {
                    System.out.println(con.getResponseMessage());
                }
            }
            catch(Exception e)
            {
                System.out.println(e.fillInStackTrace());
            }
            return 0L;
        }

        protected void onPostExecute(Long result) {
            System.out.println("Downloaded " + result + " bytes");
        }
    }

    private class TareaAudio extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            System.out.println("Inicia grabacion");
            start();
            stop();
            new TareaRed().execute();
            return 0L;
        }

        protected void onPostExecute(Long result) {
            System.out.println("Ruido " + ruido + " dB");
        }
    }

    public void start() {
        try{
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                mRecorder.prepare();
                mRecorder.start();
                ruido = 7.363 * Math.log(darRuido()) + 4.69;
            }
        }catch(Exception e){}

    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

    public double darRuido()
    {
        try {
            double ruido = 0;
            double ampMax;
            ampMax = getAmplitude();
            for (int i = 0; i < 4; i++) {
                System.out.println("Grabando...");
                Thread.sleep(1000);
                ampMax = getAmplitude();
                ruido = (ruido*i+ampMax)/(i+1);
            }
            return ruido;
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    public void enviarRegistro() {

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
