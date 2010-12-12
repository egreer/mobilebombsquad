package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

public class Tag {

	String key;
	double lat;
	double lon;
	double alt;
	String attribution;
	String title;
	
	String imageLoc;
	
	public Tag (double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public Tag (double lat, double lon, double alt) {
		this(lat, lon);
		this.alt = alt;
	}
	
	public Tag (String key, double lat, double lon, String attribution, String title) {
		this(lat, lon);
		this.key = key;
		this.attribution = attribution;
		this.title = title;
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
	

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
