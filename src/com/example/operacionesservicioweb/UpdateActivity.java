package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends Activity {
	
	private JSONArray records;
	private Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_view);

		Intent i = getIntent();
		extras = i.getExtras();
		String mensaje = extras.getString("mensaje");
		String datos = extras.getString("datos");
		Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

		try {
			records = new JSONArray(datos);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject record;

		try {
			record = records.getJSONObject(1);

			TextView dni = (TextView)findViewById(R.id.ReadDni);
			EditText nombre = (EditText)findViewById(R.id.ReadNombre);
			EditText apellidos = (EditText)findViewById(R.id.ReadApellidos);
			EditText direccion = (EditText)findViewById(R.id.ReadDireccion);
			EditText telefono = (EditText)findViewById(R.id.ReadTelefono);
			EditText equipo = (EditText)findViewById(R.id.ReadEquipo);

			dni.setText(record.getString("DNI"));
			nombre.setText(record.getString("Nombre"));
			apellidos.setText(record.getString("Apellidos"));
			direccion.setText(record.getString("Direccion"));
			telefono.setText(record.getString("Telefono"));
			equipo.setText(record.getString("Equipo"));
			
			dni.setFocusable(false);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
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
	
	public void modificarRegistro(View v) {
		try {
			EditText dni = (EditText)findViewById(R.id.ReadDni);
			EditText nombre = (EditText)findViewById(R.id.ReadNombre);
			EditText apellidos = (EditText)findViewById(R.id.ReadApellidos);
			EditText direccion = (EditText)findViewById(R.id.ReadDireccion);
			EditText telefono = (EditText)findViewById(R.id.ReadTelefono);
			EditText equipo = (EditText)findViewById(R.id.ReadEquipo);
			
			String json = "";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("DNI", dni.getText().toString());
			jsonObject.put("Nombre", nombre.getText().toString());
			jsonObject.put("Apellidos", apellidos.getText().toString());
			jsonObject.put("Direccion", direccion.getText().toString());
			jsonObject.put("Telefono", telefono.getText().toString());
			jsonObject.put("Equipo", equipo.getText().toString());
			json = jsonObject.toString();
			
			Intent i=new Intent();
			i.putExtra("respuesta","Inserci�n realizada");
			i.putExtra("json", json);
			setResult(RESULT_OK,i);
			new ModificacionBD().execute(json);
			finish();
		} catch (JSONException e) {
			Log.e("Error en la operación (JSON)", e.toString());
			e.printStackTrace();
		} 
	}
	
	private class ModificacionBD extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String json = params[0];
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPut httpput = new HttpPut(extras.getString("url"));
				StringEntity se = new StringEntity(json);
				httpput.setEntity(se);
				httpclient.execute(httpput);
				httpclient.close();
			} catch (IOException e) {
				Log.e("Error en la operación (IO)", e.toString());
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
