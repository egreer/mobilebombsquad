package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
	BombTimer bombTimer;
	CountDownTimer confirmTimer;
	private ArrayList<Player> players;
	private int currentPlayer;
	private int numTouchPoints; //correct amount per player
	
	private boolean releasable;
	
	//MediaPlayer mp = MediaPlayer.create(this, R.raw.notify);
	//Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createPlayers();
		
		
		layout = new RelativeLayout(this);
		//view = new PlayableSurfaceView(this, players.get(0).getBackgroundColor());
		view = new PlayableSurfaceView(this, players.get(0));
		clock = new TextView(this);
		
		initializeGame();
		
		clock.setText("4");
		clock.setTextColor(Color.BLUE);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		layout.addView(view);
		layout.addView(clock);
		setContentView(layout);

		confirmTimer =  new CountDownTimer(5000, 1000) {

			public void onTick(long millisUntilFinished) {
				//do nothing
			}

			public void onFinish() {
				//play confirm sound
			}
		};
		
		bombTimer = new BombTimer(5000, 1000, clock, this);

		
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
		//explosion();
		//initializeGame();
		//gameLogic();
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
				//startTurn();
				//view.bubble.
				//bombTimer.start();
				startTurn();
				view.circle.generatePosition();
			} else if (color == players.get(nextPlayer()).getTouchpointColor()) {
				signalRelease();
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
					startTurn();
			}
		} else {
			explosion();
		}
	}
	
	public void isBubbleInCircle(boolean yes) {
		//once trigger bubble is in targetcircle
		//pause timer
		//pauseTimer(failTimer);
		//bombTimer.pause();
		//start "confirm" timer
		//confirmTimer.start();
		
		if (yes) {
			bombTimer.pause();
			confirmTimer.start();
		} else {
			confirmTimer.cancel();
			bombTimer.resume();
		}
	}
	
	public void signalRelease() {
		releasable = true;
		//sound
		//vibrate??
	}
	
	public int nextPlayer() {
		return (currentPlayer + 1) % players.size();
	}
	
	public void explosion() {
		//play explosion
		//Drawable explosion = getResources().getDrawable(R.drawable.explode);
		//explosion.draw(new Canvas());
		view.drawExplosion();
		//show game over screen + retry?
	}
	
	void createPlayers() {
		players = new ArrayList<Player>();
		players.add(new Player(Color.RED, Color.WHITE, Color.BLACK));
		players.add(new Player(Color.CYAN, Color.BLACK, Color.WHITE));
	}
	
	void startTurn() {
		//change colors + randomize position of target circle
		view.changePlayer(players.get(currentPlayer));
		//start fail timer
		bombTimer.start();
		//play sound
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		manager.unregisterListener(listener);
	}
}