package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.util.GraphicsUtil;

/** A GraffitiObject is a class that wraps a {@link GLObject} with additional drawing methods for drawing on markers 
 * 
 * @author Andrew Yee
 * @author Eric Greer
 *
 */
public class GraffitiObject extends ARObject {

	int color = Color.RED;
	GLObject object;
	boolean saved = false;
	
	private FloatBuffer mat_flash;
	//private FloatBuffer mat_ambient;
	private FloatBuffer mat_flash_shiny;
	//private FloatBuffer mat_diffuse;
	private FloatBuffer mat_ambient_diffuse;
	
	private float[] savedGLMatrix = new float[16];
	private FloatBuffer savedGLMatrixBuffer;
	
	/** Constructor for a {@link GraffitiObject}
	 * 
	 * @param name			The name of the Object
	 * @param patternName	The name of the Pattern to draw on
	 * @param markerWidth	The width of the marker
	 * @param markerCenter	The center position of the marker
	 * @param object		The {@link GLObject} to draw
	 */
	public GraffitiObject(String name, String patternName, double markerWidth, double[] markerCenter, GLObject object) {
		super(name, patternName, markerWidth, markerCenter);
		this.object = object;
		
		float   mat_flashf[]       = {0.6f, 0.6f, 0.6f, 1.0f};
		float   mat_flash_shinyf[] = {50.0f};
		float	mat_ambient_diffusef[] = {Color.red(color)/255.0f, Color.green(color)/255.0f, Color.blue(color)/255.0f, Color.alpha(color)/255.0f};
		
		mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_ambient_diffuse = GraphicsUtil.makeFloatBuffer(mat_ambient_diffusef);
		
		
	}
	
	/** Constructor for a {@link GraffitiObject} that handles the inital color
	 * 
	 * @param name			The name of the Object
	 * @param patternName	The name of the Pattern to draw on
	 * @param markerWidth	The width of the marker
	 * @param markerCenter	The center position of the marker
	 * @param object		The {@link GLObject} to draw
	 * @param color			The Color to set the object to
	 */
	public GraffitiObject(String name, String patternName, double markerWidth, double[] markerCenter, int color, GLObject object) {
		this(name, patternName, markerWidth, markerCenter, object);
		this.color = color;
	}
	
	/**
	 * Everything drawn here will be drawn directly onto the marker,
	 * as the corresponding translation matrix will already be applied.
	 */
	@Override
	public final void draw(GL10 gl) {
		if (saved) {
			if(glCameraMatrixBuffer != null) {
				savedGLMatrixBuffer.put(savedGLMatrix);
				savedGLMatrixBuffer.position(0);
				
				gl.glMatrixMode(GL10.GL_MODELVIEW);
			    gl.glLoadIdentity();
		
			    gl.glMatrixMode(GL10.GL_PROJECTION);
			    gl.glLoadMatrixf( glCameraMatrixBuffer );
			    
			    gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadMatrixf(savedGLMatrixBuffer);
			}
		} else {
			super.draw(gl);
	
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,mat_flash);
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mat_flash_shiny);	
				
		}
		
		updateLighting();
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, mat_ambient_diffuse);
		gl.glTranslatef( 0.0f, 0.0f, 12.5f );	
		
		//Draws the object
	    object.draw(gl);
	}
	

	/**
	 * @return	The color of the object
	 */
	public int getColor() {
		return color;
	}

	/** Sets the Color of the Object
	 * 
	 * @param color		The Android RGBA color
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return	Whether the object is saved or not.
	 */
	public boolean isSaved() {
		return saved;
	}

	/**
	 * @param saved	Sets whether to save the object or not.
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	/** Saves the GL Matrix	To the correct position
	 * 
	 */
	public void saveGLMatrix() {
			savedGLMatrix = getGlMatrix();
			savedGLMatrixBuffer = GraphicsUtil.makeFloatBuffer(savedGLMatrix);
			saved = true;
	}
	
	/**
	 *  Creates a copy of this object for saving.
	 *  (Not the same object) 
	 */
	public GraffitiObject generateCopy() {
		return new GraffitiObject(getName(), getPatternName(), getMarkerWidth(), getCenter(), color, object);
	}
	
	/**	The Constructor for this object based upon an ARobject
	 * 
	 * @param obj	The AR Object To Create this Object from
	 */
	public GraffitiObject(ARObject obj) {
		super(obj.getName(), obj.getPatternName(), obj.getMarkerWidth(), obj.getCenter());
		this.color = ((GraffitiObject)obj).color;
		this.object = ((GraffitiObject)obj).object;
		setGLMatrix(obj.getGlMatrix());
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.dhbw.andar.ARObject#init(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void init(GL10 gl) {
		// TODO Auto-generated method stub
	}
	

	/**
	 * @return	The under lying GL Object
	 */
	public GLObject getObject() {
		return object;
	}

	/**	Sets the underlying GL object to something else
	 * @param object	The GL Object to set to
	 */
	public void setObject(GLObject object) {
		this.object = object;
	}

	/** Updates the lighting of the object
	 * 
	 */
	public void updateLighting(){
		float	mat_ambient_diffusef[] = {Color.red(color)/255.0f, Color.green(color)/255.0f, Color.blue(color)/255.0f, Color.alpha(color)/255.0f};
		mat_ambient_diffuse = GraphicsUtil.makeFloatBuffer(mat_ambient_diffusef);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return object.toString();
	}
}
