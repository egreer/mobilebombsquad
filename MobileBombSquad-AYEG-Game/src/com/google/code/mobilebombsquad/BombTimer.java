package com.google.code.mobilebombsquad;

import android.os.CountDownTimer;
import android.widget.TextView;

/** BombTimer is a specialized timer that can be paused or resumed. 
 * 
 * @author Eric Greer
 * @author Andrew Yee
 *
 */
public class BombTimer {

	CountDownTimer timer;
	TextView clock;
	MobileBombSquad exploder;
	long initialMillisInFuture;
	long countDownInterval;
	long millisUntilFinished;
	
	boolean paused = false;
	
	/** Constructor
	 * 
	 * @param millisInFuture	How long the timer goes for
	 * @param countDownInterval	How often the timer ticks
	 * @param clock				The text view to update with the time
	 * @param exploder			MobileBombSquad that contains the exploder method
	 */
	public BombTimer(long millisInFuture, long countDownInterval, TextView clock, MobileBombSquad exploder) {
		this.initialMillisInFuture = millisInFuture;
		this.countDownInterval = countDownInterval;
		this.clock = clock;
		this.exploder = exploder;
		
		timer = generateTimer(millisInFuture, countDownInterval);
	}

	/** Generates a new countdown timer
	 * 
	 * @param millisInFuture		How long the timer goes for
	 * @param countDownInterval		How often the time ticks 
	 * @return						Returns a new coutdown timer
	 */
	public CountDownTimer generateTimer(long millisInFuture, long countDownInterval) {
		return new CountDownTimer(millisInFuture, countDownInterval) {
			
			/*
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onTick(long)
			 */
			@Override
			public void onTick(long millisUntilFinished) {
				onTimerTick(millisUntilFinished);				
			}
			
			/*
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onFinish()
			 */
			@Override
			public void onFinish() {
				onTimerFinish();
				
			}
		};
	}
	
	/** Callback execution for when the timer finishes
	 * 
	 */
	public void onTimerFinish() {
		clock.setText("");
        clock.invalidate();
        exploder.explosion();
	}

	/** Callback execution for when the timer ticks 
	 * 
	 * @param millisUntilFinished	How many seconds are left.	
	 */
	public void onTimerTick(long millisUntilFinished) {
		this.millisUntilFinished = millisUntilFinished;
		clock.setText("" + millisUntilFinished / 1000);
        clock.invalidate();
	}
	
	/**
	 * Starts the timer with the initial time
	 */
	public void start() {
		timer.cancel();
		millisUntilFinished = initialMillisInFuture;
		timer = generateTimer(initialMillisInFuture, countDownInterval);
		timer.start();
	}
	
	/**
	 * Cancels the timer. Do not resume the thread.
	 */
	public void cancel() {
		timer.cancel();
	}
	
	/**
	 *	Pauses the timer 
	 */
	public void pause() {
		paused = true;
		timer.cancel();
	}
	
	/**
	 * Resumes the timer
	 */
	public void resume() {
		paused = false;
		timer.cancel();
		timer = generateTimer(millisUntilFinished, countDownInterval);
		timer.start();
	}
	
	/** 
	 * @return Returns whether the timer is paused 
	 */
	public boolean isPaused() {
		return paused;
	}

}
