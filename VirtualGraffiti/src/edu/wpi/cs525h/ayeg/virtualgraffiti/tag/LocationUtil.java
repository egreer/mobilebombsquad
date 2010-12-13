package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import java.text.DecimalFormat;

import edu.wpi.cs525h.ayeg.virtualgraffiti.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * http://www.softwarepassion.com/android-series-using-gps-data/
 * 
 * @author Andrew Yee
 *
 */
public class LocationUtil implements LocationListener {

	TagModeActivity activity;
	double latitude;
	double longitude;
	double altitude;
	LocationHandler locHandler;
	LocationManager locManager;
	
	DecimalFormat df = new DecimalFormat("#.0000");
	
	public LocationUtil(TagModeActivity activity) {
		this.activity = activity;
		locManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	public LocationUtil(TagModeActivity activity, LocationHandler handler) {
		this(activity);
		this.locHandler = handler;
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		/*this.latitude = arg0.getLatitude();
		((TextView) activity.findViewById(R.id.latValue)).setText(df.format(latitude));
		this.longitude = arg0.getLongitude();
		((TextView) activity.findViewById(R.id.lonValue)).setText(df.format(longitude));
		this.altitude = arg0.getAltitude();*/
		final Location loc = arg0;
		Thread t = new Thread() {
			public void run() {
				try {
					locHandler.activity.runOnUiThread(new Runnable() {
						public void run() {
							locHandler.updateLocation(loc.getLatitude(), loc.getLongitude(), loc.getAltitude());
						}
					});
					
				} catch (Exception e) {}
			}
		};
		t.start();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(activity, "GPS is disabled", Toast.LENGTH_SHORT).show();
		
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
		return new Tag(latitude, longitude, altitude);
	}

}
