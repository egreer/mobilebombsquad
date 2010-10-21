package com.google.code.mobilebombsquad;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AccelHandler {

	Context context;
	PlayableSurfaceView view;
	
	MediaPlayer mp;
	Vibrator vibrator;
	
	boolean conditionMet = false;
	
	public AccelHandler(Context context, PlayableSurfaceView view) {
		this.context = context;
		this.view = view;
		
		mp = MediaPlayer.create(context, R.raw.notify);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
	}
	
	void updateBubble(float x, float y, float z) {
		int height = view.getHeight();
		int width = view.getWidth();
		
		double xProportion = x / (Math.PI / 2.5);
		double yProportion = y / (Math.PI / 2.5);
		
		int newX = (int) (width/2.0 + xProportion*(width/2.0));
		int newY = (int) (height/2.0 + yProportion*(height/2.0));
		
		view.bubble.updatePosition(newX, newY);
		view.invalidate();
		
		if (checkCondition()) {
			if (!conditionMet) {
				//vibrator.vibrate(75);
				mp.start();
				conditionMet = true;
			}
		} else {
			conditionMet = false;
		}
	}
	
	boolean checkCondition() {
		return view.checkBubbleCircle();
	}
	
}
