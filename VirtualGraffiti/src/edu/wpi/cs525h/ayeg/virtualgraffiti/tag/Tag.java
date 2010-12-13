package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

/**
 * Contains information for a particular tag
 * 
 * @author Andrew Yee
 *
 */
public class Tag {

	double lat;
	double lon;
	int alt;
	String attribution;
	String title;
	
	String imageLoc;
	
	public Tag (double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public Tag (double lat, double lon, double alt) {
		this(lat, lon);
		this.alt = (int) Math.rint(alt);
	}
	
	public Tag (double lat, double lon, double alt, String attribution, String title) {
		this(lat, lon, alt);
		this.attribution = attribution;
		this.title = title;
		//this.alt = (int) Math.rint(altitude);
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

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

}
