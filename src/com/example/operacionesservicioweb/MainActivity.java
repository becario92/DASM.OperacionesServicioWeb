package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

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

	private final int CREATE_ACTIVITY = 001;
	private final int READ_ACTIVITY = 002;
	private final int UPDATE_ACTIVITY = 003;
	private final int DELETE_ACTIVITY = 004;
	private final int OPTIONS_ACTIVITY = 005;
	private String URL;//http://demo.calamar.eui.upm.es/dasmapi/v1/miw02/fichas
	private EditText dni;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		dni = (EditText)findViewById(R.id.equipoText);
		dni.setFocusable(false);
		findViewById(R.id.imageBuscar).setClickable(false);
		findViewById(R.id.imageAnadir).setClickable(false);
		findViewById(R.id.imageEditar).setClickable(false);
		findViewById(R.id.imageBorrar).setClickable(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void launchOptionsActivity(MenuItem item) {
		Intent i = new Intent(MainActivity.this, OpcionesActivity.class);
		startActivityForResult(i, OPTIONS_ACTIVITY);
	}

	public void launchCreateActivity(View v) {
		new CreateBD().execute(dni.getText().toString());
	}

	public void launchReadActivity(View v) {
		new ReadBD().execute(dni.getText().toString());
	}

	public void launchUpdateActivity(View v) {
		new UpdateBD().execute(dni.getText().toString());
	}

	public void launchDeleteActivity(View v) {
		new DeleteBD().execute(dni.getText().toString());
	}

	@Override
	protected void onActivityResult(int actividad, int resultado, Intent datos) {
		String respuesta = datos.getStringExtra("respuesta");
		Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
		if(actividad == OPTIONS_ACTIVITY) {
			URL=datos.getStringExtra("url");
			dni.setFocusable(true);
			dni.setFocusableInTouchMode(true);
			findViewById(R.id.imageBuscar).setClickable(true);
			findViewById(R.id.imageAnadir).setClickable(true);
			findViewById(R.id.imageEditar).setClickable(true);
			findViewById(R.id.imageBorrar).setClickable(true);
		}
	}

	private class ReadBD extends AsyncTask<String, Void, String>{

		private ProgressDialog pDialog;
		private boolean error;

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
		protected void onPostExecute(String datos) {
			String mensaje = "";

			pDialog.dismiss();
			if(error) {
				mensaje = "La consulta genera un error";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONArray arrayDatos = new JSONArray(datos);
				int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros) {
				case -1: 
					mensaje = "La consulta genera un error";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				case 0: 
					mensaje = "Registro no existente";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				default:
					Intent i = new Intent(MainActivity.this, ReadActivity.class);
					i.putExtra("datos", datos);
					startActivityForResult(i, READ_ACTIVITY);
				}
			} catch (Exception e) {
				mensaje = "La consulta genera un error de datos";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
			}
		}

	}

	private class CreateBD extends AsyncTask<String, Void, String>{

		private ProgressDialog pDialog;
		private boolean error;

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
			} else {
				error = true;
			}
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
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
		protected void onPostExecute(String datos) {
			String mensaje = "";
			pDialog.dismiss();
			if(error) {
				mensaje = "Debe introducir un DNI";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONArray arrayDatos = new JSONArray(datos);
				int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros) {
				case -1: 
					mensaje = "La inserci�n genera un error";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				case 0:
					Intent i = new Intent(MainActivity.this, CreateActivity.class);
					i.putExtra("url", URL);
					i.putExtra("datos", datos);
					i.putExtra("dni", dni.getText().toString());
					startActivityForResult(i, CREATE_ACTIVITY);
					break;
				default: 
					mensaje = "Registro existente";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				mensaje = "La inserci�n genera un error de datos";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
			}
		}

	}

	private class UpdateBD extends AsyncTask<String, Void, String>{

		private ProgressDialog pDialog;
		private boolean error;

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
			} else {
				error = true;
			}
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
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
		protected void onPostExecute(String datos) {
			String mensaje = "";

			pDialog.dismiss();
			if(error) {
				mensaje = "Debe introducir un DNI";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONArray arrayDatos = new JSONArray(datos);
				int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros) {
				case -1: 
					mensaje = "La actualizaci�n genera un error";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				case 0: 
					mensaje = "Registro no existente";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				default: 
					Intent i = new Intent(MainActivity.this, UpdateActivity.class);
					i.putExtra("datos", datos);
					i.putExtra("url", URL);
					startActivityForResult(i, UPDATE_ACTIVITY);
				}
			} catch (Exception e) {
				mensaje = "La actualizaci�n genera un error de datos";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
			}
		}

	}

	private class DeleteBD extends AsyncTask<String, Void, String>{

		private ProgressDialog pDialog;
		private boolean error;

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
			} else {
				error = true;
			}
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
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
		protected void onPostExecute(String datos) {
			String mensaje = "";

			pDialog.dismiss();
			if(error) {
				mensaje = "Debe introducir un DNI";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONArray arrayDatos = new JSONArray(datos);
				int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros) {
				case -1: 
					mensaje = "El borrado genera un error";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				case 0: 
					mensaje = "Registro no existente";
					Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
					break;
				default: 
					Intent i = new Intent(MainActivity.this, DeleteActivity.class);
					i.putExtra("datos", datos);
					i.putExtra("url", URL);
					i.putExtra("dni", dni.getText().toString());
					startActivityForResult(i, DELETE_ACTIVITY);
				}
			} catch (Exception e) {
				mensaje = "El borrado genera un error de datos";
				Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
			}
		}
	}

}
