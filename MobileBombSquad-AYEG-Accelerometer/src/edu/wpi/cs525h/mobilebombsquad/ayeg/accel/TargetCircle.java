package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TargetCircle {

	OvalShape circle;
	Drawable drawable;
	//Paint paint;
	
	int locX, locY;
	int circleWidth = 100, circleHeight = 100;
	
	public TargetCircle() {
		circle = new OvalShape();
		circle.resize(circleWidth, circleHeight);

		drawable = new ShapeDrawable(circle);
		
		((ShapeDrawable) drawable).getPaint().setColor(0x00000000);
		
		initializePosition();
		drawable.setBounds(locX, locY, locX+circleWidth, locY+circleHeight);
	}
	
	Drawable getDrawable() {
		return drawable;
	}
	
	/*Paint getPaint() {
		return paint;
	}*/
	
	void initializePosition() {
		int maxWidth = PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX - circleWidth;
		int maxHeight = PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY - circleHeight;
		int x =(int)(Math.random() * PlayableSurfaceView.WIDTH + PlayableSurfaceView.OFFSETX);
		int y =(int)(Math.random() * PlayableSurfaceView.HEIGHT + PlayableSurfaceView.OFFSETY);
		locX = x > maxWidth ? maxWidth : x;  
		locY = y > maxHeight ? maxHeight : y;
	}
	
	void updatePosition(int locX, int locY) {
		this.locX = locX;
		this.locY = locY;
	}
	
	
	
}
