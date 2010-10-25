package com.google.code.mobilebombsquad;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/** This listens to the accelerometer for updates
 * 
 * @author Eric Greer
 * @author Andrew Yee
 */
public class AccelListener implements SensorEventListener {
	float values[] = new float[3];
	float R[] = new float[9];
	float I[] = new float [9];
	float[] gravity = new float [3];
	float[] geomagnetic = new float[3];
	AccelHandler handler;
	
	/** Constructor for the listener
	 * @param handler
	 */
	public AccelListener(AccelHandler handler) {
		super();
		this.handler = handler;	
	}

	/*
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(final SensorEvent sensorEvent) {
		Thread t = new Thread() {
			public void run() {
				try {
					if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
						gravity = sensorEvent.values;   
					}
					
					if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
						geomagnetic = sensorEvent.values;
					}
							
					SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
			        SensorManager.getOrientation(R, values);
			        
			        final float x = (-1.0f) * values[2];
			        final float y = values[1];
			        final float z = values[0];
			        //handler.updateBubble(x, y, z);
	 
			        handler.context.runOnUiThread(new Runnable() {
						public void run() {
							handler.updateBubble(x, y, z);
						}
					});
					
				} catch (Exception e) {}
			}
		};
		t.start();
		
	}
}
