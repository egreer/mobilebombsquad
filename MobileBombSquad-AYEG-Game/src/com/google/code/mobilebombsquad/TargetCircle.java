package com.google.code.mobilebombsquad;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/** The drawable TargetCircle  
 * 
 * @author Eric Greer
 * @author Andrew Yee
 *
 */
public class TargetCircle extends ShapeDrawable{

	int locX, locY;
	int circleWidth = 100, circleHeight = 100;
	static OvalShape circle = new OvalShape();

	/**
	 * Constructor for a target circle
	 */
	public TargetCircle() {
		this(40, Color.MAGENTA);
	}
	
	/** The TouchPoint circle 
	 * 
	 * @param radius	The radius of the TouchPoint
	 * @param color		The color of the circle of the 
	 */
	public TargetCircle(double radius, int color) {
		super(circle);
		circleWidth = (int)(radius * 2);
		circleHeight = (int)(radius * 2);
				
		circle.resize(circleWidth, circleHeight);
		
		this.getPaint().setColor(color);
		this.getPaint().setStyle(Style.STROKE);
		this.getPaint().setStrokeWidth(10.0f);
		generatePosition();
	}
	
	/** Updates the bounds of the circle
	 * 
	 */
	void updateBounds() {
		this.setBounds(locX,locY,locX+circleWidth,locY+circleHeight);
	}
	
	/** Randomly generates the position
	 * 
	 */
	void generatePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - circleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - circleHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
		updateBounds();
	}
	
	/** Changes the color of the target circle 
	 * @param color	The color of the Circle 
	 */
	void changeColor(int color) {
		this.getPaint().setColor(color);
	}
	
	
}
