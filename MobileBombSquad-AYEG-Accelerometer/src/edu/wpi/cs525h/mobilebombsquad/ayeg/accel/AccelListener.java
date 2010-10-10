package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Toast;

public class AccelListener implements SensorEventListener {
	Context context;
	public AccelListener(Context context) {
		this.context = context;
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		    double x = sensorEvent.values[0];
		    double y = sensorEvent.values[1];
		    double z = sensorEvent.values[2];    
		    Toast.makeText(context, "Accel x: " + x + " y: " + y + " z: " + z, Toast.LENGTH_SHORT);
	 }
		 if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			    double x = sensorEvent.values[0];
			    double y = sensorEvent.values[1];
			    double z = sensorEvent.values[2];    
			    Toast.makeText(context, "Orient x: " + x + " y: " + y + " z: " + z, Toast.LENGTH_SHORT);
		 }
	}

}
