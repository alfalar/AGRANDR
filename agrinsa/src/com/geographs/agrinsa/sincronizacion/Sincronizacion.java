package com.geographs.agrinsa.sincronizacion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.HttpHostConnectException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.geographs.agrinsa.LoginActivity;
import com.geographs.agrinsa.RestClient;
import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.business.Dominio;
import com.geographs.agrinsa.business.NuevosLotes;
import com.geographs.agrinsa.business.Usuarios;
import com.geographs.agrinsa.business.Visita;
import com.geographs.agrinsa.sqllite.DBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Sincronizacion extends AsyncTask<Void, Void, String> {
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
			/***************** SINCRONIZACION DE USUARIOS **************************/
			RestClient clientusuarios = new RestClient(ipws+"usuarios");
			clientusuarios.AddHeader("Accept", "application/json");
			clientusuarios.Execute(RestClient.RequestMethod.GET);
			response = clientusuarios.getResponse();							
			Type listatipousuarios = new TypeToken<List<Usuarios>>(){}.getType();
			ArrayList<Usuarios> usuarios=gson.fromJson(response,listatipousuarios);
			if(usuarios.size()>0){
				if(!consultas.eliminarUsuarios().equalsIgnoreCase("OK"))respuestausuario+="Error eliminando usuarios\n";
				int i=0;
				for(Usuarios usuario:usuarios){
					//Log.d("Agrinsa", usuario.getUsuario());
					String r1=consultas.crearUsuario(usuario);
					if(r1.equalsIgnoreCase("OK")){
						i++;
					}else{
						respuestausuario+="Error creando usuario:"+usuario.getUsuario()+":"+r1+"\n";
					}
				}
				respuestausuario+=i+" usuarios creados localmente\n";
			}else{
				if(!consultas.eliminarUsuarios().equalsIgnoreCase("OK"))respuestausuario+="Error eliminando usuarios\n";
				respuestausuario+="No existen usuarios en el servidor\n";
			}
			/***************** SINCRONIZACION DE LOTES NUEVOS CREADOS**************************/			
			ArrayList<NuevosLotes> nuevoslotes=consultas.getListadoNuevosLotes();
			if(nuevoslotes!=null){
				
				String nlotesjson=gson.toJson(nuevoslotes);
				RestClient clientnuevos = new RestClient(ipws+"nuevolote");
				clientnuevos.AddParam("nuevoslotes", nlotesjson);
				clientnuevos.AddHeader("Accept", "text/plain");
				clientnuevos.Execute(RestClient.RequestMethod.POST);
				response = clientnuevos.getResponse();							
				if(response.equalsIgnoreCase("OK\n")){
					respuestausuario+=nuevoslotes.size()+" puntos de lotes sincronizados en el servidor\n";
					//BORRAR REGISTROS DE LA TABLA DE NUEVOS LOTES				
					String r2=consultas.deleteLotes();
					if(r2.equalsIgnoreCase("OK")){
						respuestausuario+="Puntos de lotes ya sincronizados eliminados localmente\n";
					}else{
						respuestausuario+="Error eliminando puntos de lotes ya sincronizados:"+r2+"\n";
					}
				}				
			}else{
				respuestausuario+="No hay puntos de lotes para sincronizar\n";
			}
			/***************** SINCRONIZACION DE VISITAS NUEVAS**************************/	
			ArrayList<Visita> visitas=consultas.getVisitas();
			if(visitas!=null){				
				String nvisitasjson=gson.toJson(visitas);
				RestClient clientvisitas = new RestClient(ipws+"visitas/setVisitas");
				clientvisitas.AddParam("visitas", nvisitasjson);
				clientvisitas.AddHeader("Accept", "text/plain");
				clientvisitas.Execute(RestClient.RequestMethod.POST);
				response = clientvisitas.getResponse();							
				if(response.equalsIgnoreCase("OK\n")){
					respuestausuario+=visitas.size()+" visitas sincronizadas en el servidor\n";
					//BORRAR REGISTROS DE LA TABLA VISITAS			
					String r3=consultas.eliminarVisitas();
					if(r3.equalsIgnoreCase("OK")){
						respuestausuario+="Visitas ya sincronizados eliminadas localmente\n";
					}else{
						respuestausuario+="Error eliminando visitas ya sincronizadas:"+r3+"\n";
					}
				}				
			}else{
				respuestausuario+="No hay visitas para sincronizar\n";
			}	
			
			
			/***************** SINCRONIZACION DE DOMINIOS**************************/			
			RestClient clientdominios= new RestClient(ipws+"dominio");			
			clientdominios.AddHeader("Accept", "application/json");
			clientdominios.Execute(RestClient.RequestMethod.POST);
			response = clientdominios.getResponse();							
			Type listadominio = new TypeToken<List<Dominio>>(){}.getType();
			List<Dominio> dominio=gson.fromJson(response,listadominio);
			if(dominio.size()>0){
				if(!consultas.eliminarDominios().equalsIgnoreCase("OK"))respuestausuario+="Error eliminando dominios\n";
				int i=0;
				for(Dominio dom: dominio){
					String r1=consultas.creardominio(dom);
					if(r1.equalsIgnoreCase("OK")){
						i++;
					}else{
						respuestausuario+="Error creando dominio:"+dom.getDominio()+":"+r1+"\n";
					}					
				}				
				respuestausuario+= i+" dominios creados\n";
			}else{
				respuestausuario+="No se pudieron retornar los valores de los dominios\n";
			}			
				
			respuestausuario+="Sincronizacion finalizada";
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
		if(activity instanceof LoginActivity){
			((LoginActivity) activity).setResultadoSincronizacion(result);
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

	public Sincronizacion(Context context, Activity activity) {
		super();
		this.context = context;
		this.activity = activity;
	}	
}