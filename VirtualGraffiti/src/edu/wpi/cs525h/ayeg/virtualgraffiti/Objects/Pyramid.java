package edu.wpi.cs525h.ayeg.virtualgraffiti.Objects;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.util.GraphicsUtil;
import edu.wpi.cs525h.ayeg.virtualgraffiti.GLObject;

/**
 * 
 * Pyramid Shape class based off of edu.dhbw.andar.pub.SimpleBox
 * 
 * @author Andrew Yee
 * @author Eric Greer
 *
 */
public class Pyramid implements GLObject {
	
	public static float value2500and1250 = 2500f/(new Double(Math.sqrt(7812500)).floatValue());
	public static float value1250and2500 = 1250f/(new Double(Math.sqrt(7812500)).floatValue());
	public static float sqrt2 = new Double(Math.sqrt(2)).floatValue();

	private FloatBuffer vertexBuffer;  // Buffer for vertex-array
	private FloatBuffer normals;

	private float[] vertices = {
			
		0.0f, 0.0f, 25.0f,  //Top
		-25.0f, -25.0f, -25.0f,
		-25.0f,  25.0f, -25.0f,
			
		0.0f, 0.0f, 25.0f, //Top
		25.0f,  25.0f, -25.0f,
		25.0f, -25.0f, -25.0f,
			
		0.0f, 0.0f, 25.0f, //Top
		25.0f,  25.0f, -25.0f,
		-25.0f,  25.0f, -25.0f,
			
		0.0f, 0.0f, 25.0f, //Top
		-25.0f, -25.0f, -25.0f,
		25.0f, -25.0f, -25.0f,
			
	};

	float normalsf[] =  {
			
		value2500and1250, 0.0f, -value1250and2500,
		value2500and1250, 0.0f, -value1250and2500,
		value2500and1250, 0.0f, -value1250and2500,

		-value2500and1250, 0.0f, value1250and2500,
		-value2500and1250, 0.0f, value1250and2500,
		-value2500and1250, 0.0f, value1250and2500, 

		0.0f, value2500and1250, -value1250and2500,
		0.0f, value2500and1250, -value1250and2500,
		0.0f, value2500and1250, -value1250and2500,
			
		0.0f, -value2500and1250, value1250and2500,
		0.0f, -value2500and1250, value1250and2500,
		0.0f, -value2500and1250, value1250and2500
		
	};
	
	/** Constructor - Set up the buffers*/
	public Pyramid() {
		vertexBuffer = GraphicsUtil.makeFloatBuffer(vertices);
		normals = GraphicsUtil.makeFloatBuffer(normalsf);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.wpi.cs525h.ayeg.virtualgraffiti.GLObject#draw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		// Enable arrays and define their buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 3, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 6, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 9, 3);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pyramid";
	}
}

