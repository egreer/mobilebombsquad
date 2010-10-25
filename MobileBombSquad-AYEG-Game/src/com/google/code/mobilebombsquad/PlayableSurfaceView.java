package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;
	
	int circlesize = 40;
	
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
		circle = new TargetCircle(circlesize, player.getTargetcircleColor());
		
		explosion = false;
	}

	protected void onDraw(Canvas canvas) {
		if (explosion) {
			this.getContext().getResources().getDrawable(R.drawable.explode).draw(canvas);
		} else {
			playable.draw(canvas);
			circle.draw(canvas);
			bubble.draw(canvas);
			drawTouchPoints(canvas);
		}
	}
	
	boolean checkBubbleCircle() {
		return circle.getBounds().contains(bubble.getBounds());
	}
	
	
	void drawTouchPoints(Canvas canvas) {
		for (TouchPoint point : touchpoints) {
			if (point.isVisible()) {
				point.draw(canvas);
			}
		}
	}
	
	void addTouchPoint(int color){
		TouchPoint t = new TouchPoint(color);
		t.setVisible(false, false);
		touchpoints.add(t);
	}

	void enableTouchPoint(int color) {
		//touchpoints.add(new TouchPoint(color));//
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				point.setVisible(true,true);
			}
		}
	}
	
	void disableTouchPoints(int color) {
		/*ArrayList<TouchPoint> removable = new ArrayList<TouchPoint>();
		
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				removable.add(point);
			}
		}
		
		touchpoints.removeAll(removable);*/
		
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				point.setVisible(false, false);
			}
		}
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

			for (TouchPoint point : touchpoints) {
				if (point.isVisible()) {
					if (action == MotionEvent.ACTION_MOVE){
						if (point.contains(x, y)){
							if(!point.isSelected()){
								point.setSelected(true);
								((MobileBombSquad) this.getContext()).touchPointPressed();
							}						
						}
					} else if (point.contains(x, y)) {  
						if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
							point.setSelected(true);
							((MobileBombSquad) this.getContext()).touchPointPressed();
						} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
							point.setSelected(false);
							((MobileBombSquad) this.getContext()).touchPointReleased();
							
						}
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
	public boolean isThisPointSelected(int color) {
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color && point.isSelected()) {
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Assumes a touchpoint with that color is available
	 * @param color
	 * @return
	 */
	public boolean allTouchPointsReleased(int color) {
		boolean colorAvailable = false;
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				if (point.isSelected()) {
					return false;
				}
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
		//circle.changeColor(player.getTargetcircleColor());
		//circle.generatePosition();
		circle = new TargetCircle(circlesize, player.getTargetcircleColor());
		this.invalidate();
	}
	
}