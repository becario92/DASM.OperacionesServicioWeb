package com.example.operacionesservicioweb;

import com.example.operacionesservicioweb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	private final int CREATE_ACTIVITY = 002;
	private final int READ_ACTIVITY = 003;
	private final int UPDATE_ACTIVITY = 004;
	private final int DELETE_ACTIVITY = 005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
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
        Intent i = new Intent(this,CreateActivity.class);
        i.putExtra("mensaje", "desde la actividad MAIN");
        startActivityForResult(i, CREATE_ACTIVITY);
    }
    
    public void lanzarReadActivity(View v) {
        Intent i = new Intent(this,ReadActivity.class);
        i.putExtra("mensaje", "desde la actividad MAIN");
        startActivityForResult(i, READ_ACTIVITY);
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
}
