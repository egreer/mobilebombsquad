package com.google.code.mobilebombsquad;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TriggerBubble extends ShapeDrawable{

	final static int COLOR = 0xFF00FF00;
	
	int locX, locY;
	int bubbleWidth, bubbleHeight;
	static OvalShape bubble = new OvalShape();
	
	public TriggerBubble() {
		this(12.5);
	}
	
	public TriggerBubble(double radius) {
		super(bubble);
	
		bubbleWidth = (int) (radius * 2);
		bubbleHeight = (int) (radius * 2);
			
		bubble.resize(bubbleWidth, bubbleHeight);
		
		
		this.getPaint().setColor(COLOR);		
		initializePosition();
		updateBounds();
	}
	
	void updateBounds() {
		this.setBounds(locX, locY, locX+bubbleWidth, locY+bubbleHeight);
	}
	
	void initializePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - bubbleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - bubbleHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
	}
	
	void updatePosition(int locX, int locY) {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - bubbleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - bubbleHeight;
			
		this.locX = locX > maxWidth ? maxWidth : (locX < PlayableSurfaceView.OFFSETX ? PlayableSurfaceView.OFFSETX : locX);  
		this.locY = locY > maxHeight ? maxHeight : (locY < PlayableSurfaceView.OFFSETY ? PlayableSurfaceView.OFFSETY : locY);
		updateBounds();
	}
}
