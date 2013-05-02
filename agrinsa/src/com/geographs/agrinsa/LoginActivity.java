package com.geographs.agrinsa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.sincronizacion.Sincronizacion;
import com.geographs.agrinsa.sqllite.DBHelper;
import com.geographs.agrinsa.util.ConnectionDetection;

public class LoginActivity extends Activity {

	DBHelper consultas;	
	MenuItem itemsync;
	MenuItem itemserver;
	Button ingreso;
	String ipserver;
	EditText servidortxt;
	ProgressDialog pdsincronizacion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
	        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		consultas = new DBHelper(this);
		ipserver=consultas.getConstante("IPWS");		
		ingreso=(Button)findViewById(R.id.btnIngreso);	
		ConnectionDetection cd = new ConnectionDetection(getApplicationContext());
		Constantes.isNetwork=cd.isConnectingToInternet();
		if(Constantes.isNetwork){
			new Sincronizacion(this.getApplicationContext(),this).execute();	
			pdsincronizacion=ProgressDialog.show(LoginActivity.this, "Por favor espere", "Sincronizando...", true);			
		}else{
			Toast toast=Toast.makeText(this, "No hay conexion de red detectada para hacer la sincronizacion",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);		
		return true;
	}

	
	public void validaUsuario(View view) {
		EditText edUsuario = (EditText) findViewById(R.id.usuario);
		EditText edPwd = (EditText) findViewById(R.id.contrasena);
		boolean validado = consultas.validarUsuario(edUsuario.getText()
				.toString(), edPwd.getText().toString());
		if (validado) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			Constantes.usuarioid_actual=consultas.getUsuarioid_actual();
		} else {
			Toast toast=Toast.makeText(this, "Nombre de usuario o contasena invalida",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}
	
	public void setResultadoSincronizacion(String resultado){
		ingreso.setEnabled(true);
		if(itemsync!=null){
			itemsync.collapseActionView();
			itemsync.setActionView(null);
		}
		pdsincronizacion.dismiss();
		Toast toast=Toast.makeText(LoginActivity.this,resultado, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menu_sincronizar:
	    	itemsync = item;
	    	ConnectionDetection cd = new ConnectionDetection(getApplicationContext());
			Constantes.isNetwork=cd.isConnectingToInternet();
			if(Constantes.isNetwork){
		    	itemsync.setActionView(R.layout.progresync);
		    	itemsync.expandActionView();
		    	ingreso.setEnabled(false);
				new Sincronizacion(this.getApplicationContext(),this).execute();	
				pdsincronizacion=ProgressDialog.show(LoginActivity.this, "Por favor espere", "Sincronizando...", true);
			}else{
				Toast toast=Toast.makeText(this, "No hay conexion de red detectada para hacer la sincronizacion",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
	    	break;
	    case R.id.menu_servidor:	    	
	    	itemserver=item;
	    	itemserver.setActionView(R.layout.servidor);
	    	itemserver.expandActionView();
			servidortxt=(EditText)findViewById(R.id.txtservidor);
	    	servidortxt.setText(ipserver);
	    	servidortxt.setOnEditorActionListener(new OnEditorActionListener() {
	    	    @Override
	    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	    	        boolean handled = false;
	    	        if (actionId == EditorInfo.IME_ACTION_DONE) {
	    	        	if(!servidortxt.getText().toString().equalsIgnoreCase(ipserver)){
	    	        		String respuesta=consultas.setConstante("IPWS", servidortxt.getText().toString());
	    	        		if(respuesta.equalsIgnoreCase("OK")){
	    	        			Toast toast=Toast.makeText(LoginActivity.this,"Nuevo servidor almacenado.", Toast.LENGTH_LONG);
	    	        			toast.setGravity(Gravity.CENTER, 0, 0);
	    	        			toast.show();
	    	        		}else{
	    	        			Toast toast=Toast.makeText(LoginActivity.this,"Error almacenando el servidor:"+respuesta, Toast.LENGTH_LONG);
	    	        			toast.setGravity(Gravity.CENTER, 0, 0);
	    	        			toast.show();
	    	        		}
	    	        		
	    	        	}	    	
	    	        	itemserver.collapseActionView();
	    	        	itemserver.setActionView(null);				

	    	            handled = true;
	    	        }
	    	        return handled;
	    	    }
	    	});
	    	break;
	    default:
	      break;
	    }
		return true;
	}	
	
}
