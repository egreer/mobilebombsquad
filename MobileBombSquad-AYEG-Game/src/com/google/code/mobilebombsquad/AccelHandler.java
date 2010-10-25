package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;

/** Accelerometer Handler is responsible for updating the GUI 
 * with data obtained from the accelerometer
 * 
 * @author Eric Greer
 * @author Andrew Yee
 *
 */
public class AccelHandler {

	MobileBombSquad context;
	PlayableSurfaceView view;

	boolean conditionMet = false;
	ArrayList<Point> points = new ArrayList<Point>();
	
	/** Constructor for the Accelerometer handler
	 * 
	 * @param context	MobileBombSquad
	 * @param view		The PlayableSurfaceView
	 */
	public AccelHandler(Context context, PlayableSurfaceView view) {
		this.context = (MobileBombSquad) context;
		this.view = view;
				
	}
	
	/** Updates the bubble with the latest position from the accelerometer
	 * 
	 * @param x		The new x coordinate
	 * @param y		The new y coordinate
	 * @param z		The new z coordinate
	 */
	void updateBubble(float x, float y, float z) {
		points.add(new Point(x, y, z));
		
		if (points.size() == 3){
			float avgX = 0;
			float avgY = 0;
			
			for (Point point : points){
				avgX += point.x;
				avgY += point.y;
			}
			
			avgX = avgX / points.size();
			avgY = avgY / points.size();
			
			int height = view.getHeight();
			int width = view.getWidth();
			
			double xProportion = avgX / (Math.PI / 2.5);
			double yProportion = avgY / (Math.PI / 2.5);
			
			int newX = (int) (width/2.0 + xProportion*(width/2.0));
			int newY = (int) (height/2.0 + yProportion*(height/2.0));
			
			points.clear();
			view.bubble.updatePosition(newX, newY);
			view.invalidate();
			
			if (checkCondition()) {
				if (!conditionMet) {
					conditionMet = true;
					context.isBubbleInCircle(conditionMet);
				}
			} else {
				conditionMet = false;
				context.isBubbleInCircle(conditionMet);
			}
		}
	}
	
	/** Checks to see if the bubble is in the circle
	 * 
	 * @return True if bubble is in circle, false otherwise
	 */
	boolean checkCondition() {
		return view.checkBubbleCircle();
	}

	/** Private class used for storing point information
	 * 
	 * @author Eric Greer
	 * @author Andrew Yee
	 *
	 */
	private class Point {
		float x;
		float y;
		@SuppressWarnings("unused")
		float z;
		Point(float x, float y, float z){
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}

