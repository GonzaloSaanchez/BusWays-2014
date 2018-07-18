package com.example.androidmaps;

import com.example.SimulacionProyecto.R;
import com.example.androidmaps.Home;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.R.bool;
import android.app.ActionBar;
import android.app.DownloadManager.Request;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
//WEB
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
//___

import java.io.*;
import java.net.*;
import java.util.Timer;

import java.util.Timer;
import java.util.TimerTask;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements OnMapClickListener{
	
	
	//Base de datos______________
	SQLite userdb;
	SQLiteDatabase db;
	Cursor CursorUno;
	Cursor CursorDos;
    //___________________________
	 private GoogleMap mapa;
	 //Web_____________________________________
	 String item; 
	 static String resultado;
	 EditText etResponse;
	 TextView tvIsConnected;
	 static String destinationAdressIngresada;
	 static double lat1d, long1d, lat2d, long2d;
	 //________________________________________
	 
	 //Velocidad_______________________________
	 static double distancia;
	 static float xe1, ye1;
	 static double velocidadProm;
	 static double tiempoLlegada;
	 TextView tiempo;
	 //________________________________________
	 static int PosicionCambiada = 0;
	 public static String SelecSpinner = "29";
	 public static String SelecSpinner2 = "Olivos";
	 
	 
	 @Override
     protected void onCreate(Bundle savedInstanceState) {
		 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent Myintent = new Intent(MainActivity.this,Home.class);
    	startActivity(Myintent); 
        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.setOnMapClickListener(this);
        
        //Base de datos_________________
        userdb = new SQLite(getApplicationContext(),"usuarios", null, 1);          
        db = userdb.getWritableDatabase();
       //________________________________

   
        
        
        //Paradas
    	 CursorUno = db.rawQuery("SELECT * FROM parada", null);
    	 if(CursorUno.moveToFirst()){		 
   		 xe1 = Float.valueOf(CursorUno.getString(1));
         ye1 = Float.valueOf(CursorUno.getString(2));
    	//destinationAdressIngresada = xe1 +" "+ ye1;
   	 }
    	 
   	 	velocidadProm = 18;
   	 	
   	 ActionBar actionBar = getActionBar();  
   	 actionBar.hide();	
   	 
   	final counterClass timer = new counterClass(3000, 1000);
	timer.start();
   
    }
    
    public static double Distancia(double lat1, double lon1, double lat2, double lon2)
    {
    	try{
    		double dist = 0.0;
    		double deltaLat = Math.toRadians(lat2 - lat1);
    		double deltaLon = Math.toRadians(lon2 - lon1);
    		lat1 = Math.toRadians(lat1);
    		lat2 = Math.toRadians(lat2);
    		lon1 = Math.toRadians(lon1);
    		lon2 = Math.toRadians(lon2);
    		double earthRadius = 6371;
    		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
    				+ Math.cos(lat1) * Math.cos(lat2)
    				* Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    		dist = earthRadius * c;
    		
    		return dist;
    	}
    	catch (Exception e){
    		return 0;
    	}
    }
    
    public void animateCamera(View view) {
        /* Prueba
        CursorUno = db.rawQuery("SELECT * FROM parada", null);
        if (CursorUno.moveToFirst()){
         Toast.makeText(getApplicationContext(), CursorUno.getString(1), Toast.LENGTH_SHORT).show();
         Toast.makeText(getApplicationContext(), CursorUno.getString(2), Toast.LENGTH_SHORT).show();
         
         	while(CursorUno.moveToNext()){
         		Toast.makeText(getApplicationContext(), CursorUno.getString(1), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), CursorUno.getString(2), Toast.LENGTH_SHORT).show();
         }
        }
        */
     }

     public void addMarker(View view) throws Exception {
       //Toast.makeText(getApplicationContext(), Home.SelecSpinner.toString(), Toast.LENGTH_SHORT).show();
       //Toast.makeText(getApplicationContext(), is.toString(), Toast.LENGTH_SHORT).show();
       //Toast.makeText(getApplicationContext(), destinationAdress.toString(), Toast.LENGTH_SHORT).show();
       /* Cuando este la base de datos Real. Codigo adaptado.
       float[] x = new float[3];
       float[] y = new float[3];
     	 
       CursorUno = db.rawQuery("SELECT * FROM parada", null);
    	 if(CursorUno.moveToFirst()){
     		 for (int i = 1; i > 0; i++)
     		 {
     		 x[i] = Float.valueOf(CursorUno.getString(1));
              y[i]= Float.valueOf(CursorUno.getString(2));
              mapa.addMarker(new MarkerOptions().position(new LatLng(x[i]
     	             , y[i])
     	       ));
        //  CursorUno.moveToNext();
     		 }
         */
     }
     
     @Override
     public void onMapClick(LatLng puntoPulsado) {
    /*    mapa.addMarker(new MarkerOptions().position(puntoPulsado).
           icon(BitmapDescriptorFactory
              .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
     */
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //WEB
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

     		} 
        catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
        	e.printStackTrace();
        	System.out.println(e.toString());
              }
        
        return result;  
    }
    
    
    
    
 // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
 
    // check network connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;   
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	//Toast.makeText(getBaseContext(), resultado.toString(), Toast.LENGTH_LONG).show();// Mostrar en pantalla el resultado del GET.
        	 resultado = result;
        	 //etResponse.setText(resultado);
       }
    }

    
    public void Separar(View view)
    {
     
    }

    public void Distancia(View view)
    {
    	
    }
    

    public void Variable(View view)
    {
    }

    //Timer___________
    public class counterClass extends CountDownTimer{

		public counterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			
		    //Conexion Web______________________________
	        //etResponse = (EditText) findViewById(R.id.etResponse);
	        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
	       //new HttpAsyncTask().execute("http://proyecto-busways.rhcloud.com/colectivos?l="+SelecSpinner+"&d="+	SelecSpinner2+"&Accion=Ver");
	       new HttpAsyncTask().execute("http://proyecto-busways.rhcloud.com/colectivos?l=29&d=olivos&Accion=Ver");
	        // check if you are connected or not
	        if(isConnected()){
	            //tvIsConnected.setBackgroundColor(0xFF00CC00);
	            //tvIsConnected.setText("You are conncted");
	        }
	        else{
	            tvIsConnected.setText("You are NOT conncted");
	        }
	        //__________________________________________
	        //Boton YO, Posicionar en el mapa._________
	        if(PosicionCambiada < 1){
	        if (mapa.getMyLocation() != null){
	            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
	               new LatLng( mapa.getMyLocation().getLatitude(), 
	             		  mapa.getMyLocation().getLongitude()), 15));
	            PosicionCambiada ++;
	        }
	        }
	        //_________________________________________
	        
	        //Paradas__________________________________
	    	 float x1;
	    	 float y1;
	    	 float x2;
	    	 float y2;
	    	 
	    	 CursorUno = db.rawQuery("SELECT * FROM parada", null);
	    	 if(CursorUno.moveToFirst()){		 
	    		 x1 = Float.valueOf(CursorUno.getString(1));
	             y1 = Float.valueOf(CursorUno.getString(2));
	             
	             CursorUno.moveToNext();
	             x2 = Float.valueOf(CursorUno.getString(1));
	             y2 = Float.valueOf(CursorUno.getString(2));
	             
	    		 mapa.addMarker(new MarkerOptions().position(new LatLng(x1
	    	             , y1)
	    	       ));
	    		 mapa.addMarker(new MarkerOptions().position(new LatLng(x2
	    	             , y2)
	    	       ));	 


	        //__________________________________________
		}
	    	//Variacion Velocidad (Capital o Provincia)
 		 	if(lat1d >= -34.6237 && lat1d <= -34.5392 && long1d >= -58.5024)
 	    	{
 	   	 	velocidadProm = 18;
 	    	}
 	    	else{
 	    	velocidadProm = 22;
 	    	}
 		 	Separar(null);
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			
			mapa.clear();
			//Separar__________________________________________________
			//"1:asd313,-34.548423,-58.455345,100";
	        //2:asd313|-34.6087|-58.3819|100:asd312|-34.6087|-58.382|100;
