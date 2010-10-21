package com.google.code.mobilebombsquad;

public class Player {
	int touchpointColor;
	int backgroundColor;
	
	Player(int touchpointColor, int backgroundColor) {
		this.touchpointColor = touchpointColor;
		this.backgroundColor = backgroundColor;
	}
	
	public int getTouchpointColor() {
		return touchpointColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}
	
}
