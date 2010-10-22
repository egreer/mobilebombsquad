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
		timer.start();
	}
	
	public void cancel() {
		timer.cancel();
	}
	
	public void pause() {
		timer.cancel();
	}
	
	public void resume() {
		timer = generateTimer(millisUntilFinished, countDownInterval);
		timer.start();
	}

}
