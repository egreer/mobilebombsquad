package layar;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**POI are the Graffiti objects and their information about the location
 *  
 * @author Eric Greer
 *
 */
/**
 * @author Eric Greer
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class POI {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id; //PRIMARY KEY  (`id`) `id` varchar(255) NOT NULL,

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
	boolean inFocus = false;	 // `inFocus` tinyint(1) default '0',

	@Persistent
	boolean doNotIndex = false; //  `doNotIndex` tinyint(1) default '0',

	@Persistent
	boolean showSmallBiw = true; //	`showSmallBiw` tinyint(1) default '1',

	@Persistent
	boolean showBiwOnClick = true; //  `showBiwOnClick` tinyint(1) default '1',

	
    //"object": {  
    String baseURL = "http://virtual-graffiti.appspot.com/image?"; //    "baseURL": "http://layar3d.jsource.nl/", 
    String full;    //    "full": "ghost.l3d",
    String reduced; //    "reduced": "ghost_reduced.l3d", 
    String icon;	//    "icon": "icon1.png",
    int size;		//    "size": 2 
    //  }, 

    //"transform": { "rel": true, "angle": 45, "scale": 5 },   
	boolean rel = true;
	double angle = 0;
	double scale = 1;
	
	public POI(/*String id,*/ double lat, double lon){
		//this.id = id;
		this.lat = lat;
		this.lon = lon;
	}


	/**
	 * @return	Returns the ID of the Point
	 */
	public Key getId() {
		return id;
	}

	/**
	 * @param id	The Key to set for the Point 
	 */
	public void setId(Key id) {
		this.id = id;
	}

	/**
	 * @return	Returns the author of the Point
	 */
	public String getAttribution() {
		return attribution;
	}

	/**
	 * @param attribution	Sets the author
	 */
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	/**
	 * @return	Returns the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title	The title to set for the point
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return	The Latitude of the point 
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat	The latitude to set for the point
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return	The Longitude of the point
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * @param lon	The Longitude to set for the Point
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return	Returns the image URL
	 */
	public String getImageURL() {
		return imageURL;
	}

	/**
	 * @param imageURL	The URL to set for the image
	 */
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

	/**
	 * @return	The type of the point
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type	The type of the point
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return	The dimension of the point
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension	The dimension to set to
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return	The altitude of the point 
	 */
	public int getAlt() {
		return alt;
	}

	/**
	 * @param alt	The Altitude to set for the point
	 */
	public void setAlt(int alt) {
		this.alt = alt;
	}

	/** 
	 * @return The relative altto the current position
	 * Should not be stored
	 */
	public int getRelativeAlt() {
		return relativeAlt;
	}

	/**
	 * @param relativeAlt	Sets the relative altitude to this
	 */
	public void setRelativeAlt(int relativeAlt) {
		this.relativeAlt = relativeAlt;
	}

	/**
	 * @return	The distance to the point 
	 * Should not be stored
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance	Sets the distance of the point
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @return	whether the point is in focus
	 */
	public boolean getInFocus() {
		return inFocus;
	}

	/**
	 * @param inFocus	is the point in focus
	 */
	public void setInFocus(boolean inFocus) {
		this.inFocus = inFocus;
	}

	/**
	 * @return	is this point indexed?
	 */
	public boolean getDoNotIndex() {
		return doNotIndex;
	}

	/**
	 * @param doNotIndex	Sets whether the point is indexed
	 */
	public void setDoNotIndex(boolean doNotIndex) {
		this.doNotIndex = doNotIndex;
	}

	public boolean getShowSmallBiw() {
		return showSmallBiw;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}


	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}


	/**
	 * @return the full URL
	 */
	public String getFull() {
		return full;
	}


	/**
	 * @param full The full URL to set
	 */
	public void setFull(String full) {
		this.full = full;
	}


	/**
	 * @return The reduced URL
	 */
	public String getReduced() {
		return reduced;
	}


	/**
	 * @param reduced The reduced URL to set
	 */
	public void setReduced(String reduced) {
		this.reduced = reduced;
	}


	/**
	 * @return the icon URL
	 */
	public String getIcon() {
		return icon;
	}


	/**
	 * @param icon The icon URL to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}


	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}


	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}


	/**
	 * @return the rel
	 */
	public boolean isRel() {
		return rel;
	}


	/**
	 * @param rel the rel to set
	 */
	public void setRel(boolean rel) {
		this.rel = rel;
	}


	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}


	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}


	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}


	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}


	public void setShowSmallBiw(boolean showSmallBiw) {
		this.showSmallBiw = showSmallBiw;
	}

	public boolean getShowBiwOnClick() {
		return showBiwOnClick;
	}

	public void setShowBiwOnClick(boolean showBiwOnClick) {
		this.showBiwOnClick = showBiwOnClick;
	}
	
	/**
	 * @param name	Name of the image
	 */
	public void setImage(String name){
		this.setFull("name="+name+"&quality=full");
		this.setReduced("name="+name+"&quality=reduced");
		this.setIcon("name="+name+"&quality=icon");
		this.setImageURL(this.getBaseURL() + this.getFull());
		
		
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
	
	/**
	 * @return Returns a JSON String representation of the object. 
	 */
	public String toJSONString(){
		String result = "{";
		
		//Object JSON Dictionary
		result += "\"object\": { \n";  
	    result += "\t\"baseURL\": \"" + baseURL + "\",\n"; 
	    result += "\t\"full\": \"" + full + "\",\n";
	    result += "\t\"reduced\": \"" + reduced + "\",\n";
	    result += "\t\"icon\": \""+ icon + "\",\n";
	    result += "\t\"size\": " +  size;
	    result += "},\n";
	    
	    //Transform JSON Dictionary
	    result += "\"transform\": { \"rel\":" + rel + ", \"angle\": " + angle +", \"scale\": " + scale + " },\n";   

	    
	    result += "\"id\": \"" + id.getId() + "\",\n";
	    result += "\"attribution\": \"" + attribution + "\",\n";
		result += "\"title\": \"" + title + "\",\n";
		result += "\"lat\": " + lat * 10e5  + ",\n";			//TODO Need to convert?
		result += "\"lon\": " + lon * 10e5 + ",\n";			//TODO Need to convert?
		if(imageURL != null) result += "\"imageURL\": \"" + imageURL + "\",\n";	
		if(line4 != null) result += "\"line4\": \"" + line4 + "\",\n";			
		if(line3 != null) result += "\"line3\": \"" + line3 + "\",\n";
		if(line2 != null) result += "\"line2\": \"" + line2 + "\",\n";			
		result += "\"type\": " + type + ",\n";
		result += "\"dimension\": " + dimension + ",\n";
		if( alt != Integer.MIN_VALUE) result += "\"alt\": " + alt +",\n";
		if(relativeAlt != Integer.MIN_VALUE) result += "\"relativeAlt\": " + relativeAlt +",\n"; 						//TODO Null?
		result += "\"distance\": " + distance +",\n"; 						
		result += "\"inFocus\": " + inFocus +",\n";
		result += "\"doNotIndex\": " + doNotIndex +",\n";
		result += "\"showSmallBiw\": " + showSmallBiw +",\n";
		result += "\"showBiwOnClick\": " + showBiwOnClick +",\n";
		result += "\"actions\": [] \n";
		result += "}";
		return result;
	}
	
}
