package com.google.code.mobilebombsquad;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup.LayoutParams;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*TODO: 
 * Smoothing
 * Animations?
 *  
 * 
 */

public class MobileBombSquad extends Activity {
	/** Called when the activity is first created. */
	private SensorManager manager;
	private AccelListener listener;
	private AccelHandler handler;
	private RelativeLayout layout;
	private PlayableSurfaceView view;
	TextView clock;
	CountDownTimer timer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		layout = new RelativeLayout(this);
		view = new PlayableSurfaceView(this);
		clock = new TextView(this);
		
		clock.setText("4");
		clock.setTextColor(Color.BLUE);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		layout.addView(view);
		layout.addView(clock);
		setContentView(layout);

		timer =  new CountDownTimer(5000, 1000) {

	     public void onTick(long millisUntilFinished) {
	         clock.setText("" + millisUntilFinished / 1000);
	         clock.invalidate();
	     }

	     public void onFinish() {
	         clock.setText("Boom");
	         clock.invalidate();
	     }
	  }.start();

		
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		handler = new AccelHandler(this, view);
		listener = new AccelListener(handler);            

		Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_FASTEST);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_FASTEST);

	}
	
	
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		manager.unregisterListener(listener);
	}
}