package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import java.text.DecimalFormat;

import edu.wpi.cs525h.ayeg.virtualgraffiti.R;
import android.view.View;
import android.widget.TextView;

/**
 * Handles the changes in location from the GPS/LocationUtil
 * 
 * @author Andrew Yee
 *
 */
public class LocationHandler {
	TagModeActivity activity;
	
	DecimalFormat df = new DecimalFormat("#.000000");
	
	public LocationHandler(TagModeActivity activity) {
		this.activity = activity;
	}
	
	/**
	 * Updates the location in the activity, and updates the appropriate TextViews
	 * 
	 * @param latitude		the new latitude
	 * @param longitude		the new longitude
	 * @param altitude		the new altitude
	 */
	void updateLocation(double latitude, double longitude, double altitude) {
		activity.latitude = latitude;
		activity.longitude = longitude;
		activity.altitude = altitude;
		
		((TextView) activity.findViewById(R.id.latValue)).setText(df.format(latitude));
		((TextView) activity.findViewById(R.id.lonValue)).setText(df.format(longitude));
		activity.findViewById(R.id.latValue).invalidate();
		activity.findViewById(R.id.lonValue).invalidate();
	}
}
