package layar;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class POI implements Serializable{
	@Persistent
	private static final long serialVersionUID = -2353794766764992652L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	String id; //PRIMARY KEY  (`id`) `id` varchar(255) NOT NULL,

	@Persistent
	String attribution; //`attribution` varchar(150) default NULL,

	@Persistent
	String title; //  `title` varchar(150) NOT NULL,

	@Persistent
	Double lat; //`lat` decimal(20,10) NOT NULL,

	@Persistent
	Double lon; //  `lon` decimal(20,10) NOT NULL,

	@Persistent
	String imageURL; //  `imageURL` varchar(255) default NULL,

	@Persistent
	String line4; 	// `line4` varchar(150) default NULL,

	@Persistent
	String line3;  //`line3` varchar(150) default NULL,

	@Persistent
	String line2; //`line2` varchar(150) default NULL,

	@Persistent
	int type;  //  `type` int(11) default '0',

	@Persistent
	int dimension; // `dimension` int(1) default '1',

	@Persistent
	int alt;     //Altitude	  `alt` int(10) default NULL,

	@Persistent
	int relativeAlt; //`relativeAlt` int(10) default NULL,

	@Persistent
	double distance; //  `distance` decimal(20,10) NOT NULL,

	@Persistent
	int inFocus;	 // `inFocus` tinyint(1) default '0',

	@Persistent
	int doNotIndex; //  `doNotIndex` tinyint(1) default '0',

	@Persistent
	int showSmallBiw; //	`showSmallBiw` tinyint(1) default '1',

	@Persistent
	int showBiwOnClick; //  `showBiwOnClick` tinyint(1) default '1',

	public POI(String id, double lat, double lon){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

	public int getRelativeAlt() {
		return relativeAlt;
	}

	public void setRelativeAlt(int relativeAlt) {
		this.relativeAlt = relativeAlt;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getInFocus() {
		return inFocus;
	}

	public void setInFocus(int inFocus) {
		this.inFocus = inFocus;
	}

	public int getDoNotIndex() {
		return doNotIndex;
	}

	public void setDoNotIndex(int doNotIndex) {
		this.doNotIndex = doNotIndex;
	}

	public int getShowSmallBiw() {
		return showSmallBiw;
	}

	public void setShowSmallBiw(int showSmallBiw) {
		this.showSmallBiw = showSmallBiw;
	}

	public int getShowBiwOnClick() {
		return showBiwOnClick;
	}

	public void setShowBiwOnClick(int showBiwOnClick) {
		this.showBiwOnClick = showBiwOnClick;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** Calculates the distance between the current point and the points
	 * 
	 * @param lat	The latitude to calculate to
	 * @param lon	The Longitude to calculate to
	 * @return		Returns the distance
	 */
	public double calcDistance(double lat, double lon) {

		double lat1 = lat;  
		double lat2 = this.lat;  
		double lon1 = lon;  
		double lon2 = this.lon;  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
		Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));
		double Radius = 6371000;
		
		return Radius * c;  
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return id + " lat: " + lat + " lon: " + lon + " imageURL: " + imageURL + " Distance: " + distance;
	}
}
