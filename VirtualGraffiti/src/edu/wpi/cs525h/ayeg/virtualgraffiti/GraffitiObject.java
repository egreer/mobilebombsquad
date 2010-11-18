package edu.wpi.cs525h.ayeg.virtualgraffiti;

import javax.microedition.khronos.opengles.GL10;

public class GraffitiObject {

	int color;
	GLObject object;
	
	public GraffitiObject(GLObject obj) {
		this.object = obj;
	}
	
	public void draw(GL10 gl) {
		object.draw(gl);
	}
}
