package layar;

/**
 * 
 * @author Eric Greer
 *
 */
public enum Quality {
	FULL ("full", 850, 480),
	REDUCED ("reduced", 427, 240), 
	ICON ("icon", 100, 56);
	
	private String quality;
	private int x;
	private int y;
	
	Quality(String quality, int x, int y){
		this.quality = quality;
		this.x = x;
		this.y = y;
	}


	/**
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}


	/** Defaults to full quality
	 * 
	 * @param qualityString
	 * @return
	 */
	public static Quality getQuality(String qualityString) {
		Quality returner = Quality.FULL;
		
		if (qualityString == null) return returner;
		else if(qualityString.equals(Quality.FULL.getQuality())) returner = Quality.FULL;
		else if(qualityString.equals(Quality.REDUCED.getQuality())) returner = Quality.REDUCED;
		else if(qualityString.equals(Quality.ICON.getQuality())) returner = Quality.ICON;
		
		return returner;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}
	
	
}
