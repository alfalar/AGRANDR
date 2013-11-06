package com.geographs.agrinsa;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.business.Coordenadas;
import com.geographs.agrinsa.business.Dominio;
import com.geographs.agrinsa.sqllite.DBHelper;

public class Fragmentcrearlote extends Fragment implements LocationListener {
	private LocationManager locationManager;
	private Location lastLocation;
	EditText textolatitud;
	EditText textolongitud;
	EditText textoagricultor;
	Spinner calificaciones;
	Button aceptar;
	Button agregar;
	ArrayList<Coordenadas> coordenadas;
	DBHelper consultas;
	ProgressDialog pdgps;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmentcrearlote, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		coordenadas = new ArrayList<Coordenadas>();
		textolatitud = (EditText) this.getView().findViewById(R.id.txtLatitud);
		textolongitud = (EditText) this.getView()
				.findViewById(R.id.txtLongitud);
		textoagricultor = (EditText) this.getView()
				.findViewById(R.id.txtagricultornlote);

		aceptar = (Button) this.getView().findViewById(
				R.id.btnGuardar);
		agregar = (Button) this.getView().findViewById(
				R.id.btnAddCoordenada);
		
		calificaciones = (Spinner) this.getView().findViewById(
				R.id.spinnercalificacion);
		locationManager = (LocationManager) this.getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		consultas = new DBHelper(this.getActivity());
		poblaSpinnerCalificacion();
		pdgps=ProgressDialog.show(this.getActivity(), "Por favor espere", "Adquiriendo posicion...", true);
	}

	public void poblaSpinnerCalificacion(){		
		List<Dominio> calificacionesdom=consultas.getDominio("DomCalifiLote");
		List<String> calificacionesstr=new ArrayList<String>(); 
		for(Dominio califica:calificacionesdom){
			calificacionesstr.add(califica.getCode()+"-"+ califica.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, calificacionesstr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			calificaciones.setAdapter(dataAdapter);
	}	
	@Override
	public void onLocationChanged(Location location) {
		pdgps.dismiss();
		this.agregar.setEnabled(true);
		lastLocation = location;
		textolatitud.setText(String.valueOf(location.getLatitude()));
		textolongitud.setText(String.valueOf(location.getLongitude()));
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	public void addCoordenada(View view) {
		if (lastLocation != null) {
			coordenadas.add(new Coordenadas(lastLocation.getLatitude(),
					lastLocation.getLongitude()));
			agregar.setText("Agregar coordenada("+coordenadas.size()+")");
			Toast.makeText(this.getActivity(), "Coordenada agregada",
					Toast.LENGTH_SHORT).show();
			this.aceptar.setEnabled(true);
		} else {
			Toast.makeText(this.getActivity(),
					"No hay coordenadas disponibles", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void guardarnuevolote(View view) {
		int calificacion = this.calificaciones.getSelectedItemPosition()+1;
		String agricultorlote=this.textoagricultor.getText().toString();
		if (consultas.guardarNuevoLote(coordenadas, calificacion,agricultorlote,"")) {
			new AlertDialog.Builder(this.getActivity())
					.setTitle("Mensaje de confirmacion")
					.setMessage("El nuevo lote se ha creado satisfactoriamente")
					.setNeutralButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			this.aceptar.setEnabled(false);
			this.coordenadas.clear();
			agregar.setText("Agregar coordenada("+coordenadas.size()+")");
		} else {
			new AlertDialog.Builder(this.getActivity())
					.setTitle("Mensaje de error")
					.setMessage(
							"Ha ocurrido un problema al crear el nuevo lote")
					.setNeutralButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}
	}

}
