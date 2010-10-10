package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AccelHandler {

	MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.notify);
	Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
	
	public AccelHandler() {
		
	}
	
	boolean checkCondition() {
		return false;
	}
	
}
