package layar;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

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



	public Key getId() {
		return id;
	}


	public void setId(Key id) {
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

	public boolean getInFocus() {
		return inFocus;
	}

	public void setInFocus(boolean inFocus) {
		this.inFocus = inFocus;
	}

	public boolean getDoNotIndex() {
		return doNotIndex;
	}

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
	 * @return the full
	 */
	public String getFull() {
		return full;
	}


	/**
	 * @param full the full to set
	 */
	public void setFull(String full) {
		this.full = full;
	}


	/**
	 * @return the reduced
	 */
	public String getReduced() {
		return reduced;
	}


	/**
	 * @param reduced the reduced to set
	 */
	public void setReduced(String reduced) {
		this.reduced = reduced;
	}


	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}


	/**
	 * @param icon the icon to set
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
		if( alt != Integer.MIN_VALUE) result += "\"relativeAlt\": " + relativeAlt +",\n"; 						//TODO Null?
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
