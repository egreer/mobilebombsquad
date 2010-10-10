package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccelListener implements SensorEventListener {
	float values[] = new float[3];
	float R[] = new float[9];
	float I[] = new float [9];
	float[] gravity = new float [3];
	float[] geomagnetic = new float[3];
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
	
		if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			gravity = sensorEvent.values;   
		}
		
		if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			geomagnetic = sensorEvent.values;
		}
				
		SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
        SensorManager.getOrientation(R, values);
        
        float x = values[0];
        float y = values[1];
        float z = values[2];
	}
}
