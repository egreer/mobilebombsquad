package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Layer {

	int layerID;
	List<GraffitiObject> graffitiObjects;
	String name;
	


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

	/**
	 * @return The name of the layer
	 */
	public String getName() {
		return name;
	}

	/** Sets the name of the layer
	 * 
	 * @param name	The name to set to
	 */
	public void setName(String name) {
		this.name = name;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {	
		String returner = layerID + ": ";
		
		if (name == null){
			
			for(GraffitiObject obj : graffitiObjects) returner += obj.toString() + " ";
			
		}else returner += name;
		
		return returner;
	}
	
}
