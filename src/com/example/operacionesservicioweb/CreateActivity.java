package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

public class CreateActivity extends Activity {
	
	private Bundle extras;
	private TextView dni;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_view);

		extras = getIntent().getExtras();
		dni = (TextView)findViewById(R.id.dni);
		dni.setText(extras.getString("dni"));
		dni.setEnabled(false);
	}
	
	public void createRecord(View v) {
		try {
			
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
			
			new CreateBD().execute(json);
			
			Intent i=new Intent();
			i.putExtra("respuesta","Inserción realizada");
			setResult(RESULT_OK,i);
			
			finish();
		} catch (JSONException e) {
			Log.e("Error en la operaciÃ³n (JSON)", e.toString());
			e.printStackTrace();
		} 
	}
	
	private class CreateBD extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String json = params[0];
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(extras.getString("url"));
				StringEntity se = new StringEntity(json);
				httppost.setEntity(se);
				httpclient.execute(httppost);
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
		i.putExtra("respuesta","Inserción cancelada");
		setResult(RESULT_CANCELED,i);
		super.onBackPressed();
	}
}
