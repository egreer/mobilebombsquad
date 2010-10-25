package com.google.code.mobilebombsquad;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TouchPoint extends ShapeDrawable{

	final static int NORM = 0xFF;
	final static int SELECT = 0x15;
	
	int locX, locY;
	int touchHeight, touchWidth;
	
	static OvalShape circle = new OvalShape();
	
	private boolean selected = false;
	private int color;
	
	public int getColor() {
		return color;
	}

	public TouchPoint() {
		this(Color.RED);
	}
	
	public TouchPoint(int color) {
		this(30, color);
	}
	
	public TouchPoint(double radius, int color) {
		super(circle);
		this.color = color;
		touchWidth = (int)(radius * 2);
		touchHeight = (int)(radius * 2);
				
		circle.resize(touchWidth, touchHeight);
		randomizePosition();
		this.getPaint().setColor(color);
		this.setBounds(locX, locY, locX + touchWidth, locY + touchHeight);
	}

	/** 
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

	boolean isSelected() {
		return selected;
	}
	
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
		return this.getBounds().contains(point.getBounds());
	}
}
