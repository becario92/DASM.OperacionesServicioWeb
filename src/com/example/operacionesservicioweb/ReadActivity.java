package com.example.operacionesservicioweb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
		String datos = extras.getString("datos");

		try {
			records = new JSONArray(datos);
			numRecord = 1;
			numRecords = records.getJSONObject(0).getInt("NUMREG");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		showRecord(numRecord);
	}

	public void showRecord(int numRecord) {
		JSONObject record;
		
		try {
			TextView recordView = (TextView)findViewById(R.id.recordView);
			recordView.setText("registro " + numRecord + " de " + numRecords);
			
			record = records.getJSONObject(numRecord);
			
			TextView dni = (TextView)findViewById(R.id.dni);
			TextView nombre = (TextView)findViewById(R.id.nombre);
			TextView apellidos = (TextView)findViewById(R.id.apellidos);
			TextView direccion = (TextView)findViewById(R.id.direccion);
			TextView telefono = (TextView)findViewById(R.id.telefono);
			TextView equipo = (TextView)findViewById(R.id.equipo);
			
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
	
	@Override
	public void onBackPressed(){
		Intent i=new Intent();
		i.putExtra("respuesta","Consulta finalizada");
		setResult(RESULT_OK,i);
		super.onBackPressed();
	}
}
