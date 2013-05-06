package com.geographs.agrinsa.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.geographs.agrinsa.business.Constantes;
import com.geographs.agrinsa.business.Coordenadas;
import com.geographs.agrinsa.business.Dominio;
import com.geographs.agrinsa.business.Lote;
import com.geographs.agrinsa.business.NuevosLotes;
import com.geographs.agrinsa.business.Usuarios;
import com.geographs.agrinsa.business.Visita;

public class DBHelper extends SQLiteOpenHelper {

	// Define the version and database file name
	private static final String DB_NAME = "agrinsa.db";
	private static final int DB_VERSION = 2;
	private int usuarioid_actual;
	private String username_actual;
	private String primernombre_actual;
	private String primerapellido_actual;

	// Use a static class to defined the data structure
	// This will come in very handy if you using Agile
	// As your development model
	private static class UsuariosTable {
		private static final String NAME = "usuarios";
		private static final String COL_ID = "usuario_id";
		private static final String COL_USERNAME = "usuario";
		private static final String COL_PASSWORD = "password";
		private static final String P_NOMBRE = "primer_nombre";
		private static final String S_NOMBRE = "segundo_nombre";
		private static final String P_APELLIDO = "primer_apellido";
		private static final String S_APELLIDO = "segundo_apellido";
		private static final String HABILITADO = "habilitado";
	}

	private static class ConsultasTable {
		private static final String NAME = "consultas";
		private static final String OPCION = "opcion";
		private static final String VALOR = "valor";
	}

	private static class NuevoLoteTable {
		private static final String NAME = "nuevolote";
		private static final String LOTE_ID = "lotenuevoid";
		private static final String VISITA = "numvisita";
		private static final String CALIFICACION = "califilote";
		private static final String USUARIO_ID = "usuarioid";
	}

	private static class CoordenadasTable {
		private static final String NAME = "coordenadaslotenuevo";
		private static final String LOTE_ID = "lotenuevoid";
		private static final String X = "x";
		private static final String Y = "y";
	}

	private static class LoteTable {
		private static final String NAME = "lotes";
		private static final String LOTE_ID = "loteid";
		private static final String USUARIO_ID = "usuarioid";
		private static final String AGRICULTOR = "agricultor";
		private static final String NOMBRE_LOTE = "nomlote";
		private static final String SEMESTRE = "semestre";
		private static final String FECHASIEMBRA = "fecsiembra";
		private static final String FECHAGERMINACION = "fecgerminacion";
		private static final String FECHACORTE = "feccorte";
		private static final String SIEMBRA = "siembra";
		private static final String NUMVISITA = "numvisita";
	}

	private static class VisitaTerrenoTable {
		private static final String NAME = "visitatrerreno";
		private static final String LOTE_ID = "loteid";
		private static final String SEMESTRE = "semestre";
		private static final String NUMVISITA = "numvisita";
		private static final String FECSIEMBRA = "fecsiembra";
		private static final String FECGERMINACION = "fecgerminacion";
		private static final String FECCORTA = "feccorte";
		private static final String TIPOVISITA = "tipovisita";
		private static final String CALIFICACION = "califilote";
		private static final String PORQUE = "porque";
		private static final String TIPOSIEMBRA = "tiposiembra";
		private static final String PRODUCCION = "produccion";
		private static final String LUGARENTREGA = "lugarentrega";
		private static final String FECHAVISITA = "fechavisita";
	}

	private static class DominiosTable {
		private static final String NAME = "dominios";
		private static final String NOMBREDOMINIO = "dominio";
		private static final String CODE = "code";
		private static final String VALUE = "value";		
	}
	
	private SQLiteDatabase db;

