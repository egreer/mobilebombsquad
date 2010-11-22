package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/** A Layer is a collection of GL objects that have been saved to the screen
 * 
 * @author Andrew Yee
 * @author Eric Greer
 *
 */
public class Layer {

	int layerID;
	List<GraffitiObject> graffitiObjects;
	String name;
	

	/** Constructor for a layer
	 * 
	 * @param layerID	THe ID number of the layer
	 */
	public Layer(int layerID) {
		this.layerID = layerID;
		graffitiObjects = new LinkedList<GraffitiObject>();
	}
	
	/** 
	 * @param obj	Adds the object to the layer
	 */
	public void add(GraffitiObject obj) {
		graffitiObjects.add(obj);
	}
	
	/** Draws the layer onto the gl
	 * 
	 * @param gl	The GL to draw onto 
	 */
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
