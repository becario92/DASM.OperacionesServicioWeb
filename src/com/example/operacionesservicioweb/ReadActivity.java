package com.example.operacionesservicioweb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReadActivity extends Activity {
	
	private JSONArray records;
	private int numRecord;
	private int numRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_view);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String mensaje = extras.getString("mensaje");
		String datos = extras.getString("datos");
		Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

		try {
			records = new JSONArray(datos);
			numRecord = 1;
			numRecords = records.getJSONObject(0).getInt("NUMREG");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		showRecord(numRecord);
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

	public void showRecord(int numRecord) {
		JSONObject record;
		
		try {
			TextView recordView = (TextView)findViewById(R.id.recordView);
			recordView.setText("registro " + numRecord + " de " + numRecords);
			
			record = records.getJSONObject(numRecord);
			
			TextView dni = (TextView)findViewById(R.id.ReadDni);
			TextView nombre = (TextView)findViewById(R.id.ReadNombre);
			TextView apellidos = (TextView)findViewById(R.id.ReadApellidos);
			TextView direccion = (TextView)findViewById(R.id.ReadDireccion);
			TextView telefono = (TextView)findViewById(R.id.ReadTelefono);
			TextView equipo = (TextView)findViewById(R.id.ReadEquipo);
			
			dni.setText(record.getString("DNI"));
			nombre.setText(record.getString("Nombre"));
			apellidos.setText(record.getString("Apellidos"));
			direccion.setText(record.getString("Direccion"));
			telefono.setText(record.getString("Telefono"));
			equipo.setText(record.getString("Equipo"));
			
			dni.setFocusable(false);
			nombre.setFocusable(false);
			apellidos.setFocusable(false);
			direccion.setFocusable(false);
			telefono.setFocusable(false);
			equipo.setFocusable(false);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void firstRecord(View v) {
		showRecord(numRecord = 1);
	}

	public void previousRecord(View v) {
		if(numRecord == 1) {
			showRecord(numRecord);
		} else {
			showRecord(--numRecord);			
		}
	}

	public void followingRecord(View v) {
		if(numRecord == numRecords) {
			showRecord(numRecord);
		} else {
			showRecord(++numRecord);			
		}
	}

	public void lastRecord(View v) {
		showRecord(numRecord = numRecords);
	}
}
