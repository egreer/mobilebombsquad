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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO: 
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
	
	MediaPlayer tpPress;
	MediaPlayer confirmTick;
	MediaPlayer confirmFinish;
	MediaPlayer releaseSignal;
	MediaPlayer starting;
	MediaPlayer exiting;
	MediaPlayer retrying;
	Vibrator vibrator;
	
	private long highScore = 0;
	private long score = 0;
	private int passes = 0;
	
	private int difficulty = 1;
	
	TextView scoreText;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		tpPress = MediaPlayer.create(this, R.raw.type);
		confirmTick = MediaPlayer.create(this, R.raw.mkshort);
		confirmFinish = MediaPlayer.create(this, R.raw.mklong);
		//releaseSignal = MediaPlayer.create(this, R.raw.notify);
		releaseSignal = MediaPlayer.create(this, R.raw.terryokay);
		starting = MediaPlayer.create(this, R.raw.terrygetserious);
		exiting = MediaPlayer.create(this, R.raw.terryheyrookie);
		retrying = MediaPlayer.create(this, R.raw.terryshallwe);
		
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		createPlayers();
		
		setContentView(R.layout.main);
		MediaPlayer.create(this, R.raw.terryburnknuckle).start();
	}
	
	public void initialize(View view) {
		starting.start();
		initializeGame();
	}
	
	public void initializeGame() {
		layout = new RelativeLayout(this);
		view = new PlayableSurfaceView(this, players.get(0));

		clock = new TextView(this);
		clock.setTextSize(30);
		clock.setPadding(PlayableSurfaceView.OFFSETX + 5, 5 , 0, 0);
		
		scoreText = new TextView(this);
		scoreText.setTextSize(30);
		scoreText.setPadding(PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - 75 , 5 , PlayableSurfaceView.OFFSETY, 0);

		layout.addView(view);
		layout.addView(clock);
		layout.addView(scoreText);
		
		for (Player play : players ){
			view.addTouchPoint(play.getTouchpointColor());
		}
		
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
		
		handler = new AccelHandler(this, view);
		listener = new AccelListener(handler); 

		retryGame();
	}
	
	/**
	 * Initializes the game variables
	 */
	public void retryGame() {
		score = 0;
		passes = 0;
		currentPlayer = 0;
		difficulty = 1;
		safeToMove = true;
		safeToPass = false;
		confirming = false;
		
		view.resetEverything();
		view.enableTouchPoint(players.get(currentPlayer).getTouchpointColor());
		view.changePlayer(players.get(currentPlayer), passes);
		//clock.setText("5");
		clock.setTextColor(players.get(currentPlayer).touchpointColor);
		
		updateScoreText();
		scoreText.setTextColor(players.get(nextPlayer()).touchpointColor);		
		
		setContentView(layout);		
		view.shouldIExplode(false);           

		Sensor mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		manager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_FASTEST);
		manager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_FASTEST);

		bombTimer = new BombTimer(11000, 1000, clock, this);
		bombTimer.start();
		
		
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
				Toast nextrelease = Toast.makeText(this, "Next player "+ nextPlayer() +" released the touch point", Toast.LENGTH_SHORT);
				nextrelease.show();
				explosion();
			} else if (!view.isThisPointSelected(currentColor) &&
					   view.checkBubbleCircle()) {

					view.disableTouchPoints(currentColor);
					//view.enableTouchPoint(nextColor);
					view.invalidate();
					currentPlayer = nextPlayer();
					//start turn for currentPlayer
					safeToPass = false;
					safeToMove = true;
					confirming = false;
					passes++; // A successful Pass
					
					score += bombTimer.millisUntilFinished / 1000 * difficulty;
					clock.setTextColor(players.get(currentPlayer).touchpointColor);
					scoreText.setTextColor(players.get(nextPlayer()).touchpointColor);
					updateScoreText();
					if (passes % 5 == 0 && difficulty < 10) {
						difficulty++;
						view.decrementTouchPointSizes();
					}
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
		highScore = Math.max(score, highScore);
		//play explosion
		//MediaPlayer exploder = MediaPlayer.create(this, R.raw.explosion);
		MediaPlayer exploder = MediaPlayer.create(this, R.raw.terryexplosion);
		exploder.start();
		vibrator.vibrate(2000);
		manager.unregisterListener(listener);
		bombTimer.cancel();
		clock.setText("");
		scoreText.setText("");
		view.shouldIExplode(true);
		//view.bubble.setVisible(false, false);
		//view.circle.setVisible(false, false);
		view.disableTouchPoints(players.get(currentPlayer).touchpointColor);
		view.disableTouchPoints(players.get(nextPlayer()).touchpointColor);
		//show game over screen + retry?
		//finish();
		//RetryDialog dialog = new RetryDialog(this, createAlertDialog());
		Toast scoreText = Toast.makeText(this, "Passes: " + passes + " Score: " + score + " High Score: " + highScore, Toast.LENGTH_LONG);
		scoreText.show();

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
		//view.enableTouchPoint(players.get(currentPlayer).getTouchpointColor());
		
		
		view.changePlayer(players.get(currentPlayer), passes);
		int newtime = 12 - difficulty;
		clock.setText("" + newtime);
		//bombTimer = new BombTimer(11000 - (difficulty * 1000), 1000, clock, this);
		bombTimer = new BombTimer(1000*newtime, 1000, clock, this);
		bombTimer.start();
		//play sound
	}
	
	AlertDialog createRetryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to retry?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                retrying.start();
	                retryGame();
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
	
	/**
	 * Updates the score text with the current value of score
	 */
	void updateScoreText(){
		scoreText.setText("" + score);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
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
		exiting.start();
		super.onDestroy();
		if (bombTimer != null) bombTimer.cancel();
		if (confirmTimer != null) confirmTimer.cancel();
		if (manager != null) manager.unregisterListener(listener);
	}
	
	public void quitgame(View view) {
		finish();
	}
	
	public int getDifficulty() {
		return difficulty;
	}
}