	// Constructor to simplify Business logic access to the repository
	public DBHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
		// Android will look for the database defined by DB_NAME
		// And if not found will invoke your onCreate method
		this.db = this.getWritableDatabase();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// CREACION DE LA TABLA DE USUARIOS
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
						UsuariosTable.NAME, UsuariosTable.COL_ID,
						UsuariosTable.COL_USERNAME, UsuariosTable.COL_PASSWORD,
						UsuariosTable.P_NOMBRE, UsuariosTable.S_NOMBRE,
						UsuariosTable.P_APELLIDO, UsuariosTable.S_APELLIDO,
						UsuariosTable.HABILITADO));
		// CREACION E INSERCION DE LA TABLA DE CONSTANTES
		db.execSQL(String.format("CREATE TABLE %s (%s TEXT, %s TEXT)",
				ConsultasTable.NAME, ConsultasTable.OPCION,
				ConsultasTable.VALOR));
		db.execSQL("INSERT or replace INTO " + ConsultasTable.NAME + " ("
				+ ConsultasTable.OPCION + ", " + ConsultasTable.VALOR
				+ ") VALUES(" + "'IPWS','http://192.168.0.2:8080/')");
		// CREACION DE LA TABLA DE NUEVOS LOTES
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s INTEGER,%s INTEGER)",
						NuevoLoteTable.NAME, NuevoLoteTable.LOTE_ID,
						NuevoLoteTable.VISITA, NuevoLoteTable.CALIFICACION,
						NuevoLoteTable.USUARIO_ID));
		// CREACION DE LA TABLA DE COORDENADAS
		db.execSQL(String.format(
				"CREATE TABLE %s (%s INTEGER, %s REAL, %s REAL)",
				CoordenadasTable.NAME, CoordenadasTable.LOTE_ID,
				CoordenadasTable.X, CoordenadasTable.Y));
		// CREACION DE LA TABLA DE LOTES
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
						LoteTable.NAME, LoteTable.LOTE_ID,
						LoteTable.USUARIO_ID, LoteTable.AGRICULTOR,
						LoteTable.NOMBRE_LOTE, LoteTable.SEMESTRE,
						LoteTable.FECHASIEMBRA, LoteTable.FECHAGERMINACION,
						LoteTable.FECHACORTE, LoteTable.SIEMBRA,
						LoteTable.NUMVISITA));
		// CREACION DE LA TABLA DE VISITAS
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, "
						+ "%s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER,"
						+ "%s REAL, %s INTEGER, %s TEXT)",
						VisitaTerrenoTable.NAME, VisitaTerrenoTable.LOTE_ID,
						VisitaTerrenoTable.SEMESTRE,
						VisitaTerrenoTable.NUMVISITA,
						VisitaTerrenoTable.FECSIEMBRA,
						VisitaTerrenoTable.FECGERMINACION,
						VisitaTerrenoTable.FECCORTA,
						VisitaTerrenoTable.TIPOVISITA,
						VisitaTerrenoTable.CALIFICACION,
						VisitaTerrenoTable.PORQUE,
						VisitaTerrenoTable.TIPOSIEMBRA,
						VisitaTerrenoTable.PRODUCCION,
						VisitaTerrenoTable.LUGARENTREGA,
						VisitaTerrenoTable.FECHAVISITA));
		// CREACION DE LA TABLA DE DOMINIOS
		db.execSQL(String
				.format("CREATE TABLE %s ( %s TEXT, %s INTEGER, %s TEXT)",
						DominiosTable.NAME, DominiosTable.NOMBREDOMINIO,DominiosTable.CODE,
						DominiosTable.VALUE));
		
	}

	public boolean validarUsuario(String usuario, String contrasena) {
		String where = UsuariosTable.COL_USERNAME + "='" + usuario + "' "
				+ "and " + UsuariosTable.COL_PASSWORD + "='" + contrasena + "'";
		Cursor cursor = this.db.query(UsuariosTable.NAME, new String[] {
				UsuariosTable.COL_ID, UsuariosTable.COL_USERNAME,
				UsuariosTable.P_NOMBRE, UsuariosTable.P_APELLIDO }, where,
				null, null, null, null);

		if (cursor.moveToFirst()) {
			this.usuarioid_actual = cursor.getInt(0);
			this.username_actual = cursor.getString(1);
			this.primernombre_actual = cursor.getString(2);
			this.primerapellido_actual = cursor.getString(3);
			cursor.close();
			return true;
		} else {
			return false;
		}

	}

	public String crearUsuario(Usuarios usuario) {
		try {
			int val = usuario.isHabilitado() ? 1 : 0;
			String sql = "INSERT OR REPLACE INTO " + UsuariosTable.NAME + " ("
					+ UsuariosTable.COL_ID + ", " + UsuariosTable.COL_USERNAME
					+ ", " + UsuariosTable.COL_PASSWORD + ", "
					+ UsuariosTable.P_NOMBRE + ", " + UsuariosTable.S_NOMBRE
					+ ", " + UsuariosTable.P_APELLIDO + ", "
					+ UsuariosTable.S_APELLIDO + ", "
					+ UsuariosTable.HABILITADO + ") VALUES("
					+ usuario.getUsuario_id() + ",'" + usuario.getUsuario()
					+ "','" + usuario.getPassword() + "','"
					+ usuario.getPrimerNombre() + "','"
					+ usuario.getSegundoNombre() + "','"
					+ usuario.getPrimerApellido() + "','"
					+ usuario.getSegundoApellido() + "'," + val + ")";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}

	public String eliminarUsuarios() {
		try {
			db.delete(UsuariosTable.NAME, null, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	/*
	 * Retorna el valor de una constante de acuerdo a una opcion
	 */
	public String getConstante(String opcion) {
		String where = ConsultasTable.OPCION + "='" + opcion + "'";
		Cursor cursor = this.db.query(ConsultasTable.NAME,
				new String[] { ConsultasTable.VALOR }, where, null, null, null,
				null);

		if (cursor.moveToFirst()) {
			String constante = cursor.getString(0);
			cursor.close();
			return constante;
		} else {
			return "ND";
		}
	}

	/**
	 * 
	 * @param llave
	 * @param valor
	 * @return
	 */
	public String setConstante(String llave, String valor) {
		try {
			ContentValues valores = new ContentValues();
			valores.put(ConsultasTable.VALOR, valor);
			String where = ConsultasTable.OPCION + "='" + llave + "'";
			db.update(ConsultasTable.NAME, valores, where, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public int getMaximoIdLote() {
		final String MY_QUERY = "SELECT MAX(" + NuevoLoteTable.LOTE_ID
				+ ") FROM " + NuevoLoteTable.NAME;
		Cursor cur = db.rawQuery(MY_QUERY, null);
		cur.moveToFirst();
		int ID = cur.getInt(0);
		cur.close();
		return ID;
	}

	/**
	 * @Description Almacena un nuevo lote en las 2 tablas
	 * @param coordenadas
	 * @param calificacion
	 * @param loteid
	 * @return
	 */
	public boolean guardarNuevoLote(ArrayList<Coordenadas> coordenadas,
			int calificacion) {
		try {
			String sql = "INSERT INTO " + NuevoLoteTable.NAME + " ("
					+ NuevoLoteTable.CALIFICACION + ", "
					+ NuevoLoteTable.USUARIO_ID + ", " + NuevoLoteTable.VISITA
					+ ") VALUES(" + calificacion + "," + Constantes.usuarioid_actual
					+ ",0" + ")";
			db.execSQL(sql);
			int maxid = getMaximoIdLote();
			Log.d("MAXIMO ID LOTE:", String.valueOf(maxid));
			for (Coordenadas c : coordenadas) {
				sql = "INSERT INTO " + CoordenadasTable.NAME + " ("
						+ CoordenadasTable.LOTE_ID + ", " + CoordenadasTable.X
						+ ", " + CoordenadasTable.Y + ") VALUES(" + maxid + ","
						+ c.getLongitud() + "," + c.getLatitud() + ")";
				db.execSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Retorna la lista de lotes creados con sus coordenadas
	 * 
	 * @return
	 */
	public ArrayList<NuevosLotes> getListadoNuevosLotes() {
		ArrayList<NuevosLotes> nuevoslotes = new ArrayList<NuevosLotes>();
		Cursor cursor = this.db.query(NuevoLoteTable.NAME, new String[] {
				NuevoLoteTable.LOTE_ID, NuevoLoteTable.CALIFICACION,
				NuevoLoteTable.USUARIO_ID }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				NuevosLotes nuevolote = new NuevosLotes();
				nuevolote.setLotenuevoid(cursor.getInt(0));
				nuevolote.setCalifilote(cursor.getInt(1));
				nuevolote.setUsuarioid(cursor.getInt(2));
				ArrayList<Coordenadas> coordenadas = getCoordenadasLote(cursor
						.getInt(0));
				nuevolote.setCoordenadas(coordenadas);
				nuevoslotes.add(nuevolote);
			} while (cursor.moveToNext());
		} else {
			return null;
		}
		return nuevoslotes;
	}

	/**
	 * 
	 * @param loteid
	 * @return
	 */
	private ArrayList<Coordenadas> getCoordenadasLote(int loteid) {
		ArrayList<Coordenadas> coordenadas = new ArrayList<Coordenadas>();
		String where = CoordenadasTable.LOTE_ID + "=" + loteid;
		Cursor cursor = this.db.query(CoordenadasTable.NAME, new String[] {
				CoordenadasTable.X, CoordenadasTable.Y }, where, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				Coordenadas coordenada = new Coordenadas(cursor.getDouble(1),
						cursor.getDouble(0));
				coordenadas.add(coordenada);
			} while (cursor.moveToNext());
		} else {
			return null;
		}
		return coordenadas;
	}

	/**
	 * Borra todos los registros de la tabla de lotes nuevos y de la tabla de
	 * coordenadas
	 * 
	 * @return
	 */
	public String deleteLotes() {
		try {
			int deletes = this.db.delete(NuevoLoteTable.NAME, "1", null);
			int deletesc = this.db.delete(CoordenadasTable.NAME, "1", null);
			Log.d("deletelotes", "Borrados " + deletes + " lotes con "
					+ deletesc + "coordenadas");
			return "OK";
		} catch (Exception e) {
			Log.d("errordeleteslotes", e.getMessage());
			return e.getMessage();
		}
	}

	/**
	 * Creacion de un lote que se trae desde el servidor
	 * 
	 * @param lote
	 * @return
	 */
	public String nuevolote(Lote lote) {
		try {
			String sql = "INSERT OR REPLACE INTO " + LoteTable.NAME + " ("
					+ LoteTable.LOTE_ID + ", " + LoteTable.USUARIO_ID + ", "
					+ LoteTable.AGRICULTOR + ", " + LoteTable.NOMBRE_LOTE
					+ ", " + LoteTable.SEMESTRE + ", " + LoteTable.FECHASIEMBRA
					+ ", " + LoteTable.FECHAGERMINACION + ", "
					+ LoteTable.FECHACORTE + ", " + LoteTable.SIEMBRA + ", "
					+ LoteTable.NUMVISITA + ") VALUES(" + lote.getLoteid()
					+ "," + lote.getUsuarioid() + ",'" + lote.getAgricultor()
					+ "','" + lote.getNomlote() + "','" + lote.getSemestre()
					+ "','" + lote.getFecsiembra() + "','"
					+ lote.getFecgerminacion() + "','" + lote.getFeccorte()
					+ "','" + lote.getSiembra() + "'," + lote.getNumvisita()
					+ ")";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}

	/**
	 * Elimina los lotes traidos desde el servidor
	 * 
	 * @return
	 */
	public String eliminarLotes() {
		try {
			db.delete(LoteTable.NAME, null, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	/**
	 * Retorna todos los lotes
	 * 
	 * @param loteid
	 * @return
	 */
	public ArrayList<Lote> getLotes(String loteid) {
		String where = null;
		if (loteid != null)
			where = LoteTable.LOTE_ID + "=" + loteid;
		ArrayList<Lote> lotes = new ArrayList<Lote>();
		Cursor cursor = this.db.query(LoteTable.NAME, new String[] {
				LoteTable.LOTE_ID, LoteTable.NOMBRE_LOTE, LoteTable.AGRICULTOR,
				LoteTable.FECHACORTE, LoteTable.FECHAGERMINACION,
				LoteTable.FECHASIEMBRA, LoteTable.SEMESTRE, LoteTable.SIEMBRA,
				LoteTable.NUMVISITA }, where, null, null, null, LoteTable.AGRICULTOR);
		if (cursor.moveToFirst()) {
			do {
				Lote lote = new Lote();
				lote.setLoteid(cursor.getInt(0));
				lote.setNomlote(cursor.getString(1));
				lote.setAgricultor(cursor.getString(2));
				lote.setFeccorte(cursor.getString(3));
				lote.setFecgerminacion(cursor.getString(4));
				lote.setFecsiembra(cursor.getString(5));
				lote.setSemestre(cursor.getInt(6));
				lote.setSiembra(cursor.getString(7));
				lote.setNumvisita(cursor.getInt(8));
				lotes.add(lote);
			} while (cursor.moveToNext());
			return lotes;
		} else {
			return null;
		}
	}

	public String actualizaVisita(int loteid, int newnumvisita) {
		try {
			String where = LoteTable.LOTE_ID + "=" + loteid;
			ContentValues valores = new ContentValues();
			valores.put(LoteTable.NUMVISITA, newnumvisita);
			this.db.update(LoteTable.NAME, valores, where, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * Crea una nueva visita
	 * 
	 * @param visita
	 * @return
	 */
	public String nuevavisita(Visita visita) {
		try {
			String sql = "INSERT OR REPLACE INTO " + VisitaTerrenoTable.NAME
					+ " (" + VisitaTerrenoTable.LOTE_ID + ", "
					+ VisitaTerrenoTable.SEMESTRE + ", "
					+ VisitaTerrenoTable.NUMVISITA + ", "
					+ VisitaTerrenoTable.FECSIEMBRA + ", "
					+ VisitaTerrenoTable.FECGERMINACION + ", "
					+ VisitaTerrenoTable.FECCORTA + ", "
					+ VisitaTerrenoTable.TIPOVISITA + ", "
					+ VisitaTerrenoTable.CALIFICACION + ", "
					+ VisitaTerrenoTable.PORQUE + ", "
					+ VisitaTerrenoTable.TIPOSIEMBRA + ", "
					+ VisitaTerrenoTable.PRODUCCION + ", "
					+ VisitaTerrenoTable.LUGARENTREGA + ", "
					+ VisitaTerrenoTable.FECHAVISITA + ") VALUES("
					+ visita.getLoteid() + "," + visita.getSemestre() + ","
					+ visita.getNumvisita() + ",'" + visita.getFechasiembra()
					+ "','" + visita.getFechagerminacion() + "','"
					+ visita.getFechacorte() + "'," + visita.getTipovisita()
					+ "," + visita.getCalificacion() + "," + visita.getPorque()
					+ "," + visita.getTiposiembra() + ","
					+ visita.getProduccion() + "," + visita.getLugarentrega()
					+ ",'" + visita.getFechavisita() + "')";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}

	/**
	 * Retorna todas las visitas
	 * 
	 * @return
	 */
	public ArrayList<Visita> getVisitas() {
		ArrayList<Visita> visitas = new ArrayList<Visita>();
		Cursor cursor = this.db.query(VisitaTerrenoTable.NAME, new String[] {
				VisitaTerrenoTable.LOTE_ID, VisitaTerrenoTable.SEMESTRE,
				VisitaTerrenoTable.NUMVISITA, VisitaTerrenoTable.FECSIEMBRA,
				VisitaTerrenoTable.FECGERMINACION, VisitaTerrenoTable.FECCORTA,
				VisitaTerrenoTable.TIPOVISITA, VisitaTerrenoTable.CALIFICACION,
				VisitaTerrenoTable.PORQUE, VisitaTerrenoTable.TIPOSIEMBRA,
				VisitaTerrenoTable.PRODUCCION, VisitaTerrenoTable.LUGARENTREGA,
				VisitaTerrenoTable.FECHAVISITA }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Visita visita = new Visita();
				visita.setLoteid(cursor.getInt(0));
				visita.setSemestre(cursor.getInt(1));
				visita.setNumvisita(cursor.getInt(2));
				visita.setFechasiembra(cursor.getString(3));
				visita.setFechagerminacion(cursor.getString(4));
				visita.setFechacorte(cursor.getString(5));
				visita.setTipovisita(cursor.getInt(6));
				visita.setCalificacion(cursor.getInt(7));
				visita.setPorque(cursor.getInt(8));
				visita.setTiposiembra(cursor.getInt(9));
				visita.setProduccion(cursor.getInt(10));
				visita.setLugarentrega(cursor.getInt(11));
				visita.setFechavisita(cursor.getString(12));
				visitas.add(visita);
			} while (cursor.moveToNext());
			return visitas;
		} else {
			return null;
		}
	}

	/**
	 * Elimina todas las visitas
	 * 
	 * @return
	 */
	public String eliminarVisitas() {
		try {
			db.delete(VisitaTerrenoTable.NAME, null, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	/**
	 * 
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// Later when you change the DB_VERSION
		// This code will be invoked to bring your database
		// Upto the correct specification
	}

	public int getUsuarioid_actual() {
		return usuarioid_actual;
	}

	/**
	 * Crea un dominio en la tabla de dominios
	 * @param dominio
	 * @return
	 */
	public String creardominio(Dominio dominio) {
		try {			
			String sql = "INSERT OR REPLACE INTO " + DominiosTable.NAME + " ("
					+ DominiosTable.NOMBREDOMINIO + ", " + DominiosTable.CODE
					+ ", " + DominiosTable.VALUE + ") VALUES('"
					+ dominio.getDominio() + "'," + dominio.getCode()
					+ ",'" + dominio.getValue()  + "')";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}
	
	public String eliminarDominios() {
		try {
			db.delete(DominiosTable.NAME, null, null);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}

	}
	
	/**
	 * Retorna los valores de un dominio
	 * @param dominiov
	 * @return
	 */
	public List<Dominio> getDominio(String dominiov) {
		String where = DominiosTable.NOMBREDOMINIO + "='" + dominiov + "'";
		List<Dominio> dominios = new ArrayList<Dominio>();
		Cursor cursor = this.db.query(DominiosTable.NAME, new String[] {
				DominiosTable.NOMBREDOMINIO, DominiosTable.CODE,
				DominiosTable.VALUE }, where, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Dominio dominio= new Dominio();
				dominio.setDominio(cursor.getString(0));
				dominio.setCode(cursor.getInt(1));
				dominio.setValue(cursor.getString(2));
				dominios.add(dominio);
			} while (cursor.moveToNext());
		} else {
			return null;
		}
		return dominios;
	}	
}
