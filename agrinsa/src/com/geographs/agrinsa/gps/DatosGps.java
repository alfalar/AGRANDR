package com.geographs.agrinsa.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class DatosGps extends Activity implements LocationListener {	
	private LocationManager locationManager;
	private Location location;
	public DatosGps() {
		super();
		 // Get the location manager
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    // Initialize the location fields
	    //if (location != null) {	      
	    //  onLocationChanged(location);
	    //} else {
	    	//MENSAJE DE NO GPS
	    //}
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location=location;
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

	public Location getLocation() {
		return location;
	}

}
