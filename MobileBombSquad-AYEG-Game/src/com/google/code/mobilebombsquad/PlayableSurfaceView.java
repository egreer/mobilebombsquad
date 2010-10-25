package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/** The playable surface of the game
 * 
 * @author Eric Greer
 * @author Andrew Yee
 */
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
	
	/** Creates a playable surface view
	 * 		
	 * @param context	The context to draw on
	 * @param player	The current player 
	 */
	public PlayableSurfaceView(Context context, Player player) {
		super(context);
		
		playable = new ShapeDrawable(new RectShape());
		playable.getPaint().setColor(player.getBackgroundColor());
		playable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		circle = new TargetCircle(circlesize, player.getTargetcircleColor());
		
		explosion = false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		playable.draw(canvas);
		circle.draw(canvas);
		bubble.draw(canvas);
		drawTouchPoints(canvas);
		if (explosion) {
			Drawable explode = this.getContext().getResources().getDrawable(R.drawable.explosionfordroid);
			explode.setBounds(OFFSETX, OFFSETY, OFFSETX+explode.getIntrinsicWidth(), OFFSETY+explode.getIntrinsicHeight());
			explode.draw(canvas);
		}
	}
	
	/** Check to see if the target circle contains the bubble
	 * 
	 * @return Whether the point is drawn in the bubble
	 */
	boolean checkBubbleCircle() {
		return circle.getBounds().contains(bubble.getBounds());
		//return circle.getBounds().intersect(bubble.getBounds());
	}
	
	/**
	 *  Draws all the TouchPoints on the 
	 * @param canvas	The canvas to draw the points on
	 */
	void drawTouchPoints(Canvas canvas) {
		for (TouchPoint point : touchpoints) {
			if (point.isVisible()) {
				point.draw(canvas);
			}
		}
	}
	
	/** Adds a new touch point of the specified color
	 * 
	 * @param color	The color of the TouchPoint to add
	 */
	void addTouchPoint(int color){
		TouchPoint t = new TouchPoint(color);
		t.setVisible(false, false);
		touchpoints.add(t);
	}

	/** Enables the TouchPoint of the specific color
	 * 
	 * @param color The color of the TouchPoint to enable
	 */
	void enableTouchPoint(int color) {
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				point.randomizePosition();
				while (isItOverlapping(point)) {
					point.randomizePosition();
				}
				point.setVisible(true,false);
			}
		}
	}
	
	/** Disables the TouchPoint of the specific color  
	 * 
	 * @param The color of the TouchPoint to disable 
	 */
	void disableTouchPoints(int color) {
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				point.setVisible(false, false);
			}
		}
	}

	/** Changes the background color
	 * 
	 * @param color	The color to set the background to
	 */
	public void changeBackgroundColor(int color) {
		playable.getPaint().setColor(color);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
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
						} else{
							if (point.isSelected()){
								int contained = 0;
								int hisX;
								int hisY;
								for (int h = 0 ; h < event.getHistorySize() ; h++){
									hisX  =(int) event.getHistoricalX(i, event.getHistorySize()-h);
									hisY = (int) event.getHistoricalY(i, event.getHistorySize()-h);
									
									if (point.contains(hisX, hisY) && point.isSelected()){
										contained++;
									}
								}
								
								if ( 3 < event.getHistorySize() && contained < (event.getHistorySize() / 2 ) ){
									point.setSelected(false);
									((MobileBombSquad) this.getContext()).touchPointReleased();	
								}
							}
						}
					}
					
					if (point.contains(x, y)) {  
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
		Toast.makeText(this.getContext(), "Number of Touchpoints" + touchpoints.size(), Toast.LENGTH_SHORT).show();
		
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				return point.isSelected();
			}
		}
		return false;
	}
	
	public void drawExplosion() {
		explosion = true;
		this.invalidate();
	}
	
	/** The user changes the colors to the next player
	 * 
	 * @param player The next player
	 */
	public void changePlayer(Player player) {
		changeBackgroundColor(player.getBackgroundColor());
		circle = new TargetCircle(circlesize, player.getTargetcircleColor());
		this.invalidate();
	}
	
	/** Checks if the TouchPoint overlaps any TouchPoint 
	 * 
	 * @param point	The point to check
	 * @return		True if any TouchPoint has updated 
	 */
	boolean isItOverlapping(TouchPoint point) {
		for (TouchPoint tp : touchpoints) {
			if (tp.isVisible()) {	
				if (point.overlaps(tp)) {
					return true;
				}
			}
		}
		return false;
		
	}
	
}