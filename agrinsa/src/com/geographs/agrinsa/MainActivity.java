package com.geographs.agrinsa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.sincronizacion.Sincronizacion;
import com.geographs.agrinsa.sincronizacion.SincronizacionLotes;
import com.geographs.agrinsa.util.ConnectionDetection;

public class MainActivity extends FragmentActivity {
	TabHost tHost;
	Fragmentcrearlote fragmentcrearlote;
	Fragmentvisita fragmentvisita;
	ProgressDialog pdsincronizacion;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentcrearlote=new Fragmentcrearlote();
		fragmentvisita=new Fragmentvisita();
		tHost = (TabHost) findViewById(android.R.id.tabhost);
		tHost.setup();
		ConnectionDetection cd = new ConnectionDetection(getApplicationContext());
		Constantes.isNetwork=cd.isConnectingToInternet();
		if(Constantes.isNetwork){
			pdsincronizacion=ProgressDialog.show(MainActivity.this, "Por favor espere", "Sincronizando...", true);
			new SincronizacionLotes(this.getApplicationContext(),this).execute();
		}
		
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("crealote")) {
					pushFragments(tabId, fragmentcrearlote);
				} else if (tabId.equals("hacervisita")) {
					pushFragments(tabId, fragmentvisita);
				}
			}

		};

		
		tHost.setOnTabChangedListener(tabChangeListener);
		TabHost.TabSpec tSpecClote = tHost.newTabSpec("crealote");
		tSpecClote.setIndicator("Crear Lote",
				getResources().getDrawable(R.drawable.crealote));
		tSpecClote.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
		tHost.addTab(tSpecClote);
		TabHost.TabSpec tSpecApple = tHost.newTabSpec("hacervisita");
		tSpecApple.setIndicator("Hacer Visita",
				getResources().getDrawable(R.drawable.hacervisita));
		tSpecApple.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
		tHost.addTab(tSpecApple);		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);	
		return true;
	}


	public void pushFragments(String tag, Fragment fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment);
		ft.commit();
	}
	
	public void addCoordenada(View view) {
		fragmentcrearlote.addCoordenada(view);
	}
	
	public void guardarnuevolote(View view) {
		fragmentcrearlote.guardarnuevolote(view);
	}
	
	public void guardarvisita(View view) {
		fragmentvisita.guardarvisita(view);
	}
	
	@SuppressWarnings("deprecation")
	public void abrePicker(View view) {
		fragmentvisita.abrePicker(view);
	}
	
	public void setResultadoSincronizacionLotes(String resultado){	
		pdsincronizacion.dismiss();
		Toast toast=Toast.makeText(MainActivity.this,resultado, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    	case R.id.menu_salir:
	    		Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
	    		break;
		}
		return true;
	}
	
	
}
