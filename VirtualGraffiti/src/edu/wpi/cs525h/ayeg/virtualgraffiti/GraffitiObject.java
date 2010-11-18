package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.util.GraphicsUtil;

public class GraffitiObject extends ARObject {

	int color;
	GLObject object;
	boolean saved = false;
	
	private FloatBuffer mat_flash;
	private FloatBuffer mat_ambient;
	private FloatBuffer mat_flash_shiny;
	private FloatBuffer mat_diffuse;
	
	private float[] savedGLMatrix = new float[16];
	private FloatBuffer savedGLMatrixBuffer;
	
	public GraffitiObject(String name, String patternName, double markerWidth, double[] markerCenter, GLObject object) {
		super(name, patternName, markerWidth, markerCenter);
		this.object = object;
		
		float   mat_ambientf[]     = {0f, 1.0f, 0f, 1.0f};
		float   mat_flashf[]       = {0f, 1.0f, 0f, 1.0f};
		float   mat_diffusef[]       = {0f, 1.0f, 0f, 1.0f};
		float   mat_flash_shinyf[] = {50.0f};
		
		mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf);
		mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(mat_diffusef);
		
	}
	
	/**
	 * Everything drawn here will be drawn directly onto the marker,
	 * as the corresponding translation matrix will already be applied.
	 */
	@Override
	public final void draw(GL10 gl) {
		if (saved) {
			if(!isInitialized()) {
				init(gl);
				setInitialized(true);
			}
			if(glCameraMatrixBuffer != null) {
				savedGLMatrixBuffer.put(savedGLMatrix);
				savedGLMatrixBuffer.position(0);
				
				//argDrawMode3D
				gl.glMatrixMode(GL10.GL_MODELVIEW);
			    gl.glLoadIdentity();
			    //argDraw3dCamera
			    gl.glMatrixMode(GL10.GL_PROJECTION);
			    gl.glLoadMatrixf( glCameraMatrixBuffer );
			    
			    gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadMatrixf(savedGLMatrixBuffer);
			}
		} else {
			super.draw(gl);
		
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,mat_flash);
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mat_flash_shiny);	
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse);	
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient);
	
		    gl.glColor4f(0, 1.0f, 0, 1.0f);
		    gl.glTranslatef( 0.0f, 0.0f, 12.5f );
	    
		}
		
	    object.draw(gl);
	}
	

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	public void saveGLMatrix() {
		System.arraycopy(getGlMatrix(), 0, savedGLMatrix, 0, getGlMatrix().length);
	}
	
	@Override
	public void init(GL10 gl) {
		// TODO Auto-generated method stub
		
	}
}
