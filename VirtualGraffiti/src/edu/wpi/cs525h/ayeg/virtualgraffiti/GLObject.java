package edu.wpi.cs525h.ayeg.virtualgraffiti;

import javax.microedition.khronos.opengles.GL10;

/** ALL drawable objects implement this interface
 * 
 * @author Andrew Yee
 * @author Eric Greer
 *
 */
public interface GLObject {
	
	/**	The Draw method for the object that will draw the shape onto the gl
	 * 
	 * @param gl	The GL renderer
	 */
	public void draw(GL10 gl);
	
}
