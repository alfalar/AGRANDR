package com.geographs.agrinsa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geographs.agrinsa.business.Dominio;
import com.geographs.agrinsa.business.Lote;
import com.geographs.agrinsa.business.Visita;
import com.geographs.agrinsa.sqllite.DBHelper;

public class Fragmentvisita extends Fragment implements
		DatePickerDialog.OnDateSetListener {

	private TextView txtsiembra;
	private TextView txtcorte;
	private EditText txtproduccion;
	private Spinner lugarentrega;
	private TextView txtgerminacion;
	private ImageButton btfgermin;
	private int id_button_selected;
	private Spinner combocalificacion;
	private Spinner combotipovisita;
	private Spinner combolotes;
	private Spinner combotiposiembra;
	private Spinner comboporque;
	private EditText semestre;
	private EditText numvisita;
	private Button guardarvisita;
	private Lote loteseleccionado;
	DBHelper consultas;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragmentvisita, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		txtsiembra = (TextView) this.getActivity().findViewById(
				R.id.txtfsiembra);
		txtcorte = (TextView) this.getActivity().findViewById(
				R.id.txtfcorte);
		txtgerminacion = (TextView) this.getActivity().findViewById(
				R.id.txtfgermin);
		
		semestre=(EditText) this.getActivity().findViewById(
				R.id.txtsemestre);
		numvisita=(EditText) this.getActivity().findViewById(
				R.id.txtnumerovisita);
		btfgermin=(ImageButton) this.getActivity().findViewById(
				R.id.btnfgermin);
		guardarvisita=(Button) this.getActivity().findViewById(
				R.id.btnGuardarvisita);
		combotiposiembra=(Spinner) this.getActivity().findViewById(
				R.id.spinnertiposiembras);
		combocalificacion=(Spinner) this.getActivity().findViewById(
				R.id.spinnercalilote);
		combotipovisita=(Spinner) this.getActivity().findViewById(
				R.id.spinnertipovisita);
		comboporque=(Spinner) this.getActivity().findViewById(
				R.id.spinnerporques);
		txtproduccion=(EditText) this.getActivity().findViewById(
				R.id.txtproduccion);
		lugarentrega=(Spinner) this.getActivity().findViewById(
				R.id.spinnerlugarentrega);		
		consultas = new DBHelper(this.getActivity());
		poblaSpinnerLotes();
		poblaSpinnerTipoVisita();
		poblaSpinnerCalificacion();
		poblaSpinnerPorque();
		poblaSpinnerTipoSiembra();
		poblaSpinnerLugarentrega();
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		GregorianCalendar gc=new GregorianCalendar(year, month, day);
		Date fecha=gc.getTime();
		String fechastr=sdf.format(fecha);
		switch(id_button_selected){
    	case R.id.btnfcorte:
    		
    		txtcorte.setText(fechastr);
    		break;
    	case R.id.btnfgermin:
    		txtgerminacion.setText(fechastr);
    		break;
    	case R.id.btnfsiembra:
    		txtsiembra.setText(fechastr);
    		break;        		
	}
		
	}

	@SuppressWarnings("deprecation")
	public void abrePicker(View view) {
		switch(view.getId()){
        	case R.id.btnfcorte:
        		id_button_selected=R.id.btnfcorte;
        		break;
        	case R.id.btnfgermin:
        		id_button_selected=R.id.btnfgermin;
        		break;
        	case R.id.btnfsiembra:
        		id_button_selected=R.id.btnfsiembra;
        		break;        		
		}
		DatePickerFragment nf = new DatePickerFragment();
		nf.setFv(this);
		nf.show(this.getActivity().getSupportFragmentManager(),
				"datePicker");
	}
	/**
	 * Llena combo de lotes
	 */
	public void poblaSpinnerLotes(){
		combolotes = (Spinner) this.getActivity().findViewById(
				R.id.spinnerlistalotes);
		combolotes.setOnItemSelectedListener(new CustomOnLoteSelectedListener());
		List<Lote> lotes=consultas.getLotes(null);
		List<String> lotesStr=new ArrayList<String>();
		lotesStr.add("Seleccione un lote");
		if(lotes!=null){
			for(Lote lote:lotes){
				String tmp=lote.getLoteid()+"-"+lote.getNomlote()+"-"+lote.getAgricultor();
				lotesStr.add(tmp);
			}	
		}else{
			lotesStr.add("No se encontraron lotes");
		}
		
				
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, lotesStr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			combolotes.setAdapter(dataAdapter);
	}
	/**
	 *  Llena combo de Tipos de visita
	 */
	public void poblaSpinnerTipoVisita(){		
		List<Dominio> tipovisitadom=consultas.getDominio("DomTipoVisita");
		List<String> tipovisitastr=new ArrayList<String>(); 
		for(Dominio tipovisita:tipovisitadom){
			tipovisitastr.add(tipovisita.getCode()+"-"+ tipovisita.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, tipovisitastr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			combotipovisita.setAdapter(dataAdapter);
	}	
	/**
	 *  Llena combo de Calificacion del lote
	 */
	public void poblaSpinnerCalificacion(){		
		List<Dominio> calificacionesdom=consultas.getDominio("DomCalifiLote");
		List<String> calificacionesstr=new ArrayList<String>(); 
		for(Dominio califica:calificacionesdom){
			calificacionesstr.add(califica.getCode()+"-"+ califica.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, calificacionesstr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			combocalificacion.setAdapter(dataAdapter);
	}	
	/**
	 *  Llena combo de Porque califica
	 */
	public void poblaSpinnerPorque(){		
		List<Dominio> porquedom=consultas.getDominio("DomPQCalifica");
		List<String> porquestr=new ArrayList<String>(); 
		for(Dominio porque:porquedom){
			porquestr.add(porque.getCode()+"-"+ porque.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, porquestr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			comboporque.setAdapter(dataAdapter);
	}	
	/**
	 *  Llena combo de Tipo de siembra
	 */
	public void poblaSpinnerTipoSiembra(){		
		List<Dominio> tiposiembradom=consultas.getDominio("DomSiembra");
		List<String> tiposiembrastr=new ArrayList<String>(); 
		for(Dominio ts:tiposiembradom){
			tiposiembrastr.add(ts.getCode()+"-"+ ts.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, tiposiembrastr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			combotiposiembra.setAdapter(dataAdapter);
	}	
	/**
	 *  Llena combo de lugar de entrega
	 */
	public void poblaSpinnerLugarentrega(){		
		List<Dominio> lugarentregadom=consultas.getDominio("DomLugarEntrega");
		List<String> lugarentregastr=new ArrayList<String>(); 
		for(Dominio lugar:lugarentregadom){
			lugarentregastr.add(lugar.getCode()+"-"+ lugar.getValue());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, lugarentregastr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			lugarentrega.setAdapter(dataAdapter);
	}	
	
	/**
	 * 
	 * @param view
	 */
	public void guardarvisita(View view) {
		String mensajeerror="";
		/*if(txtsiembra.getText().length()==0){
			mensajeerror+="La fecha de siembra es obligatoria\n";
		}
		if(txtgerminacion.getText().length()==0){
			mensajeerror+="La fecha de germinacion es obligatoria\n";
		}
		if(txtcorte.getText().length()==0){
			mensajeerror+="La fecha de corta es obligatoria\n";
		}
		*/
		double produccion=0;
		if(txtproduccion.getText().toString().length()>0){
			produccion=new Double(txtproduccion.getText().toString());
		}
		int calificacionv = this.combocalificacion.getSelectedItemPosition()+1;
		int tipovisita = this.combotipovisita.getSelectedItemPosition()+1;
		int tiposiembra=this.combotiposiembra.getSelectedItemPosition()+1;
		int porque=this.comboporque.getSelectedItemPosition()+1;
		int lugarentrega=this.lugarentrega.getSelectedItemPosition()+1;
		if(mensajeerror.length()>0){
			Toast toast=Toast.makeText(this.getActivity(), mensajeerror,
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Visita visita=new Visita();
			visita.setLoteid(loteseleccionado.getLoteid());
			visita.setSemestre(loteseleccionado.getSemestre());
			visita.setNumvisita(loteseleccionado.getNumvisita());
			visita.setFechasiembra(txtsiembra.getText().toString());
			visita.setFechagerminacion(txtgerminacion.getText().toString());
			visita.setFechacorte(txtcorte.getText().toString());
			visita.setCalificacion(calificacionv);
			visita.setTipovisita(tipovisita);
			visita.setTiposiembra(tiposiembra);
			visita.setPorque(porque);
			visita.setLugarentrega(lugarentrega);
			visita.setProduccion(produccion);
			java.util.Date date = new java.util.Date(); 
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
			String fecha = sdf.format(date);
			visita.setFechavisita(fecha);
			String resultado=consultas.nuevavisita(visita);
			if(resultado.equalsIgnoreCase("OK")){
				resultado=consultas.actualizaVisita(loteseleccionado.getLoteid(), 
						loteseleccionado.getNumvisita()+1);
				new AlertDialog.Builder(this.getActivity())
				.setTitle("Mensaje de confirmacion")
				.setMessage("La nueva visita se ha creado satisfactoriamente")
				.setNeutralButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
				clear();
			}else{
				new AlertDialog.Builder(this.getActivity())
				.setTitle("Mensaje de error")
				.setMessage("La nueva visita no se ha creado:"+resultado)
				.setNeutralButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
			}
		}
		
	}

	public void clear(){
		semestre.setText("");
		txtsiembra.setText("");
		txtgerminacion.setText("");
		txtcorte.setText("");
		numvisita.setText("");
		btfgermin.setEnabled(true);		
		combotiposiembra.setSelection(0);
		combocalificacion.setSelection(0);
		comboporque.setSelection(0);
		combotipovisita.setSelection(0);
		txtproduccion.setText("");
		lugarentrega.setSelection(0);
		combolotes.setSelection(0);		
	}

	private class CustomOnLoteSelectedListener implements OnItemSelectedListener {
		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			String lseleccionado=parent.getItemAtPosition(pos).toString();
			if(!lseleccionado.equalsIgnoreCase("No se encontraron lotes")){
				if(lseleccionado.equalsIgnoreCase("Seleccione un lote")){
					semestre.setText("");
					txtsiembra.setText("");
					txtgerminacion.setText("");
					txtcorte.setText("");
					numvisita.setText("");
					btfgermin.setEnabled(true);		
					combotiposiembra.setSelection(0);
					combocalificacion.setSelection(0);
					comboporque.setSelection(0);
					combotipovisita.setSelection(0);
					txtproduccion.setText("");
					lugarentrega.setSelection(0);
					guardarvisita.setEnabled(false);
				}else{
					String idseleccionado=lseleccionado.split("-")[0];
					ArrayList<Lote> lotes=consultas.getLotes(idseleccionado);
					for(Lote lote: lotes){
						loteseleccionado=lote;
						if(lote.getSemestre()>0){
							String[] semestres = getResources().getStringArray(R.array.listasemestre);
							semestre.setText(semestres[lote.getSemestre()-1]);
						}else{
							semestre.setText("");
						}
						if(!lote.getFecsiembra().equalsIgnoreCase("null") && !lote.getFecsiembra().equalsIgnoreCase("")){						
							txtsiembra.setText(lote.getFecsiembra());
						}else{
							txtsiembra.setText("");
						}
						if(!lote.getFeccorte().equalsIgnoreCase("null") && !lote.getFeccorte().equalsIgnoreCase("")){
							txtcorte.setText(lote.getFeccorte());
						}else{
							txtcorte.setText("");
						}
						if(!lote.getFecgerminacion().equalsIgnoreCase("null") && !lote.getFecgerminacion().equalsIgnoreCase("")){
							//SI YA TIENE UNA FECHA DEBE QUEDAR DESHABILITADO
							txtgerminacion.setText(lote.getFecgerminacion());
							btfgermin.setEnabled(false);
						}else{
							txtgerminacion.setText("");
							btfgermin.setEnabled(true);
						}
						numvisita.setText(String.valueOf(lote.getNumvisita()));
						if(!lote.getSiembra().equalsIgnoreCase("null")){
							combotiposiembra.setSelection(Integer.parseInt(lote.getSiembra())-1);
						}else{
							combotiposiembra.setSelection(0);
						}
						combocalificacion.setSelection(0);
						comboporque.setSelection(0);
						combotipovisita.setSelection(0);
						txtproduccion.setText("");
						lugarentrega.setSelection(0);
						guardarvisita.setEnabled(true);
					}

				}
			}else{	
				loteseleccionado=null;
				guardarvisita.setEnabled(false);
			}
			
		  }
		 
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  } 

	}
}
