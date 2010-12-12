package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * http://www.softwarepassion.com/android-series-using-gps-data/
 * 
 * @author Andrew Yee
 *
 */
public class LocationUtil implements LocationListener {

	double latitude;
	double longitude;
	LocationManager locManager;
	
	public LocationUtil(Activity activity) {
		locManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		this.latitude = arg0.getLatitude();
		this.longitude = arg0.getLongitude();
		
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
	
	public Tag createTag() {
		return new Tag(latitude, longitude);
	}

}
