package edu.wpi.cs525h.ayeg.virtualgraffiti;

public enum Shape {
	SPHERE ("Sphere", R.drawable.sphere),
	CUBE ("Cube", R.drawable.cube),
	PYARAMID ("Triangle", R.drawable.pyramid);
	
	private String name; 
	private int icon;
	
	Shape(String name, int icon){
		this.name = name;
		this.icon = icon;
	}


	public int getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}
}
