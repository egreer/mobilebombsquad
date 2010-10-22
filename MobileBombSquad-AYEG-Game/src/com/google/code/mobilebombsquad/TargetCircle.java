package com.google.code.mobilebombsquad;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TargetCircle extends ShapeDrawable{

	//final static int COLOR = Color.MAGENTA;

	
	int locX, locY;
	int circleWidth = 100, circleHeight = 100;
	static OvalShape circle = new OvalShape();
	
	public TargetCircle() {
		this(40, Color.MAGENTA);
	}
	
	public TargetCircle(double radius, int color) {
		super(circle);
		circleWidth = (int)(radius * 2);
		circleHeight = (int)(radius * 2);
				
		circle.resize(circleWidth, circleHeight);
		
		this.getPaint().setColor(color);
		this.getPaint().setStyle(Style.STROKE);
		generatePosition();
	}
	
	
	void updateBounds() {
		this.setBounds(locX,locY,locX+circleWidth,locY+circleHeight);
	}
	
	void generatePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - circleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - circleHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
		updateBounds();
	}
	
	void changeColor(int color) {
		this.getPaint().setColor(color);
	}
	
	
}
