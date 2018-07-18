package com.example.androidmaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.SimulacionProyecto.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends Activity {
	public static String resultadoLineas;
	public static String resultadoRamales;
	public static String[] Lineas = {"Espere un momento"};
	public static String[] Ramales = {"Boca", "Olivos"} ;
	public static int i = 0;
	public static int pos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button Siguiente = (Button) findViewById(R.id.btn);
		
		
		//Spinner Colectivos
		Spinner spinnerColectivos = (Spinner) findViewById(R.id.Spinner1);
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Lineas);
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerColectivos.setAdapter(spinner_adapter);
			
		//Spinner Ramales
		Spinner spinnerRamales = (Spinner) findViewById(R.id.Spinner2);
		ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Ramales);
		spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRamales.setAdapter(spinner_adapter2);
		
	Siguiente.setOnClickListener(new View.OnClickListener() {
	     public void onClick(View view) {
		    Intent intent = new Intent();
		    setResult(RESULT_OK, intent);
		    finish();
           }
        });
			
	//Listener 
	spinnerColectivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
		int position, long id) {
    	// TODO Auto-generated method stub
	    Spinner spinnerColectivos = (Spinner) findViewById(R.id.Spinner1);
		MainActivity.SelecSpinner = spinnerColectivos.getSelectedItem().toString(); //Seleccion Spinner
		/*if(pos > 3){
		pedirRamales(MainActivity.SelecSpinner);
		}
		pos++;
		*/
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
				}  } );	

	
	spinnerRamales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
		int position, long id) {
    	// TODO Auto-generated method stub
	    Spinner spinnerRamales = (Spinner) findViewById(R.id.Spinner2);
		MainActivity.SelecSpinner2 = spinnerRamales.getSelectedItem().toString(); //Seleccion Spinner
	
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
				}  } );	
	
	
	
	
	 ActionBar actionBar = getActionBar();  
   	 actionBar.hide();	
	
   	 
   	 
   	 
   	 new HttpAsyncTask().execute("http://proyecto-busways.rhcloud.com/lineas");
   	 
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
        	switch(i){
        	case 0:
        		resultadoLineas = result;
				i++;
        		SepararLineas(resultadoLineas);
        		break;
        	
        	/*case 1: 
        		resultadoRamales = result;
        		SepararRamales(resultadoRamales); 
        		i = 0;
        		break;
        		*/
       }
    }
    }
    public void SepararLineas(String ResultadoLineas)
    {
    	Lineas = ResultadoLineas.split(":");
    	Lineas[0] = "Seleccione Linea";
    }
    public void pedirRamales(String selecSpinner)
    {
    	//i = 1;
    	//new HttpAsyncTask().execute("http://proyecto-busways.rhcloud.com/lineas?l="+SelecSpinner);
    }
    public void SepararRamales(String ResultadoRamales)
    {
    	//Ramales = ResultadoRamales.split(";");
    }
    
    public void Variable(View view){
    	//System.out.println(SelecSpinner+" "+SelecSpinner2);
    }
}