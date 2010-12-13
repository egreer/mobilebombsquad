package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import android.view.View;

public class LocationHandler {
	TagModeActivity context;
	View view;
	
	public LocationHandler(TagModeActivity context, View view) {
		this.context = context;
		this.view = view;
	}
	
	void updateLocation(double latitude, double longitude, double altitude) {
		
	}
}
