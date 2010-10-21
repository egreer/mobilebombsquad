package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
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
	CountDownTimer failTimer;
	CountDownTimer confirmTimer;
	private ArrayList<Player> players;
	private int currentPlayer;
	private int numTouchPoints; //correct amount per player
	
	private boolean releasable;
	
	MediaPlayer mp = MediaPlayer.create(this, R.raw.notify);
	Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createPlayers();
		
		layout = new RelativeLayout(this);
		view = new PlayableSurfaceView(this, players.get(0).getBackgroundColor());
		clock = new TextView(this);
		
		clock.setText("4");
		clock.setTextColor(Color.BLUE);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		layout.addView(view);
		layout.addView(clock);
		setContentView(layout);

		failTimer =  new CountDownTimer(5000, 1000) {

	     public void onTick(long millisUntilFinished) {
	         clock.setText("" + millisUntilFinished / 1000);
	         clock.invalidate();
	     }

	     public void onFinish() {
	         clock.setText("Boom");
	         clock.invalidate();
	         //explosion();
	     }
	  };//.start();

		
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		handler = new AccelHandler(this, view);
		listener = new AccelListener(handler);            

		Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_FASTEST);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_FASTEST);

		//show welcome screen
		//hit start game
		//initialization:
		////show one color's touch point
		initializeGame();
		gameLogic();
	}
	
	public void initializeGame() {
		numTouchPoints = 1;
		currentPlayer = 0;
		releasable = false;
		view.addNewTouchPoint(players.get(currentPlayer).getTouchpointColor());
	}
	
	public void gameLogic() {
		//once pressed draw triggerbubble + targetcircle + start timer + clear other touchpoints
		//once trigger bubble is in targetcircle, pause timer, start "confirm" timer
		//once "confirm" timer is elapsed draw other color's touch point, stop "fail" timer
		//switch "control" to other player (flip screen color)
		////currentPlayer is switched when next player has touched all of their points and currentPlayer has released all his points
		//repeat
	}
	
	public void touchPointPressed(int color) {
		if (view.allTouchPointsPressed(color)) {
			//start condition
			if (color == players.get(currentPlayer).getTouchpointColor()) {
				//start timer/game
			} else if (color == players.get(nextPlayer()).getTouchpointColor()) {
				//signal release
			}
		}
	}
	
	public void touchPointReleased(int color) {
		if (releasable) {
			if (view.allTouchPointsReleased(color) && (color == players.get(currentPlayer).getTouchpointColor())) {
					releasable = false;
					view.removeTouchPoints(color);
					currentPlayer = nextPlayer();
					//start turn for currentPlayer
			}
		} else {
			explosion();
		}
	}
	
	public void release() {
		releasable = true;
		//sound
		//vibrate??
	}
	
	public int nextPlayer() {
		return (currentPlayer + 1) % players.size();
	}
	
	public void explosion() {
		//play explosion
		//show game over screen + retry?
	}
	
	void createPlayers() {
		players = new ArrayList<Player>();
		players.add(new Player(Color.RED, Color.WHITE));
		players.add(new Player(Color.CYAN, Color.BLACK));
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		manager.unregisterListener(listener);
	}
}