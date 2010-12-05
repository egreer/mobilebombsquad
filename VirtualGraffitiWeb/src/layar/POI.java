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
	double distance = 0; //  `distance` decimal(20,10) NOT NULL,
	
	@Persistent
	String imageURL; //  `imageURL` varchar(255) default NULL,

	@Persistent
	String line4; 	// `line4` varchar(150) default NULL,

	@Persistent
	String line3;  //`line3` varchar(150) default NULL,

	@Persistent
	String line2; //`line2` varchar(150) default NULL,

	@Persistent
	int type = 0;  //  `type` int(11) default '0',

	@Persistent
	int dimension = 1; // `dimension` int(1) default '1',

	@Persistent
	int alt = Integer.MIN_VALUE;     //Altitude	  `alt` int(10) default NULL,

	@Persistent
	int relativeAlt = Integer.MIN_VALUE; //`relativeAlt` int(10) default NULL,


	@Persistent
	int inFocus = 0;	 // `inFocus` tinyint(1) default '0',

	@Persistent
	int doNotIndex = 0; //  `doNotIndex` tinyint(1) default '0',

	@Persistent
	int showSmallBiw = 1; //	`showSmallBiw` tinyint(1) default '1',

	@Persistent
	int showBiwOnClick = 1; //  `showBiwOnClick` tinyint(1) default '1',

	
    //"object": {  
    String baseURL; //    "baseURL": "http://layar3d.jsource.nl/", 
    String full;    //    "full": "ghost.l3d",
    String reduced; //    "reduced": "ghost_reduced.l3d", 
    String icon;	//    "icon": "icon1.png",
    int size;		//    "size": 2 
    //  }, 

    //"transform": { "rel": true, "angle": 45, "scale": 5 },   
	boolean rel = true;
	double angle = 0;
	double scale = 1;
	
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
	
	
	public String toJSONString(){
		String result = "{";
		
		//Object JSON Dictionary
		result += "\"object\": { \n";  
	    result += "\t\"baseURL\": \"" + "http://site.layar.com/wp-content/uploads/2010/02/" /*baseURL*/ + "\",\n"; 
	    result += "\t\"full\": \"" + "Layar-logo-shine-copy1.png" /*full*/ + "\",\n";
	    result += "\t\"reduced\": \"" + reduced + "\",\n";
	    result += "\t\"icon\": \""+ icon + "\",\n";
	    result += "\t\"size\": " +  size + ",\n";
	    result += "},\n";
	    
	    //Transform JSON Dictionary
	    result += "\"transform\": { \"rel\":" + rel + ", \"angle\": " + angle +", \"scale\": " + scale + " },\n";   

	    
	    result += "\"id\": \"" + id + "\",\n";
	    result += "\"attribution\": \"" + attribution + "\",\n";
		result += "\"title\": \"" + title + "\",\n";
		result += "\"lat\": " + lat * 10e6  + ",\n";			//TODO Need to convert?
		result += "\"lon\": " + lon * 10e6 + ",\n";			//TODO Need to convert?
		if(imageURL != null) result += "\"imageURL\": \"" + imageURL + "\",\n";	
		if(line4 != null) result += "\"line4\": \"" + line4 + "\",\n";			
		if(line3 != null) result += "\"line3\": \"" + line3 + "\",\n";
		if(line2 != null) result += "\"line2\": \"" + line2 + "\",\n";			
		result += "\"type\": " + type + ",\n";
		result += "\"dimension\": " + dimension + ",\n";
		if( alt != Integer.MIN_VALUE) result += "\"alt\": " + alt +",\n";
		if( alt != Integer.MIN_VALUE) result += "\"relativeAlt\": " + relativeAlt +",\n"; 						//TODO Null?
		result += "\"distance\": " + distance +",\n"; 						
		result += "\"inFocus\": " + inFocus +",\n";
		result += "\"doNotIndex\": " + doNotIndex +",\n";
		result += "\"showSmallBiw\": " + showSmallBiw +",\n";
		result += "\"showBiwOnClick\": " + showBiwOnClick +",\n";
		result += "}";
		return result;
	}
	
}
