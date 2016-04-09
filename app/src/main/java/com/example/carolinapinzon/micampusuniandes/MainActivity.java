package com.example.carolinapinzon.micampusuniandes;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Calendar;


public class MainActivity extends Activity {
    private MediaRecorder mRecorder = null;
    private double ruido;
    private int lugar;
    private SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ruido = 0;
        lugar = 98;
        new TareaAudio().execute();
        new Solicitar().execute();
        mydatabase = openOrCreateDatabase("micampus", MODE_PRIVATE, null);
        //mydatabase.execSQL("DROP TABLE Registros;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Registros(Hora INT,Dia INT,Lugar INT,Ruido INT);");
        /* PRUEBA SQLITE
        SQLiteDatabase mydatabase = openOrCreateDatabase("micampus", MODE_PRIVATE, null);
        //mydatabase.execSQL("DROP TABLE Registros;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Registros(Hora INT,Dia INT,Lugar INT,Ruido INT);");
        mydatabase.execSQL("INSERT INTO Registros VALUES(15,7,99,"+(int)(Math.random()*80)+");");
        Cursor resultSet = mydatabase.rawQuery("Select * from Registros", null);
        resultSet.moveToFirst();
        if (resultSet.moveToFirst()){
            do{
                String hora = resultSet.getString(0);
                System.out.println("hora SQL: "+ hora);
                String dia = resultSet.getString(1);
                System.out.println("dia SQL: "+ dia);
                String lugar = resultSet.getString(2);
                System.out.println("lugar SQL: "+ lugar);
                String ruido = resultSet.getString(3);
                System.out.println("ruido SQL: "+ ruido);
                // do what ever you want here
            }while(resultSet.moveToNext());
        }
        resultSet.close();*/
        Cursor resultSet = mydatabase.rawQuery("Select Ruido, count(*) as cuenta from Registros where Hora = 17 group by ruido order by cuenta DESC;", null);
        resultSet.moveToFirst();
        if (resultSet.moveToFirst()){
            do{
                String ruido = resultSet.getString(0);
                System.out.println("Ruido SQL: "+ ruido);
                String cuenta = resultSet.getString(1);
                System.out.println("cuenta SQL: "+ cuenta);
                // do what ever you want here
            }while(resultSet.moveToNext());
        }
        resultSet.close();
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

    private class EnviarEstado extends AsyncTask<URL, Integer, Long> {
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
                Calendar c = Calendar.getInstance();
                int day_of_week = c.get(Calendar.DAY_OF_WEEK);
                System.out.println("int day_of_week = c.get(Calendar.DAY_OF_WEEK): - - - : " + day_of_week);
                objetoJSON.put("dia",""+day_of_week);
                int hora = c.get(Calendar.HOUR_OF_DAY);
                System.out.println("int hora = c.get(Calendar.HOUR_OF_DAY): - - - : " + hora);
                objetoJSON.put("hora",""+hora);
                int ruidoInt = (int)ruido;
                objetoJSON.put("ruido", ""+ruidoInt);
                objetoJSON.put("lugar", ""+lugar);
                System.out.println("JSON de ruido a mandar");
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

    private class Solicitar extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            llamarHTTP(7,15,70);
            llamarHTTP(7,16,60);
            llamarHTTP(7,17,60);
            return 0L;
        }

        private void llamarHTTP(int diaP, int horaP, int ruidoP)
        {

            try {
                String url = "http://157.253.205.30/api/darSugerencia";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject objetoJSON = new JSONObject();
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_WEEK);
                objetoJSON.put("dia",""+diaP);
                int hora = c.get(Calendar.HOUR_OF_DAY);
                objetoJSON.put("hora",""+horaP);
                int ruidoInt = (int)ruido;
                objetoJSON.put("ruido", ""+ruidoP);
                //objetoJSON.put("lugar", ""+lugar);

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

                    System.out.println("- - - - - - Respuesta del servidor - - - - - -");
                    System.out.println("" + sb.toString());
                    System.out.println("- - - - - - Respuesta del servidor - - - - - -");

                } else {
                    System.out.println(con.getResponseMessage());
                }
            }
            catch(Exception e)
            {
                System.out.println(e.fillInStackTrace());
            }
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
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_WEEK);
            int hora = c.get(Calendar.HOUR_OF_DAY);
            mydatabase.execSQL("INSERT INTO Registros VALUES("+ hora +","+ day +","+ lugar +"," + ((int)ruido/10)*10 + ");");
            new EnviarEstado().execute();
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
