package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO: 
 * Smoothing
 * Animations?
 *  
 * @author Eric Greer
 * @author Andrew Yee
 */
public class MobileBombSquad extends Activity {
	static final int RETRY_DIALOG = 1; 
	
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
	MediaPlayer tpPress;
	MediaPlayer confirmTick;
	MediaPlayer confirmFinish;
	MediaPlayer releaseSignal;
	Vibrator vibrator;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		createPlayers();

		
		//initializeGame();
		
		setContentView(R.layout.main);
		//Button button = (Button)findViewById(R.id.startbutton);
		//button.setPadding(160, 380, 0, 0);
		//button.setWidth(40);
		//button.setHeight(20);
		
		
		//layout = new RelativeLayout(this);
		//Drawable titlescn = getResources().get
		
		//show welcome screen
		//hit start game
		//initialization:
		////show one color's touch point
		//explosion();
		//initializeGame();
		//gameLogic();
	}
	
	public void initialize(View view) {
		initializeGame();
	}
	
	/**
	 * Initializes the game variables
	 */
	public void initializeGame() {
		
		layout = new RelativeLayout(this);
		view = new PlayableSurfaceView(this, players.get(0));
		clock = new TextView(this);
		
		//numTouchPoints = 1;
		currentPlayer = 0;
		safeToMove = true;
		safeToPass = false;
		confirming = false;
		for (Player play : players ){
			view.addTouchPoint(play.getTouchpointColor());
		}
		view.enableTouchPoint(players.get(currentPlayer).getTouchpointColor());
		
		clock.setText("9");
		clock.setTextColor(Color.BLUE);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		layout.addView(view);
		layout.addView(clock);
		setContentView(layout);
		
		confirmTick = MediaPlayer.create(this, R.raw.mkshort);
		confirmFinish = MediaPlayer.create(this, R.raw.mklong);
		//releaseSignal = MediaPlayer.create(this, R.raw.notify);
		releaseSignal = MediaPlayer.create(this, R.raw.terryokay);
		
		confirmTimer =  new CountDownTimer(3000, 1000) {
			/*
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onTick(long)
			 */
			public void onTick(long millisUntilFinished) {
				//do nothing
				//play tick sound
				confirmTick.start();
			}

			/*
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onFinish()
			 */
			public void onFinish() {
				view.enableTouchPoint(players.get(nextPlayer()).getTouchpointColor());
				confirming = true;
				safeToMove = false;
				confirmFinish.start();
				
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
		
		tpPress = MediaPlayer.create(this, R.raw.type);
	}
	
	public void gameLogic() {
		//once pressed draw triggerbubble + targetcircle + start timer + clear other touchpoints
		//once trigger bubble is in targetcircle, pause timer, start "confirm" timer
		//once "confirm" timer is elapsed draw other color's touch point, stop "fail" timer
		//switch "control" to other player (flip screen color)
		////currentPlayer is switched when next player has touched all of their points and currentPlayer has released all his points
		//repeat
	}
	
	/** 
	 * This method is called whenever a TouchPoint is pressed
	 */
	public void touchPointPressed() {
		tpPress.start();
		if (view.isThisPointSelected(players.get(currentPlayer).getTouchpointColor()) &&
			view.isThisPointSelected(players.get(nextPlayer()).getTouchpointColor()) && 
			view.checkBubbleCircle() &&
			!safeToPass) {
			
			signalRelease();
			
		}
	}
	
	/**
	 * Method is called whenever a touch point is released
	 */
	public void touchPointReleased() {
		if (safeToPass) {
			int currentColor = players.get(currentPlayer).getTouchpointColor();
			int nextColor = players.get(nextPlayer()).getTouchpointColor();
			boolean nextPressed = view.isThisPointSelected(nextColor);
			if (!nextPressed) {
				Toast nextrelease = Toast.makeText(this, "Next player released the touch point", Toast.LENGTH_SHORT);
				nextrelease.show();
				explosion();
			} else if (!view.isThisPointSelected(currentColor) &&
					   view.checkBubbleCircle()) {

					view.disableTouchPoints(currentColor);
					view.invalidate();
					currentPlayer = nextPlayer();
					//start turn for currentPlayer
					safeToPass = false;
					safeToMove = true;
					confirming = false;
					startTurn();
				}
		} else if (safeToMove){
			
		} else {
			Toast released = Toast.makeText(this, "You released the touch point", Toast.LENGTH_SHORT);
			released.show();
			explosion();
		}
	}
	
	/**
	 * Pauses/resumes the bomb timer and Starts/cancels the confirm timer 
	 * @param yes	Boolean True bubble is is circle, false bubble is not
	 */
	public void isBubbleInCircle(boolean yes) {
		//once trigger bubble is in targetcircle
		//pause timer
		//pauseTimer(failTimer);
		//bombTimer.pause();
		//start "confirm" timer
		//confirmTimer.start();
		
		if (yes){
			if(!confirming && view.isThisPointSelected(players.get(currentPlayer).getTouchpointColor())) {
				view.circle.changeColor(Color.GREEN);
				bombTimer.pause();
				confirmTimer.start();
				//confirming = true;
			}
		} else {
			if ((safeToPass && confirming) || !safeToMove) {
				Toast leftcircle = Toast.makeText(this, "The bubble left the circle", Toast.LENGTH_SHORT);
				leftcircle.show();
				explosion();
			} else if (bombTimer.isPaused()) {
				confirming = false;
				confirmTimer.cancel();
				bombTimer.resume();
				view.circle.changeColor(players.get(currentPlayer).getTargetcircleColor());
			}
		}
	}
	
	/**
	 *  Signal's the release of the TouchPoints from the current user 
	 */
	public void signalRelease() {
		safeToPass = true;
		//sound
		releaseSignal.start();
		//vibrate??
		Toast signalreleasetoast = Toast.makeText(this, "Okay to release Player " + currentPlayer, Toast.LENGTH_SHORT);
		//signalreleasetoast.show();
	}
	
	/** 
	 * @return Returns the number of the next player
	 */
	public int nextPlayer() {
		return (currentPlayer + 1) % players.size();
	}
	
	/**
	 * Failure of the End Game conditions, and displays an explosion. 
	 */
	public void explosion() {
		//play explosion
		//MediaPlayer exploder = MediaPlayer.create(this, R.raw.explosion);
		MediaPlayer exploder = MediaPlayer.create(this, R.raw.terryexplosion);
		exploder.start();
		vibrator.vibrate(2000);
		manager.unregisterListener(listener);
		bombTimer.cancel();
		clock.setText("");
		view.drawExplosion();
		//view.bubble.setVisible(false, false);
		//view.circle.setVisible(false, false);
		view.disableTouchPoints(players.get(currentPlayer).touchpointColor);
		view.disableTouchPoints(players.get(nextPlayer()).touchpointColor);
		//show game over screen + retry?
		//finish();
		//RetryDialog dialog = new RetryDialog(this, createAlertDialog());
		
		new CountDownTimer(2000, 1000) {

			@Override
			public void onFinish() {
				showDialog(RETRY_DIALOG);
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
		}.start();
	}
	
	/**
	 * Creates the players of the game 
	 */
	void createPlayers() {
		players = new ArrayList<Player>();
		players.add(new Player(Color.RED, Color.WHITE, Color.BLACK));
		players.add(new Player(Color.CYAN, Color.BLACK, Color.WHITE));
	}
	
	/**
	 *  Starts the turn by:
	 *  	Disabling the BombTimer
	 *  	Starting a new BombTimer
	 *  	Changes the view of the current player 
	 */
	void startTurn() {
		bombTimer.cancel();
		//change colors + randomize position of target circle
		//start fail timer
		bombTimer.start();
		
		view.changePlayer(players.get(currentPlayer));
		//play sound
	}
	
	AlertDialog createRetryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to retry?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                MediaPlayer.create(getThisContext(), R.raw.terrychaching).start();
	                initializeGame();
	           }
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                getThisContext().finish();
	           }
	       });
		//RetryDialog alert = (RetryDialog) builder.create();
		
		return builder.create();
	}
	
	Activity getThisContext() {
		return this;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		
		switch(id) {
			case RETRY_DIALOG:
				MediaPlayer.create(this, R.raw.terryareyouokay).start();
				dialog = createRetryDialog();
				break;
			default:
				dialog = null;
		}
		
		return dialog;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		manager.unregisterListener(listener);
	}
}