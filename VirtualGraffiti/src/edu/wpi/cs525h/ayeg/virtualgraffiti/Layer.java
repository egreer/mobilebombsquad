package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Layer {

	int layerID;
	List<GraffitiObject> graffitiObjects;
	
	public Layer(int layerID) {
		this.layerID = layerID;
		graffitiObjects = new LinkedList<GraffitiObject>();
	}
	
	public void add(GraffitiObject obj) {
		graffitiObjects.add(obj);
	}
	
	public void draw(GL10 gl) {
		for (GraffitiObject obj : graffitiObjects) {
			if (obj.isSaved()) {
				obj.draw(gl);
			}
			
		}
	}
}
