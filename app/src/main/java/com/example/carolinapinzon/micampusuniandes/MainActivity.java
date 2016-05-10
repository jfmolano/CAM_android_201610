package com.example.carolinapinzon.micampusuniandes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends Activity {

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("0:10", new ArrayList<String>() {{
            add("0");
        }});
        placesByBeacons.put("1:10", new ArrayList<String>() {{
            add("1");
        }});
        placesByBeacons.put("2:10", new ArrayList<String>() {{
            add("2");
        }});
        placesByBeacons.put("3:10", new ArrayList<String>() {{
            add("3");
        }});
        placesByBeacons.put("4:10", new ArrayList<String>() {{
            add("4");
        }});
        placesByBeacons.put("5:10", new ArrayList<String>() {{
            add("5");
        }});
        placesByBeacons.put("6:10", new ArrayList<String>() {{
            add("6");
        }});
        placesByBeacons.put("7:10", new ArrayList<String>() {{
            add("7");
        }});
        placesByBeacons.put("8:10", new ArrayList<String>() {{
            add("8");
        }});
        placesByBeacons.put("9:10", new ArrayList<String>() {{
            add("9");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;

    private final static String INFO_LOCAL = "- Info. local";
    private MediaRecorder mRecorder = null;
    private double ruido;
    private int lugar;
    private int temperatura;
    private int humedad;
    private int luz;
    private SQLiteDatabase mydatabase;
    private boolean listaInterfaz;
    private int preferenciaAct;
    private int preferencia1hora;
    private int preferencia2hora;
    private Registro[] act;
    private Registro[] f1;
    private Registro[] f2;
    private Activity esta;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //new MyAsync().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    String valor = places.get(0);
                    lugar = Integer.parseInt(valor);
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        ruido = 0;
        lugar = 0;
        temperatura = 18;
        humedad = 28;
        luz = 50;
        new TareaAudio().execute();
        mydatabase = openOrCreateDatabase("micampus", MODE_PRIVATE, null);
        //mydatabase.execSQL("DROP TABLE Registros;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Registros(Hora INT,Dia INT,Lugar INT,Ruido INT,Luz INT,Temperatura INT,Humedad INT);");
        /*
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 0 +","+ 1 +","+ 1 +"," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES(" + 1 + "," + 1 + "," + 2 + "," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES(" + 2 + "," + 1 + "," + 3 + "," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES(" + 3 + "," + 1 + "," + 3 + "," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 4 +","+ 1 +","+ 2 +"," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 5 +","+ 1 +","+ 1 +"," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES(" + 6 + "," + 1 + "," + 1 + "," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES(" + 7 + "," + 1 + "," + 2 + "," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 8 +","+ 1 +","+ 3 +"," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 9 +","+ 1 +","+ 3 +"," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 10 +","+ 1 +","+ 2 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 11 +","+ 1 +","+ 1 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 12 +","+ 1 +","+ 1 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 13 +","+ 1 +","+ 2 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 14 +","+ 1 +","+ 3 +"," + 80 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 15 +","+ 1 +","+ 3 +"," + 80 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 16 +","+ 1 +","+ 2 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 17 +","+ 1 +","+ 1 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 18 +","+ 1 +","+ 1 +"," + 70 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 19 +","+ 1 +","+ 2 +"," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 20 +","+ 1 +","+ 3 +"," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 21 +","+ 1 +","+ 3 +"," + 60 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 22 +","+ 1 +","+ 2 +"," + 50 + ");");
        mydatabase.execSQL("INSERT INTO Registros VALUES("+ 23 +","+ 1 +","+ 1 +"," + 50 + ");");*/
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

        /* Dar preferencias
        Cursor resultSet = mydatabase.rawQuery("Select Ruido, count(*) as cuenta from Registros where Hora = 18 group by ruido order by cuenta DESC;", null);
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
        resultSet.close();*/

        //GENERADOR DE REGISTROS

        //mydatabase.execSQL("DELETE from Registros;");

        /*for (int i = 0;i<24;i++)
        {
            for (int j = 1;j<8;j++)
            {
                for (int k = 40;k<100;k+=10)
                {
                    for (int l = 0;l<100;l+=10)
                    {
                        for (int m = 0;m<40;m+=5)
                        {
                            for (int n = 0;n<100;n+=10)
                            {
                                mydatabase.execSQL("INSERT INTO Registros VALUES("+ i +","+ j +","+ (int)(Math.random()*10) +"," + k + "," + l + "," + m + "," + n + ");");
                                //System.out.println("QUERY: " + "INSERT INTO Registros VALUES("+ i +","+ j +","+ (int)(Math.random()*10) +"," + k + "," + l + "," + m + "," + n + ");");
                            }
                        }
                    }
                }
                                System.out.println("QUERY: "+ i +" , "+ j );
            }
        }*/
        //Hora
        Calendar c = Calendar.getInstance();
        int hora_act = c.get(Calendar.HOUR_OF_DAY);
        preferenciaAct = darPreferencia(hora_act);
        preferencia1hora = darPreferencia(hora_act+1);
        preferencia2hora = darPreferencia(hora_act+2);
        System.out.println("Preferencia + + + + + + + + + + "+preferenciaAct);
        System.out.println("Preferencia + + + + + + + + + + " + preferencia1hora);
        System.out.println("Preferencia + + + + + + + + + + " + preferencia2hora);
        listaInterfaz = false;
        new Solicitar().execute();
        while(!listaInterfaz){}
        populateSuferenciasDia(act,f1,f2);
        populateListView();
        registerClickCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private int darPreferencia(int hora)
    {
        int respuesta = -1;
        Cursor resultSet = mydatabase.rawQuery("Select Ruido, Luz, Temperatura, Humedad, count(*) as cuenta from Registros where Hora = "+hora+" group by ruido, luz, temperatura, humedad order by cuenta DESC;", null);
        resultSet.moveToFirst();
        if (resultSet.moveToFirst()){
                String ruido = resultSet.getString(0);
                System.out.println("Ruido SQL: "+ ruido);
                String cuenta = resultSet.getString(4);
                System.out.println("Cuenta SQL: "+ cuenta);
                respuesta = Integer.parseInt(ruido);
            // do what ever you want here
        }
        resultSet.close();
        return respuesta;
    }

    //JSON ejemplo: {"dia":"6","hora":"12","ruido":"78","lugar":"3"}

    private class EnviarEstado extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                Thread.sleep(10000);
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
                //System.out.println("int day_of_week = c.get(Calendar.DAY_OF_WEEK): - - - : " + day_of_week);
                objetoJSON.put("dia",""+day_of_week);
                int hora = c.get(Calendar.HOUR_OF_DAY);
                //System.out.println("int hora = c.get(Calendar.HOUR_OF_DAY): - - - : " + hora);
                objetoJSON.put("hora",""+hora);
                int ruidoInt = (int)ruido;
                objetoJSON.put("ruido", ""+ruidoInt);
                objetoJSON.put("lugar", ""+lugar);
                System.out.println("JSON de ruido a mandar: "+objetoJSON);
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

                    //System.out.println("- - - - - - - - - - - - - - - - - -");
                    //System.out.println("" + sb.toString());
                    //System.out.println("- - - - - - - - - - - - - - - - - -");

                } else {
                    //System.out.println(con.getResponseMessage());
                }
            }
            catch(Exception e)
            {
                System.out.println(e.fillInStackTrace());
            }
            return 0L;
        }

        protected void onPostExecute(Long result) {
            //System.out.println("Downloaded " + result + " bytes");
        }
    }

    private class Solicitar extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int dia = c.get(Calendar.DAY_OF_WEEK);
            act = llamarHTTP(dia,hora,preferenciaAct);
            f1 = llamarHTTP(dia,hora+1,preferencia1hora);
            f2 = llamarHTTP(dia,hora+2,preferencia2hora);
            listaInterfaz = true;
            return 0L;
        }

        private Registro[] llamarHTTP(int diaP, int horaP, int ruidoP)
        {
            Registro[] a_entregar = new Registro[3];
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
                objetoJSON.put("dia",""+diaP);
                objetoJSON.put("hora",""+horaP);
                objetoJSON.put("ruido", "" + ruidoP);
                //objetoJSON.put("lugar", ""+lugar);

                //System.out.println(objetoJSON);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(objetoJSON.toString());
                wr.flush();

//display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                //System.out.println("Respuesta:");
                //System.out.println(HttpResult);
                //System.out.println("Respuesta esperada:");
                //System.out.println(HttpURLConnection.HTTP_OK);
                if (HttpResult == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    br.close();
                    String res = sb.toString();
                    //System.out.println("- - - - - - Respuesta del servidor - - - - - -");
                    //System.out.println("" + res);
                    //System.out.println("- - - - - - Respuesta del servidor - - - - - -");
                    JSONArray objetoJSON_recepcion = new JSONArray(res);
                    for (int i=0;i<objetoJSON_recepcion.length() && i<3;i++)
                    {
                        JSONObject o = objetoJSON_recepcion.getJSONObject(i);
                        Registro r = new Registro(horaP,o.getString("confianza"),o.getInt("lugar"),o.getInt("ruido"));
                        a_entregar[i] = r;
                    }
                    System.out.println("- - - - - - Sugerencia del servidor - - - - - -");
                    System.out.println(objetoJSON_recepcion);

                } else {
                    //System.out.println(con.getResponseMessage());
                }
            }
            catch(Exception e)
            {
                System.out.println("Problemas de red");
                Cursor resultSet = mydatabase.rawQuery("Select Lugar, AVG(Ruido), count(*) as cuenta from Registros where Hora = "+horaP+" and Ruido > "+(ruidoP-1)+" and Ruido < "+(ruidoP+11)+" and Dia = "+diaP+" group by lugar order by cuenta DESC;", null);
                resultSet.moveToFirst();
                int l = 0;
                if (resultSet.moveToFirst()){
                    do{
                        String lugar = resultSet.getString(0);
                        System.out.println("Lugar sugerencia sin red: "+ lugar + "de la hora: "+horaP);
                        String ruidoNoRed = resultSet.getString(1);
                        System.out.println("Ruido promedio sin red: "+ ruidoNoRed + "de la hora: "+horaP);
                        String cuenta = resultSet.getString(2);
                        System.out.println("Cuenta sin red: "+ cuenta + "de la hora: "+horaP);
                        System.out.println("-----------------");
                        System.out.println("-----------------");
                        int ruidoParseado = (int)Double.parseDouble(ruidoNoRed);
                        if(l<3)
                        {
                            Registro r = new Registro(horaP,"",Integer.parseInt(lugar)+10,ruidoParseado);
                            a_entregar[l] = r;
                        }
                        l++;
                        // do what ever you want here
                    }while(resultSet.moveToNext());
                }
                resultSet.close();
                System.out.print("Lista la query");
            }
            return a_entregar;
        }

        protected void onPostExecute(Long result) {
            //System.out.println("Downloaded " + result + " bytes");
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
            mydatabase.execSQL("INSERT INTO Registros VALUES("+ hora +","+ day +","+ lugar +"," + ((int)ruido/10)*10 + ","+ (luz/10)*10 +","+ (temperatura/10)*10 +","+ (humedad/10)*10 +");");
            new EnviarEstado().execute();
            return 0L;
        }

        protected void onPostExecute(Long result) {
            System.out.println("Ruido " + ruido + " dB");
        }
    }

    private class MyAsync extends AsyncTask {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            PD = new ProgressDialog(MainActivity.this);
            PD.setTitle("Please Wait..");
            PD.setMessage("Loading...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            return null;
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

    public String darNombreLugar(int id)
    {
        if(id == 0)
        {
            return "ML S1";
        }
        else if(id == 1)
        {
            return "ML 3";
        }
        else if(id == 2)
        {
            return "ML 4";
        }
        else if(id == 3)
        {
            return "ML 5";
        }
        else if(id == 4)
        {
            return "ML 6";
        }
        else if(id == 5)
        {
            return "ML 7";
        }
        else if(id == 6)
        {
            return "ML 8";
        }
        else if(id == 7)
        {
            return "SD 7";
        }
        else if(id == 8)
        {
            return "SD 8";
        }
        else if(id == 9)
        {
            return "SD 9";
        }
        else if(id == 10)
        {
            return "ML S1 "+INFO_LOCAL;
        }
        else if(id == 11)
        {
            return "ML 3 "+INFO_LOCAL;
        }
        else if(id == 12)
        {
            return "ML 4 "+INFO_LOCAL;
        }
        else if(id == 13)
        {
            return "ML 5 "+INFO_LOCAL;
        }
        else if(id == 14)
        {
            return "ML 6 "+INFO_LOCAL;
        }
        else if(id == 15)
        {
            return "ML 7 "+INFO_LOCAL;
        }
        else if(id == 16)
        {
            return "ML 8 "+INFO_LOCAL;
        }
        else if(id == 17)
        {
            return "SD 7 "+INFO_LOCAL;
        }
        else if(id == 18)
        {
            return "SD 8 "+INFO_LOCAL;
        }
        else if(id == 19)
        {
            return "SD 9 "+INFO_LOCAL;
        }
        else
        {
            return "SD 9 "+INFO_LOCAL;
        }
    }

    public int darFoto(int id)
    {
        if(id == 0)
        {
            return R.drawable.mls1;
        }
        else if(id == 1)
        {
            return R.drawable.ml3;
        }
        else if(id == 2)
        {
            return R.drawable.ml4;
        }
        else if(id == 3)
        {
            return R.drawable.ml5;
        }
        else if(id == 4)
        {
            return R.drawable.ml6;
        }
        else if(id == 5)
        {
            return R.drawable.ml7;
        }
        else if(id == 6)
        {
            return R.drawable.ml8;
        }
        else if(id == 7)
        {
            return R.drawable.sd7;
        }
        else if(id == 8)
        {
            return R.drawable.sd8;
        }
        else if(id == 9)
        {
            return R.drawable.sd9;
        }
        else if(id == 10)
        {
            return R.drawable.mls1;
        }
        else if(id == 11)
        {
            return R.drawable.ml3;
        }
        else if(id == 12)
        {
            return R.drawable.ml4;
        }
        else if(id == 13)
        {
            return R.drawable.ml5;
        }
        else if(id == 14)
        {
            return R.drawable.ml6;
        }
        else if(id == 15)
        {
            return R.drawable.ml7;
        }
        else if(id == 16)
        {
            return R.drawable.ml8;
        }
        else if(id == 17)
        {
            return R.drawable.sd7;
        }
        else if(id == 18)
        {
            return R.drawable.sd8;
        }
        else if(id == 19)
        {
            return R.drawable.sd9;
        }
        else
        {
            return R.drawable.sd9;
        }
    }

    public void populateSuferenciasDia(Registro[] r1,Registro[] r2,Registro[] r3) {
        Instancia instancia = Instancia.darInstancia();
        System.out.println("S Principal ACT");
        System.out.println(r1[0]);
        System.out.println("S 1 ACT");
        System.out.println(r1[1]);
        System.out.println("S 2 ACT");
        System.out.println(r1[2]);
        System.out.println("S Principal T1");
        System.out.println(r2[0]);
        System.out.println("S 1 T1");
        System.out.println(r2[1]);
        System.out.println("S 2 T1");
        System.out.println(r2[2]);
        System.out.println("S Principal T2");
        System.out.println(r3[0]);
        System.out.println("S 1 T2");
        System.out.println(r3[1]);
        System.out.println("S 2 T2");
        System.out.println(r3[2]);
        //instancia.agregarSugerencia(new SugerenciaDia(darNombreLugar(r1[0].darLugar()), r1[0].darRuido(), r1[0].darHora(), darFoto(r1[0].darLugar())));
        instancia.agregarSugerencia(new SugerenciaDia(darNombreLugar(r1[0].darLugar()), r1[0].darRuido(), r1[0].darHora(), darFoto(r1[0].darLugar()),new SugerenciaDia(darNombreLugar(r1[1].darLugar()), r1[1].darRuido(), r1[1].darHora(), darFoto(r1[1].darLugar()),null,null),new SugerenciaDia(darNombreLugar(r1[2].darLugar()), r1[2].darRuido(), r1[2].darHora(), darFoto(r1[2].darLugar()),null,null)));
        instancia.agregarSugerencia(new SugerenciaDia(darNombreLugar(r2[0].darLugar()), r2[0].darRuido(), r2[0].darHora(), darFoto(r2[0].darLugar()),new SugerenciaDia(darNombreLugar(r2[1].darLugar()), r2[1].darRuido(), r2[1].darHora(), darFoto(r2[1].darLugar()),null,null),new SugerenciaDia(darNombreLugar(r2[2].darLugar()), r2[2].darRuido(), r2[2].darHora(), darFoto(r2[2].darLugar()),null,null)));
        instancia.agregarSugerencia(new SugerenciaDia(darNombreLugar(r3[0].darLugar()), r3[0].darRuido(), r3[0].darHora(), darFoto(r3[0].darLugar()),new SugerenciaDia(darNombreLugar(r3[1].darLugar()), r3[1].darRuido(), r3[1].darHora(), darFoto(r3[1].darLugar()),null,null),new SugerenciaDia(darNombreLugar(r3[2].darLugar()), r3[2].darRuido(), r3[2].darHora(), darFoto(r3[2].darLugar()),null,null)));
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

                Intent intent = new Intent(MainActivity.this, OtrasRecomendaciones.class);
                intent.putExtra("position", "" + position);
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

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }
}
