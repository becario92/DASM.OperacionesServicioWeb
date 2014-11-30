package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class OpcionesActivity extends PreferenceActivity {

	private String urlFichas;
	private boolean servicioConectado = false;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.opciones);
	}

	public void configurarConexion() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String urlConnect = pref.getString("menu_servidor","") + "/" + 
				pref.getString("menu_usuario","") + "/connect/" + pref.getString("menu_clave","");
		urlFichas = pref.getString("menu_servidor","") + "/" + 
				pref.getString("menu_usuario","") + "/fichas";
		new ConexionBD().execute(urlConnect);
	}

	@Override
	public void onBackPressed(){	
		if(!servicioConectado) {
			configurarConexion();
		}
		else {
			Intent i=new Intent();
			i.putExtra("url", urlFichas);
			i.putExtra("respuesta","Servicio conectado");
			setResult(RESULT_OK,i);
			super.onBackPressed();
		}
	}

	private class ConexionBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OpcionesActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String datos = "";
			String url = params[0];
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
				datos = EntityUtils.toString(response.getEntity());
				httpclient.close();
			} catch (IOException e) {
				Log.e("Error en la operaciÃ³n", e.toString());
				e.printStackTrace();
			}
			return datos;
		}


		@Override
		protected void onPostExecute(String datos) {
			String mensaje = "";
			pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(datos);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros) {
				case 0: 
					servicioConectado = true;
					onBackPressed();
					break;
				default: 
					mensaje = "No se ha podido establecer la conexión con el servicio. Configure la conexión.";
					Toast.makeText(OpcionesActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				}
			} catch (JSONException e) {
				mensaje = "No se ha podido establecer la conexión con el servicio. Configure la conexión.";
				Toast.makeText(OpcionesActivity.this, mensaje, Toast.LENGTH_LONG).show();
			}

		}
	}
}
