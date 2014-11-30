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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateActivity extends Activity {
	
	private JSONArray records;
	private Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_view);
		
		extras = getIntent().getExtras();
		String datos = extras.getString("datos");

		try {
			records = new JSONArray(datos);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject record;

		try {
			record = records.getJSONObject(1);

			TextView dni = (TextView)findViewById(R.id.dni);
			EditText nombre = (EditText)findViewById(R.id.nombre);
			EditText apellidos = (EditText)findViewById(R.id.apellidos);
			EditText direccion = (EditText)findViewById(R.id.direccion);
			EditText telefono = (EditText)findViewById(R.id.telefono);
			EditText equipo = (EditText)findViewById(R.id.equipo);

			dni.setText(record.getString("DNI"));
			nombre.setText(record.getString("Nombre"));
			apellidos.setText(record.getString("Apellidos"));
			direccion.setText(record.getString("Direccion"));
			telefono.setText(record.getString("Telefono"));
			equipo.setText(record.getString("Equipo"));
			
			dni.setEnabled(false);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	public void updateRecord(View v) {
		try {
			EditText dni = (EditText)findViewById(R.id.dni);
			EditText nombre = (EditText)findViewById(R.id.nombre);
			EditText apellidos = (EditText)findViewById(R.id.apellidos);
			EditText direccion = (EditText)findViewById(R.id.direccion);
			EditText telefono = (EditText)findViewById(R.id.telefono);
			EditText equipo = (EditText)findViewById(R.id.equipo);
			
			String json = "";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("DNI", dni.getText().toString());
			jsonObject.put("Nombre", nombre.getText().toString());
			jsonObject.put("Apellidos", apellidos.getText().toString());
			jsonObject.put("Direccion", direccion.getText().toString());
			jsonObject.put("Telefono", telefono.getText().toString());
			jsonObject.put("Equipo", equipo.getText().toString());
			json = jsonObject.toString();
			
			new UpdateBD().execute(json);
			
			Intent i=new Intent();
			i.putExtra("respuesta","Actualización realizada");
			setResult(RESULT_OK,i);
			
			finish();
		} catch (JSONException e) {
			Log.e("Error en la operaciÃ³n (JSON)", e.toString());
			e.printStackTrace();
		} 
	}
	
	private class UpdateBD extends AsyncTask<String, Void, Void> {

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
				Log.e("Error en la operaciÃ³n (IO)", e.toString());
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	@Override
	public void onBackPressed(){
		Intent i=new Intent();
		i.putExtra("respuesta","Actualización cancelada");
		setResult(RESULT_CANCELED,i);
		super.onBackPressed();
	}
}
