package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TriggerBubble {

	final static int COLOR = 0xFF00FF00;
	
	Drawable drawable;
	
	int locX, locY;
	int bubbleWidth, bubbleHeight;
	
	public TriggerBubble() {
		this(12.5);
	}
	
	public TriggerBubble(double radius) {
		bubbleWidth = (int) (radius * 2);
		bubbleHeight = (int) (radius * 2);
		
		OvalShape bubble = new OvalShape();
		bubble.resize(bubbleWidth, bubbleHeight);
		drawable = new ShapeDrawable(bubble);
		
		((ShapeDrawable) drawable).getPaint().setColor(COLOR);		
		initializePosition();
		updateBounds();
	}
	
	Drawable getDrawable() {
		return drawable;
	}
	
	void updateBounds() {
		drawable.setBounds(locX, locY, locX+bubbleWidth, locY+bubbleHeight);
	}
	
	Rect getBounds() {
		return drawable.getBounds();
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
