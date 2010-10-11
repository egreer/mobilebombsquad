package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;
	
	private ShapeDrawable playable;
	TriggerBubble bubble;
	TargetCircle circle;
	
	public PlayableSurfaceView(Context context) {
		super(context);

		playable = new ShapeDrawable(new RectShape());
		playable.getPaint().setColor(0xFFFFFFFF);
		playable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		circle = new TargetCircle();
	}

	protected void onDraw(Canvas canvas) {
		playable.draw(canvas);
		circle.draw(canvas);
		bubble.draw(canvas);

	}
	
	boolean checkBubbleCircle() {
		return circle.getBounds().contains(bubble.getBounds());
	}
}