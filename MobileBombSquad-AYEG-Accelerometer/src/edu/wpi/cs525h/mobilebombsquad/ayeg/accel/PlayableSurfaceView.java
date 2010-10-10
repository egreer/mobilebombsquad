package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;

	private ShapeDrawable mDrawable;	
	MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.notify);
	Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

	public PlayableSurfaceView(Context context, AttributeSet attributes) {
		super(context, attributes);

		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xFFFFFFFF);
		mDrawable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
	}

	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);

	}
}