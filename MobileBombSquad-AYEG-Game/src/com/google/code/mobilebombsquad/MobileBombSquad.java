package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	static BombTimer bombTimer;
	CountDownTimer confirmTimer;
	private ArrayList<Player> players;
	private int currentPlayer;
	
	private boolean safeToPass;
	private boolean safeToMove;
	private boolean confirming;
	
	//MediaPlayer mp = MediaPlayer.create(this, R.raw.notify);
	//Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createPlayers();
		
		layout = new RelativeLayout(this);
		view = new PlayableSurfaceView(this, players.get(0));
		clock = new TextView(this);
		
		initializeGame();
		
		clock.setText("9");
		clock.setTextColor(Color.BLUE);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		layout.addView(view);
		layout.addView(clock);
		setContentView(layout);

		confirmTimer =  new CountDownTimer(5000, 1000) {

			public void onTick(long millisUntilFinished) {
				//do nothing
				//play tick sound
			}

			public void onFinish() {
				view.enableTouchPoint(players.get(nextPlayer()).getTouchpointColor());
				confirming = true;
				safeToMove = false;
				
			}
		};
		
		bombTimer = new BombTimer(10000, 1000, clock, this);

		
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		handler = new AccelHandler(this, view);
		listener = new AccelListener(handler);            

		Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_FASTEST);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_FASTEST);

		bombTimer.start();
		
		//show welcome screen
		//hit start game
		//initialization:
		////show one color's touch point
		//explosion();
		//initializeGame();
		//gameLogic();
	}
	
	public void initializeGame() {
		//numTouchPoints = 1;
		currentPlayer = 0;
		safeToMove = true;
		safeToPass = false;
		confirming = false;
		for (Player play : players ){
			//view.addTouchPoint(players.get(currentPlayer).getTouchpointColor());
			view.addTouchPoint(play.getTouchpointColor());
		}
		view.enableTouchPoint(players.get(currentPlayer).getTouchpointColor());
	}
	
	public void gameLogic() {
		//once pressed draw triggerbubble + targetcircle + start timer + clear other touchpoints
		//once trigger bubble is in targetcircle, pause timer, start "confirm" timer
		//once "confirm" timer is elapsed draw other color's touch point, stop "fail" timer
		//switch "control" to other player (flip screen color)
		////currentPlayer is switched when next player has touched all of their points and currentPlayer has released all his points
		//repeat
	}
	
	public void touchPointPressed() {
		if (view.isThisPointSelected(players.get(currentPlayer).getTouchpointColor()) &&
			view.isThisPointSelected(players.get(nextPlayer()).getTouchpointColor()) && 
			view.checkBubbleCircle() &&
			!safeToPass) {
			
			/*Toast signaltoast = Toast.makeText(this, "Signaling release", Toast.LENGTH_SHORT);
			signaltoast.show();
			*/
			signalRelease();
			
		}
	}
	
	public void touchPointReleased() {
		/*Toast t = Toast.makeText(this, "Point Released", Toast.LENGTH_SHORT);
		t.show();*/
		if (safeToPass) {
			int color = players.get(currentPlayer).getTouchpointColor();
			boolean nextPressed = view.isThisPointSelected(players.get(nextPlayer()).getTouchpointColor());
			if (!nextPressed) {
				explosion();
			} else if (!view.isThisPointSelected(color) &&
					   view.checkBubbleCircle()) {
					Toast releasetoast = Toast.makeText(this, "Releasing player " + currentPlayer, Toast.LENGTH_SHORT);
					releasetoast.show();
					view.disableTouchPoints(color);
					view.invalidate();
					currentPlayer = nextPlayer();
					//start turn for currentPlayer
					safeToPass = false;
					safeToMove = true;
					confirming = false;
					//bombTimer.cancel();
					startTurn();
				}
		} else if (safeToMove){
			
		} else {
			explosion();
		}
	}
	
	synchronized public void isBubbleInCircle(boolean yes) {
		//once trigger bubble is in targetcircle
		//pause timer
		//pauseTimer(failTimer);
		//bombTimer.pause();
		//start "confirm" timer
		//confirmTimer.start();
		
		if (yes){
			if(!confirming && view.isThisPointSelected(players.get(currentPlayer).getTouchpointColor())) {
				bombTimer.pause();
				confirmTimer.start();
				//confirming = true;
			}
		} else {
			if (safeToPass && confirming) {
				explosion();
			} else if (bombTimer.isPaused()) {
				confirming = false;
				confirmTimer.cancel();
				bombTimer.resume();
			}
		}
	}
	
	public void signalRelease() {
		safeToPass = true;
		//sound
		//vibrate??
		Toast signalreleasetoast = Toast.makeText(this, "Okay to release Player " + currentPlayer, Toast.LENGTH_SHORT);
		signalreleasetoast.show();
	}
	
	public int nextPlayer() {
		return (currentPlayer + 1) % players.size();
	}
	
	public void explosion() {
		//play explosion
		//Drawable explosion = getResources().getDrawable(R.drawable.explode);
		//explosion.draw(new Canvas());
		bombTimer.cancel();
		clock.setText("Boom");
		view.drawExplosion();
		//show game over screen + retry?
		//finish();
	}
	
	void createPlayers() {
		players = new ArrayList<Player>();
		players.add(new Player(Color.RED, Color.WHITE, Color.BLACK));
		players.add(new Player(Color.CYAN, Color.BLACK, Color.WHITE));
	}
	
	void startTurn() {
		bombTimer.cancel();
		//change colors + randomize position of target circle
		//start fail timer
		bombTimer.start();
		
		view.changePlayer(players.get(currentPlayer));
		
				//play sound
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		manager.unregisterListener(listener);
	}
}