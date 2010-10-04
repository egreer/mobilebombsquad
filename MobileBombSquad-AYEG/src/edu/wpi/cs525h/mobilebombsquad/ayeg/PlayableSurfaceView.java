package edu.wpi.cs525h.mobilebombsquad.ayeg;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;

	private ShapeDrawable mDrawable;	
	private boolean touchpointColor;
	private int numTouchpoints;
	ArrayList<TouchPoint> touchpoints = new ArrayList<TouchPoint>();
	MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.notify);
	Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

	public PlayableSurfaceView(Context context, AttributeSet attributes) {
		super(context, attributes);

		touchpointColor = true;
		numTouchpoints = 2;

		generateTouchPoints(touchpointColor, numTouchpoints);

		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xFFFFFFFF);
		mDrawable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
	}

	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		drawTouchPoints(canvas);

	}

	void drawTouchPoints(Canvas canvas) {
		for (TouchPoint point : touchpoints) {
			point.touchpoint.draw(canvas);
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
			if (color) {
				touchpoints.add(new TouchPoint(getResources().getDrawable(R.drawable.touchpointred)));
			} else {
				touchpoints.add(new TouchPoint(getResources().getDrawable(R.drawable.touchpointblue)));
			}
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
				mp.start();
				vibrator.vibrate(250);

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
