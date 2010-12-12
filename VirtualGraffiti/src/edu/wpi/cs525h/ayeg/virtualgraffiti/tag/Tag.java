package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

public class Tag {

	String key;
	double lat;
	double lon;
	
	String imageLoc;
	
	public Tag (double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public Tag (String key, double lat, double lon) {
		this(lat, lon);
		this.key = key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public double getLon() {
		return lon;
	}
	
	public void setImagePath(String filepath) {
		imageLoc = filepath;
	}
	
	public String getImagePath() {
		return imageLoc;
	}
}
