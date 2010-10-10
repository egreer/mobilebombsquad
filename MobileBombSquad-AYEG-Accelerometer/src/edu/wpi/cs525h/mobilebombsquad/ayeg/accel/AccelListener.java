package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
		Toast t;
		
		float values[] = new float[3];
		float R[] = new float[9];
		float I[] = new float [9];
		float[] gravity = new float [3];
		float[] geomagnetic = new float[3];
		
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
        t = Toast.makeText(context, "Orient x: " + x + " y: " + y + " z: " + z, Toast.LENGTH_SHORT);
        t.show();
	}

}
