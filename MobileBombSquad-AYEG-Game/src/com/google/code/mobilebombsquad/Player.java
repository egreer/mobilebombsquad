package com.google.code.mobilebombsquad;

public class Player {
	int touchpointColor;
	int backgroundColor;
	int targetcircleColor;
	
	Player(int touchpointColor, int backgroundColor, int targetcircleColor) {
		this.touchpointColor = touchpointColor;
		this.backgroundColor = backgroundColor;
		this.targetcircleColor = targetcircleColor;
	}
	
	public int getTouchpointColor() {
		return touchpointColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public int getTargetcircleColor() {
		return targetcircleColor;
	}
	
	
}
