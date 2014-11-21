package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private final int CREATE_ACTIVITY = 002;
	private final int READ_ACTIVITY = 003;
	private final int UPDATE_ACTIVITY = 004;
	private final int DELETE_ACTIVITY = 005;
	
	private EditText dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        
        dni = (EditText)findViewById(R.id.equipoText);
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
    
    public void lanzarCreateActivity(View v) {
        //Intent i = new Intent(this,CreateActivity.class);
        //i.putExtra("mensaje", "desde la actividad MAIN");
        //startActivityForResult(i, CREATE_ACTIVITY);
        new InsercionBD().execute(dni.getText().toString());
    }
    
    public void lanzarReadActivity(View v) {
        //Intent i = new Intent(this,ReadActivity.class);
        //i.putExtra("mensaje", "desde la actividad MAIN");
        //startActivityForResult(i, READ_ACTIVITY);
        new ConsultaBD().execute(dni.getText().toString());
    }
    
    public void lanzarUpdateActivity(View v) {
        Intent i = new Intent(this,UpdateActivity.class);
        i.putExtra("mensaje", "desde la actividad MAIN");
        startActivityForResult(i, UPDATE_ACTIVITY);
    }
    
    public void lanzarDeleteActivity(View v) {
        Intent i = new Intent(this,DeleteActivity.class);
        i.putExtra("mensaje", "desde la actividad MAIN");
        startActivityForResult(i, DELETE_ACTIVITY);
    }
    
    private class ConsultaBD extends AsyncTask<String, Void, String>{

        private ProgressDialog pDialog;
        private boolean error;
        private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw02/fichas";
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error = false;
            pDialog = new ProgressDialog(MainActivity.this);
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
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                mensaje = "La consulta genera un error de datos";
            }
            Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
        }
        
    }
    
    private class InsercionBD extends AsyncTask<String, Void, String>{

        private ProgressDialog pDialog;
        private boolean error;
        private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw02/fichas";
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error = false;
            pDialog = new ProgressDialog(MainActivity.this);
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
                HttpPost httppost = new HttpPost(URL);                
                
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("DNI", "87654321");
                jsonObject.put("Nombre", "Pepe");
                jsonObject.put("Apellidos", "Perito Palo");
                jsonObject.put("Direccion", "Golosina, 1");
                jsonObject.put("Telefono", "91-660-56-56");
                jsonObject.put("Equipo", "2");
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httppost.setEntity(se);
                
                HttpResponse response = httpclient.execute(httppost);
                datos = EntityUtils.toString(response.getEntity());
                httpclient.close();
            } catch (IOException e) {
                error = true;
                Log.e("Error en la operación (IO)", e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                error = true;
                Log.e("Error en la operación (JSON)", e.toString());
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
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                mensaje = "La consulta genera un error de datos";
            }
            Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
        }
        
    }
}
