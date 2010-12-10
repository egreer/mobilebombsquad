package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

public class Tag {

	String key;
	double lat;
	double lon;
	
	String imageLoc;
	
	public Tag (String key, double lat, double lon) {
		this.key = key;
		this.lat = lat;
		this.lon = lon;
	}
	
	public void setImagePath(String filepath) {
		imageLoc = filepath;
	}
	
	public String getImagePath() {
		return imageLoc;
	}
}
