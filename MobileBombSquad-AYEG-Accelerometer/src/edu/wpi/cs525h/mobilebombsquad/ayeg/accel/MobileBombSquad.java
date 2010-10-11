package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class MobileBombSquad extends Activity {
    /** Called when the activity is first created. */
	private SensorManager manager;
	private AccelListener listener;
	private AccelHandler handler;
	private LinearLayout layout;
	private PlayableSurfaceView view;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        layout = new LinearLayout(this);
        view = new PlayableSurfaceView(this);
        layout.addView(view);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        setContentView(layout);
        
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        handler = new AccelHandler(this, view);
        listener = new AccelListener(handler);            
        
        Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_GAME);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_GAME);
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	manager.unregisterListener(listener);
    }
}