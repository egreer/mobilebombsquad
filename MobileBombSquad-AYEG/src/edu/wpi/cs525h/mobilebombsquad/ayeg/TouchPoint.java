package edu.wpi.cs525h.mobilebombsquad.ayeg;

import android.graphics.drawable.Drawable;

public class TouchPoint {

	Drawable touchpoint;
	int locX, locY;
	int touchHeight, touchWidth;
	
	public TouchPoint(Drawable touchpoint) {
		this.touchpoint = touchpoint;
		touchWidth = touchpoint.getIntrinsicWidth()*2;
		touchHeight = touchpoint.getIntrinsicHeight()*2;
		randomizePosition();
		touchpoint.setBounds(locX, locY, locX + touchWidth, locY + touchHeight);
	}

	/** 
	 * TODO: specify upper bound
	 */
	void randomizePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - touchWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - touchHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		//int x = 300;
		//int y = 600;
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
	}

}
