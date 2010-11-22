package edu.wpi.cs525h.ayeg.virtualgraffiti;

import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Cube;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Pyramid;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Sphere;

/** The Listing of shapes, contains all the information about shapes in Graffiti Builder
 * 
 * @author Eric Greer
 * @author Andrew Yee
 */
public enum Shape {
	SPHERE ("Sphere", R.drawable.sphere, new Sphere()),
	CUBE ("Cube", R.drawable.cube, new Cube()),
	PYARAMID ("Triangle", R.drawable.pyramid, new Pyramid());
	
	private String name; 
	private int icon;
	private GLObject object;
	
	/** Constructor for a Shape
	 * 
	 * @param name	The name of the Shape
	 * @param icon	The menu icon for the shape (as specified in XML)
	 * @param object The GL Object of the shape 
	 */
	Shape(String name, int icon, GLObject object){
		this.name = name;
		this.icon = icon;
		this.object = object;
	}

	/**
	 * @return The Icon ID for this shape 
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * @return	The string name of the Shape
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return	The GL Object of the Shape
	 */
	public GLObject getObject() {
		return object;
	}
}
