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
	private TriggerBubble bubble;
	private TargetCircle circle;
	
	public PlayableSurfaceView(Context context) {
		super(context);

		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xFFFFFFFF);
		mDrawable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		((ShapeDrawable) bubble.getDrawable()).getPaint().setColor(0xFF00FF00);
		bubble.getDrawable().setBounds(25, 25, 50, 50);
		
		circle = new TargetCircle();
	}
	
	public PlayableSurfaceView(Context context, AttributeSet attributes) {
		super(context, attributes);

		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xFFFFFFFF);
		mDrawable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		((ShapeDrawable) bubble.getDrawable()).getPaint().setColor(0xFF00FF00);
		bubble.getDrawable().setBounds(25, 25, 50, 50);
		
		circle = new TargetCircle();
	}

	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		circle.getDrawable().draw(canvas);
		bubble.getDrawable().draw(canvas);

	}
}