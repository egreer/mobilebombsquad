package com.google.code.mobilebombsquad;

import android.os.CountDownTimer;
import android.widget.TextView;

public class BombTimer {

	CountDownTimer timer;
	TextView clock;
	MobileBombSquad exploder;
	long initialMillisInFuture;
	long countDownInterval;
	long millisUntilFinished;
	
	boolean paused = false;
	
	public BombTimer(long millisInFuture, long countDownInterval, TextView clock, MobileBombSquad exploder) {
		//super(millisInFuture, countDownInterval);
		initialMillisInFuture = millisInFuture;
		this.countDownInterval = countDownInterval;
		this.clock = clock;
		this.exploder = exploder;
		
		timer = generateTimer(millisInFuture, countDownInterval);
	
	}

	public CountDownTimer generateTimer(long millisInFuture, long countDownInterval) {
		return new CountDownTimer(millisInFuture, countDownInterval) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				onTimerTick(millisUntilFinished);				
			}
			
			@Override
			public void onFinish() {
				onTimerFinish();
				
			}
		};
	}
	
	//@Override
	public void onTimerFinish() {
        clock.setText("Boom");
        clock.invalidate();
        exploder.explosion();
	}

	//@Override
	public void onTimerTick(long millisUntilFinished) {
		this.millisUntilFinished = millisUntilFinished;
		clock.setText("" + millisUntilFinished / 1000);
        clock.invalidate();

	}
	
	public void start() {
		timer.cancel();
		timer = generateTimer(initialMillisInFuture, countDownInterval);
		timer.start();
	}
	
	/*public void reset() {
		timer = generateTimer(initialMillisInFuture, countDownInterval);
	}*/
	
	public void cancel() {
		timer.cancel();
	}
	
	public void pause() {
		paused = true;
		timer.cancel();
	}
	
	public void resume() {
		paused = false;
		timer = generateTimer(millisUntilFinished, countDownInterval);
		timer.start();
	}
	
	public boolean isPaused() {
		return paused;
	}

}
