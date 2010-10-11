package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TriggerBubble {

	final static int COLOR = 0xFF00FF00;
	
	OvalShape bubble;
	Drawable drawable;
	
	int locX, locY;
	int bubbleWidth = 25, bubbleHeight = 25;
	
	public TriggerBubble() {
		bubble = new OvalShape();
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
		this.locX = locX;
		this.locY = locY;
		updateBounds();
	}
	
}
