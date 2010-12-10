package layar;

/**
 * 
 * @author Eric Greer
 *
 */
public enum Quality {
	FULL ("full", 500, 500),
	REDUCED ("reduced", 100, 100), 
	ICON ("icon", 60, 60);
	
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
	
	
}
