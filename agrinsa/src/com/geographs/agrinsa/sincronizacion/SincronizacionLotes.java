package com.geographs.agrinsa.sincronizacion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.HttpHostConnectException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.geographs.agrinsa.MainActivity;
import com.geographs.agrinsa.RestClient;
import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.business.Lote;
import com.geographs.agrinsa.sqllite.DBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SincronizacionLotes extends AsyncTask<Void, Void, String> {
	DBHelper consultas;
	Context context;
	Activity activity;
	@SuppressWarnings("finally")
	@Override
	protected String doInBackground(Void... params) {
		String response="";
		String respuestausuario="";
		try {
			Gson gson = new Gson();
			consultas = new DBHelper(context);
			String ipws = consultas.getConstante("IPWS")+"agrinsa/rest/";
			/***************** SINCRONIZACION DE LOTES DEL USUARIO**************************/
			RestClient clientlotes = new RestClient(ipws+"visitas");
			clientlotes.AddParam("usuarioid", String.valueOf(Constantes.usuarioid_actual));
			clientlotes.AddHeader("Accept", "application/json");
			clientlotes.Execute(RestClient.RequestMethod.POST);
			response = clientlotes.getResponse();
			Type listalotes = new TypeToken<List<Lote>>(){}.getType();
			ArrayList<Lote> lotes=gson.fromJson(response,listalotes);
			if(lotes.size()>0){
				if(!consultas.eliminarLotes().equalsIgnoreCase("OK"))respuestausuario+="Error eliminando lotes\n";
				for(Lote lote:lotes){
					String r3=consultas.nuevolote(lote);
					if(r3.equalsIgnoreCase("OK")){
						respuestausuario+="Lote "+lote.getNomlote()+" Creado\n";
					}else{
						respuestausuario+="Error creando lote:"+lote.getNomlote()+","+r3+"\n";
					}				
				}							
			}else{
				if(!consultas.eliminarLotes().equalsIgnoreCase("OK"))respuestausuario+="Error eliminando lotes\n";
				respuestausuario+="No hay lotes disponibles en el servidor";
			}
			respuestausuario+="\nSincronizacion finalizada";
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			respuestausuario="No se puede conectar al servicio de sincronizacion. Intentelo mas tarde.";
		} catch (Exception e) {
			e.printStackTrace();
			respuestausuario="No se puede conectar al servicio de sincronizacion. Intentelo mas tarde.";				
		}finally{
			return respuestausuario;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(activity instanceof MainActivity){
			((MainActivity) activity).setResultadoSincronizacionLotes(result);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	public SincronizacionLotes(Context context, Activity activity) {
		super();
		this.context = context;
		this.activity = activity;
	}	
}