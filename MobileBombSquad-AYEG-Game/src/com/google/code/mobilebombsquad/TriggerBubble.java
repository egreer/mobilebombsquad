package com.google.code.mobilebombsquad;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/** The drawable green trigger bubble 
 * 
 * @author Eric Greer
 * @author Andrew Yee
 */
public class TriggerBubble extends ShapeDrawable{

	int locX, locY;
	int bubbleWidth, bubbleHeight;
	static OvalShape bubble = new OvalShape();
	
	/** Default constructor with 12.5 px radius
	 * 
	 */
	public TriggerBubble() {
		this(12.5);
	}
	
	/** The constructor for a trigger bubble with a specific. 
	 * 
	 * @param radius	The radius of the bubble to create
	 */
	public TriggerBubble(double radius) {
		super(bubble);
	
		bubbleWidth = (int) (radius * 2);
		bubbleHeight = (int) (radius * 2);
			
		bubble.resize(bubbleWidth, bubbleHeight);
		
		this.getPaint().setColor(Color.GREEN);		
		initializePosition();
		updateBounds();
	}
	
	/** Updates the bounds of the object   
	 * 
	 */
	void updateBounds() {
		this.setBounds(locX, locY, locX+bubbleWidth, locY+bubbleHeight);
	}
	
	/**
	 * Initializes the position to a random location
	 */
	void initializePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - bubbleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - bubbleHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
	}
	
	/** Updates the position with a new location x and y 
	 * 
	 * @param locX	The x location 
	 * @param locY	The y location
	 */
	void updatePosition(int locX, int locY) {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - bubbleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - bubbleHeight;
			
		this.locX = locX > maxWidth ? maxWidth : (locX < PlayableSurfaceView.OFFSETX ? PlayableSurfaceView.OFFSETX : locX);  
		this.locY = locY > maxHeight ? maxHeight : (locY < PlayableSurfaceView.OFFSETY ? PlayableSurfaceView.OFFSETY : locY);
		updateBounds();
	}
}
