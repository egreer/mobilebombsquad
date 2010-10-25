package com.google.code.mobilebombsquad;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**	The Drawable TouchPoints on the screen
 * 
 * @author Eric Greer
 * @author Andrew Yee	
 */
public class TouchPoint extends ShapeDrawable{

	final static int NORM = 0xFF;
	final static int SELECT = 0x55;
	
	int locX, locY;
	double radius;
	int touchHeight, touchWidth;
	
	static OvalShape circle = new OvalShape();
	
	private boolean selected = false;
	private int color;
	
	/**
	 * @return	Returns the color of the point
	 */
	public int getColor() {
		return color;
	}

	/** Default TouchPoint with Red
	 * 
	 */
	public TouchPoint() {
		this(Color.RED);
	}
	
	/** Constructor for the TouchPoint
	 * 
	 * @param color		The color of the TouchPoint
	 */
	public TouchPoint(int color) {
		this(60, color);
	}
	
	/**	Constructor for the TouchPoint
	 * 
	 * @param radius	The radius of the TouchPoint
	 * @param color		The color of the TouchPoint
	 */
	public TouchPoint(double radius, int color) {
		super(circle);
		this.color = color;
		this.radius = radius;
		touchWidth = (int)(radius * 2);
		touchHeight = (int)(radius * 2);
				
		circle.resize(touchWidth, touchHeight);
		randomizePosition();
		this.getPaint().setColor(color);
		this.setBounds(locX, locY, locX + touchWidth, locY + touchHeight);
	}

	/** Randomizes the position the of the TouchPoint  
	 * 
	 */
	void randomizePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - touchWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - touchHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
	}

	void changePosition() {
		randomizePosition();
		this.setBounds(locX, locY, locX + touchWidth, locY + touchHeight);
	}
	
	/**
	 * @return If the TouchPoint is selected.
	 */
	boolean isSelected() {
		return selected;
	}
	
	/**	Sets whether the bubble is updated
	 * 
	 * @param newStatus		Boolean
	 */
	void setSelected(boolean newStatus) {
		selected = newStatus;
		
		if(selected){
			this.getPaint().setAlpha(SELECT);
		}else{
			this.getPaint().setAlpha(NORM);
		}
	}
	
	/**
	 * Checks if given (x,y) point is in the touch point area
	 * @param x
	 * @param y
	 * @return
	 */
	boolean contains(int x, int y) {
		return this.getBounds().contains(x, y);
	}
	
	boolean overlaps(TouchPoint point) {
		return this.getBounds().intersect(point.getBounds());
	}
	
	void decrementSize() {
		radius -= 5.0;
		touchWidth = (int)(radius * 2);
		touchHeight = (int)(radius * 2);
		
		circle.resize(touchWidth, touchHeight);
	}
}
