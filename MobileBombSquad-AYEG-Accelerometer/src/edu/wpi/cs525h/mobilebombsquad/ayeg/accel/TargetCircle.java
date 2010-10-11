package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TargetCircle {

	final static int COLOR = 0xFF000000;
	
	Drawable drawable;
	
	int locX, locY;
	int circleWidth = 100, circleHeight = 100;
	
	public TargetCircle() {
		this(50);
	}
	
	public TargetCircle(double radius) {
		circleWidth = (int)(radius * 2);
		circleHeight = (int)(radius * 2);
		
		OvalShape circle = new OvalShape();
		circle.resize(circleWidth, circleHeight);
		drawable = new ShapeDrawable(circle);
		
		((ShapeDrawable) drawable).getPaint().setColor(COLOR);
		generatePosition();
	}
	
	Drawable getDrawable() {
		return drawable;
	}
	
	void updateBounds() {
		drawable.setBounds(locX,locY,locX+circleWidth,locY+circleHeight);
	}
	
	Rect getBounds() {
		return drawable.getBounds();
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
	
	
}
