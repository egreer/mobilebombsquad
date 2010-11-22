package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.interfaces.OpenGLRenderer;
import edu.dhbw.andar.util.GraphicsUtil;

/** A custom renderer for GL objects
 * 
 * @author Andrew Yee
 * @author Eric Greer
 */
public class GraffitiRenderer implements OpenGLRenderer {

	private float[] ambientlight1 = {.3f, .3f, .3f, 1f};
	private float[] diffuselight1 = {.7f, .7f, .7f, 1f};
	private float[] specularlight1 = {0.6f, 0.6f, 0.6f, 1f};
	private float[] lightposition1 = {20.0f,-40.0f,100.0f,1f};
	
	private FloatBuffer lightPositionBuffer1 =  GraphicsUtil.makeFloatBuffer(lightposition1);
	private FloatBuffer specularLightBuffer1 = GraphicsUtil.makeFloatBuffer(specularlight1);
	private FloatBuffer diffuseLightBuffer1 = GraphicsUtil.makeFloatBuffer(diffuselight1);
	private FloatBuffer ambientLightBuffer1 = GraphicsUtil.makeFloatBuffer(ambientlight1);
	
	GraffitiBuilderActivity builder;
	
	/** Constructor 
	 * 
	 * @param builder	The activity of the object
	 */
	public GraffitiRenderer (GraffitiBuilderActivity builder) {
		this.builder = builder;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.dhbw.andar.interfaces.OpenGLRenderer#draw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public final void draw(GL10 gl) {
		List<Layer> layers = builder.getLayers();
		
		//TODO HERE 
		for (Layer layer : layers) {
			layer.draw(gl);
		}
	}


	/**
	 * Sets up the Environment
	 */
	public final void setupEnv(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPositionBuffer1);
		gl.glEnable(GL10.GL_LIGHT1);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisable(GL10.GL_TEXTURE_2D);
	    
		initGL(gl);
	}
	
	/**
	 * Initializes the GL for Drawing
	 */
	public final void initGL(GL10 gl) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_NORMALIZE);
	}
	
}
