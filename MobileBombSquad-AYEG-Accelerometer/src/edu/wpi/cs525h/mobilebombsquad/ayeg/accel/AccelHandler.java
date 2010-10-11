package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AccelHandler {

	Context context;
	PlayableSurfaceView view;
	
	MediaPlayer mp;
	Vibrator vibrator;
	
	public AccelHandler(Context context, PlayableSurfaceView view) {
		this.context = context;
		this.view = view;
		
		mp = MediaPlayer.create(context, R.raw.notify);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
	}
	
	void updateBubble(float x, float y, float z) {
		
	}
	
	boolean checkCondition() {
		return false;
	}
	
}
