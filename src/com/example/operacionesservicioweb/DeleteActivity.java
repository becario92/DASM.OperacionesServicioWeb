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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteActivity extends Activity {
	
	private JSONArray records;
	private JSONObject record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_view);
        
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String mensaje = extras.getString("mensaje");
        String datos = extras.getString("datos");
		Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

		try {
			records = new JSONArray(datos);
		} catch (JSONException e) {
			e.printStackTrace();
		}

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
    
    public void borrarRegistro(View v) {
    	new BorradoBD().execute();
		finish();
    }
    
    private class BorradoBD extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String url = getIntent().getExtras().getString("url");
			String dni = getIntent().getExtras().getString("dni");
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
}
