package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;
	
	private ShapeDrawable playable;
	TriggerBubble bubble;
	TargetCircle circle;
	
	
	private boolean touchpointColor;
	private int numTouchpoints;
	ArrayList<TouchPoint> touchpoints = new ArrayList<TouchPoint>();
	MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.notify);
	Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

	
	public PlayableSurfaceView(Context context) {
		super(context);

		touchpointColor = true;
		numTouchpoints = 2;
		
		playable = new ShapeDrawable(new RectShape());
		playable.getPaint().setColor(0xFFFFFFFF);
		playable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		generateTouchPoints(touchpointColor, numTouchpoints);
		bubble = new TriggerBubble();
		circle = new TargetCircle();
	}

	protected void onDraw(Canvas canvas) {
		playable.draw(canvas);
		circle.draw(canvas);
		bubble.draw(canvas);
		drawTouchPoints(canvas);

	}
	
	boolean checkBubbleCircle() {
		return circle.getBounds().contains(bubble.getBounds());
	}
	
	
	void drawTouchPoints(Canvas canvas) {
		for (TouchPoint point : touchpoints) {
			point.draw(canvas);
		}
	}

	/**
	 * TODO: Something other than boolean
	 * TODO: Make sure they don't overlap
	 * @param color red = true, blue = false
	 */
	void generateTouchPoints(boolean color, int number) {
		touchpoints.clear(); //TODO: Change for final app
		for (int i = 0; i < number; i++) {
			touchpoints.add(new TouchPoint());
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		for (int i=0; i<event.getPointerCount(); i++) {

			int action = event.getActionMasked();
			int x = (int) event.getX(i);
			int y = (int) event.getY(i); 

			//if (action == MotionEvent.ACTION_DOWN) {
			for (TouchPoint point : touchpoints) {
				if (point.contains(x, y)) {
					//point.setSelected(true); //Uncomment to do multitouch more then 3 points implements a toggle that  
					if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
						point.setSelected(true);
					} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
						point.setSelected(false);
					}
				}
			}

			if (checkConditions()) {
				vibrator.vibrate(250);
				mp.start();
				

				touchpointColor = !touchpointColor;
				generateTouchPoints(touchpointColor, numTouchpoints);
				this.invalidate();
			}
		}
		return true;
	}


	private boolean checkConditions() {
		for (TouchPoint point : touchpoints) {
			if (!point.getSelected()) {
				return false;
			}
		}
		return true;

	}
}