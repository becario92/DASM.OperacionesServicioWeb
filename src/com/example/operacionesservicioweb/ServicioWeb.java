package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ServicioWeb extends Activity {
    
    private EditText dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        
        dni = (EditText)findViewById(R.id.equipoText);
    }
    
    public void consultar(View v){
        new ConsultaBD().execute(dni.getText().toString());
    }
    
    private class ConsultaBD extends AsyncTask<String, Void, String>{

        private ProgressDialog pDialog;
        private boolean error;
        private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw02/fichas";
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error = false;
            pDialog = new ProgressDialog(ServicioWeb.this);
            pDialog.setMessage(getString(R.string.progress_title));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        @Override
        protected String doInBackground(String... parametros) {
            String dni = parametros[0];
            String datos = "";
            String url_final = URL;
            if(!dni.equals("")) {
                url_final += "/" + dni;
            }
            try {
                AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
                HttpGet httpget = new HttpGet(url_final);
                //httppost.getEntity(new StringEntity(xxx, HTTP.UTF_8));;
                HttpResponse response = httpclient.execute(httpget);
                datos = EntityUtils.toString(response.getEntity());
                httpclient.close();
            } catch (IOException e) {
                error = true;
                Log.e("Error en la operación", e.toString());
                e.printStackTrace();
            }
            return datos;            
        }
        
        @Override
        protected void onPostExecute(String info) {
            String mensaje = "";
            
            pDialog.dismiss();
            if(error) {
                mensaje = "La consulta genera un error";
                Toast.makeText(ServicioWeb.this, mensaje, Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONArray arrayDatos = new JSONArray(info);
                int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
                switch(numRegistros) {
                case -1: mensaje = "La consulta genera un error";
                        break;
                case 0: mensaje = "La consulta no devuelve registros";
                        break;
                default: mensaje = "La consulta devuelve " + numRegistros + "registro/s";
                }
                Toast.makeText(ServicioWeb.this, mensaje, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                mensaje = "La consulta genera un error de datos";
            }
            Toast.makeText(ServicioWeb.this, mensaje, Toast.LENGTH_LONG).show();
        }
        
    }
}