/*
			Double lat1f, long1f,lat2f, long2f;
	    	String lat1, long1, lat2, long2;
	    	String[] LatYLong1;
	    	String[] LatYLong2;
	    	String[] texto;
	    	texto = resultado.split(":");
	    	String textoaSepararUno, textoaSepararDos;
	    	
	    	textoaSepararUno = texto[1].toString();
	    	textoaSepararDos = texto[2].toString();
	    	textoaSepararUno = textoaSepararUno.replace("|", ",");
	    	textoaSepararDos = textoaSepararDos.replace("|", ",");
	        LatYLong1 = textoaSepararUno.split(","); //OJO, no funciona con "|".
	        LatYLong2 = textoaSepararDos.split(","); //OJO, no funciona con "|".
	        
	  
	    	lat1 = LatYLong1[1];
	    	long1 = LatYLong1[2];
	      	lat2 = LatYLong2[1];
	    	long2 = LatYLong2[2];

	    	lat1f = Double.valueOf(lat1);
	    	long1f = Double.valueOf(long1);
	    	lat2f = Double.valueOf(lat2);
	    	long2f = Double.valueOf(long2);
	    	lat1d = lat1f.doubleValue();
	    	long1d = long1f.doubleValue();
	    	lat2d = lat2f.doubleValue();
	    	long2d = long2f.doubleValue();
	    	
	    	LatLng Bondi1 = new LatLng(lat1d, long1d);
	    	LatLng Bondi2 = new LatLng(lat2d, long2d);
	   	
	   		Marker MarkerBondi1 = mapa.addMarker(new MarkerOptions()
	   	    .position(Bondi1));
	   		Marker MarkerBondi2 = mapa.addMarker(new MarkerOptions()
	   	    .position(Bondi2));
	   	 

	   		MarkerBondi1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	   		MarkerBondi2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	   		*/
			
		
			String[] res;
			res = resultado.split(":");

			int resPos0 = Integer.parseInt(res[0]);
			for(int i=0; i < resPos0; i++){

			res[i+1] = res[i+1].replace("|", ",");

			String res2[] = res[i+1].split(",");

			LatLng Bondi = new LatLng((Double.valueOf(res2[1])).doubleValue(), (Double.valueOf(res2[2])).doubleValue());

			mapa.addMarker(new MarkerOptions()
			.position(Bondi)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
			
			final counterClass timer = new counterClass(3000, 1000);
	    	timer.start();
	    	//___________________________________________________________
	    	
	    	//Distancia__________________________________________________
	    	int distanciaInt, tiempoLlegadaInt;
	    	distancia = Distancia(lat1d, long1d, xe1, ye1);
	    	distancia *= 100;
	    	distanciaInt = (int) distancia;
	    	distancia = (double) distanciaInt;
	    	distancia = distancia /1000; //Convertimos la distancia de metros a km.
	    	tiempoLlegada = (distancia / velocidadProm)*60;
	    	
	    	tiempoLlegada *= 10;
	    	tiempoLlegadaInt = (int)tiempoLlegada;

	    	
	    	String tiempoLlegadaStr;
	    	
	    	tiempoLlegadaStr = String.valueOf(tiempoLlegadaInt);
	    	
	    	tiempo = (TextView) findViewById(R.id.tiempo);
	    	tiempo.setText("Llega en: "+tiempoLlegadaStr+" minutos");
	    	//___________________________________________________________
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    public void Timerstart(View view)
    {
    	final counterClass timer = new counterClass(3000, 1000);
    	timer.start();
    }
   //_________________________________
}
