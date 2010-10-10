package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MobileBombSquad extends Activity {
    /** Called when the activity is first created. */
	private SensorManager manager;
	private AccelListener listener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        listener = new AccelListener();
                
        
        Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_GAME/*SensorManager.SENSOR_DELAY_UI*/);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_GAME/*SensorManager.SENSOR_DELAY_UI*/);
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	manager.unregisterListener(listener);
    }
}