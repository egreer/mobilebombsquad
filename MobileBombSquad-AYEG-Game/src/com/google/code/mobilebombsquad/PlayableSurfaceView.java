package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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

	ArrayList<TouchPoint> touchpoints = new ArrayList<TouchPoint>();

	boolean explosion;
	
	public PlayableSurfaceView(Context context, Player player) {
		super(context);
		
		playable = new ShapeDrawable(new RectShape());
		//playable.getPaint().setColor(initialColor);
		playable.getPaint().setColor(player.getBackgroundColor());
		playable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		circle = new TargetCircle(40, player.getTargetcircleColor());
		
		explosion = false;
	}

	protected void onDraw(Canvas canvas) {
		if (explosion) {
			this.getContext().getResources().getDrawable(R.drawable.explode).draw(canvas);
			return;
		}
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

	void addNewTouchPoint(int color) {
		touchpoints.add(new TouchPoint(color));
	}
	
	void removeTouchPoints(int color) {
		ArrayList<TouchPoint> removable = new ArrayList<TouchPoint>();
		
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				removable.add(point);
			}
		}
		
		touchpoints.removeAll(removable);
	}

	public void changeBackgroundColor(int color) {
		playable.getPaint().setColor(color);
		//playable.invalidateSelf();
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
						//tell MobileBombSquad a point and its color is selected
						//((MobileBombSquad) this.getContext()).touchPointPressed(point.getColor());
						((MobileBombSquad) this.getContext()).touchPointPressed();
					} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
						point.setSelected(false);
						//tell MobileBombSquad a point and its color is unselected
						//((MobileBombSquad) this.getContext()).touchPointReleased(point.getColor());
						((MobileBombSquad) this.getContext()).touchPointReleased();
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Assumes a touchpoint with that color is available
	 * @param color
	 * @return
	 */
	public boolean allTouchPointsPressed(int color) {
		boolean colorAvailable = false;
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color && !point.isSelected()) {
				return false;
			}
			
			if (point.getColor() == color) {
				colorAvailable = true;
			}
		}
		
		//if (!colorAvailable) {
	//		return false;
		//}
		
		//return true;
		return colorAvailable;
	}
	
	/**
	 * Assumes a touchpoint with that color is available
	 * @param color
	 * @return
	 */
	public boolean allTouchPointsReleased(int color) {
		boolean colorAvailable = false;
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color && point.isSelected()) {
				return false;
			}
			if (point.getColor() == color) {
				colorAvailable = true;
			}
		}
		return colorAvailable;
	}
	
	public void drawExplosion() {
		explosion = true;
		this.invalidate();
	}
	
	public void changePlayer(Player player) {
		changeBackgroundColor(player.getBackgroundColor());
		circle.changeColor(player.getTargetcircleColor());
		circle.generatePosition();
		this.invalidate();
	}
	
}