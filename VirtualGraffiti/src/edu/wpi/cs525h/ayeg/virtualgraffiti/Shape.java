package edu.wpi.cs525h.ayeg.virtualgraffiti;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.pub.SimpleBox;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Pyramid;

public enum Shape {
	SPHERE ("Sphere", R.drawable.sphere, new SimpleBox()),
	CUBE ("Cube", R.drawable.cube, new SimpleBox()),
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
