package com.google.code.mobilebombsquad;

/** The Player of a game  
 * 
 * @author Eric Greer
 * @author Andrew Yee
 */
public class Player {
	int touchpointColor;
	int backgroundColor;
	int targetcircleColor;
	
	/** Creates a player object for MobileBombSquad
	 * 
	 * @param touchpointColor	The TouchPoint Color 
	 * @param backgroundColor	The Background Color
	 * @param targetcircleColor	The Target Circle Color
	 */
	Player(int touchpointColor, int backgroundColor, int targetcircleColor) {
		this.touchpointColor = touchpointColor;
		this.backgroundColor = backgroundColor;
		this.targetcircleColor = targetcircleColor;
	}
	
	/**
	 * 
	 * @return	The color of the TouchPoint 
	 */
	public int getTouchpointColor() {
		return touchpointColor;
	}

	/**
	 * 
	 * @return	The color of the Background
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * 
	 * @return The target Circle Color
	 */
	public int getTargetcircleColor() {
		return targetcircleColor;
	}
	
	
}
