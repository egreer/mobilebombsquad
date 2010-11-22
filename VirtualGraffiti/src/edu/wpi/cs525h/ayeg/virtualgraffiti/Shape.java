package edu.wpi.cs525h.ayeg.virtualgraffiti;

import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Cube;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Pyramid;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Sphere;

public enum Shape {
	SPHERE ("Sphere", R.drawable.sphere, new Sphere()),
	CUBE ("Cube", R.drawable.cube, new Cube()),
	PYARAMID ("Triangle", R.drawable.pyramid, new Pyramid());
	
	private String name; 
	private int icon;
	private GLObject object;
	

	Shape(String name, int icon, GLObject object){
		this.name = name;
		this.icon = icon;
		this.object = object;
	}


	public int getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}
	
	public GLObject getObject() {
		return object;
	}
}
