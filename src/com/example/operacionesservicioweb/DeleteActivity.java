package com.example.operacionesservicioweb;

import java.io.IOException;

import org.apache.http.client.methods.HttpDelete;
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

public class DeleteActivity extends Activity {
	
	private JSONArray records;
	private JSONObject record;
	private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_view);
        
        Intent i = getIntent();
        extras = i.getExtras();
        String datos = extras.getString("datos");

		try {
			records = new JSONArray(datos);
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
			nombre.setFocusable(false);
			apellidos.setFocusable(false);
			direccion.setFocusable(false);
			telefono.setFocusable(false);
			equipo.setFocusable(false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void deleteRecord(View v) {
    	new DeleteBD().execute();
    	
    	Intent i=new Intent();
		i.putExtra("respuesta","Borrado realizado");
		setResult(RESULT_OK,i);
    	
		finish();
    }
    
    private class DeleteBD extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String url = extras.getString("url");
			String dni = extras.getString("dni");
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpDelete httpdelete = new HttpDelete(url + "/" + dni);
				httpclient.execute(httpdelete);
				httpclient.close();
			} catch (IOException e) {
				Log.e("Error en la operación (IO)", e.toString());
				e.printStackTrace();
			}
			return null;
		}
    }
    
    @Override
	public void onBackPressed(){
		Intent i=new Intent();
		i.putExtra("respuesta","Borrado cancelado");
		setResult(RESULT_CANCELED,i);
		super.onBackPressed();
	}
}
