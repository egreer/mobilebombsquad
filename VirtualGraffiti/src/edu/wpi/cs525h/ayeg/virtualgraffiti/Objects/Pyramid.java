package edu.wpi.cs525h.ayeg.virtualgraffiti.Objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

import edu.dhbw.andar.util.GraphicsUtil;
import edu.wpi.cs525h.ayeg.virtualgraffiti.GLObject;

public class Pyramid implements GLObject {
	
	public static float value2500and1250 = 2500f/(new Double(Math.sqrt(7812500)).floatValue());
	public static float value1250and2500 = 1250f/(new Double(Math.sqrt(7812500)).floatValue());
	public static float sqrt2 = new Double(Math.sqrt(2)).floatValue();
	

	private FloatBuffer vertexBuffer;  // Buffer for vertex-array
	//   private FloatBuffer colorBuffer;   // Buffer for color-array
	//private ByteBuffer indexBuffer;    // Buffer for index-array
	private FloatBuffer normals;
	
	/*	   private float[] vertices = { // 5 vertices of the pyramid in (x,y,z)
		      -25.0f, -25.0f, -25.0f,  // 0. left-bottom-back
		       25.0f, -25.0f, -25.0f,  // 1. right-bottom-back
		       25.0f, -25.0f,  25.0f,  // 2. right-bottom-front
		      -25.0f, -25.0f,  25.0f,  // 3. left-bottom-front
		       0.0f,  25.0f,  0.0f   // 4. top
		   };*/


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
			
			/*0.0f, 0.0f, 25.0f,
			25.0f, 0.0f, 0.0f,
			0.0f, 25.0f, 0.0f,
			
			0.0f, 0.0f, 25.0f,
			25.0f, 0.0f, 0.0f,
			-25.0f*sqrt2, -25.0f*sqrt2, 0.0f,
			
			0.0f, 0.0f, 25.0f,
			0.0f, 25.0f, 0.0f,
			-25.0f*sqrt2, -25.0f*sqrt2, 0.0f*/


			
			//Bottom
		
			/*-25.0f, -25.0f, -25.0f,
			-25.0f,  25.0f, -25.0f,
			 25.0f, -25.0f, -25.0f,
			 25.0f,  25.0f, -25.0f,*/
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
			
									
			/*0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,*/
		};
	
	/*	   private float[] colors = {  // Colors of the 5 vertices in RGBA
		      0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
		      0.0f, 1.0f, 0.0f, 1.0f,  // 1. green
		      0.0f, 0.0f, 1.0f, 1.0f,  // 2. blue
		      0.0f, 1.0f, 0.0f, 1.0f,  // 3. green
		      1.0f, 0.0f, 0.0f, 1.0f   // 4. red
		   };*/

	private byte[] indices = { // Vertex indices of the 4 Triangles
			2, 4, 3,   // front face (CCW)
			1, 4, 2,   // right face
			0, 4, 1,   // back face
			4, 0, 3    // left face
	};

	// Constructor - Set up the buffers
	public Pyramid() {
		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		//ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		//vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		/*vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		      vertexBuffer.put(vertices);         // Copy data into buffer
		      vertexBuffer.position(0);           // Rewind
		 */

		vertexBuffer = GraphicsUtil.makeFloatBuffer(vertices);
		// Setup color-array buffer. Colors in float. An float has 4 bytes
		/*      ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		      cbb.order(ByteOrder.nativeOrder());
		      colorBuffer = cbb.asFloatBuffer();
		      colorBuffer.put(colors);
		      colorBuffer.position(0);
		 */
		normals = GraphicsUtil.makeFloatBuffer(normalsf);
		// Setup index-array buffer. Indices in byte.
		/*indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);*/
	}

	// Draw the shape
	public void draw(GL10 gl, int color) {
		//gl.glFrontFace(GL10.GL_CCW);  // Front face in counter-clockwise orientation

		// Enable arrays and define their buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		//gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
		//gl.glColor4x(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length/3);
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 3, 3);
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 6, 3);
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 9, 3);
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);

		//      gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		//      gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

	/*	gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE,
				indexBuffer);
*/
		//gl.glDisable(GL10.GL_COLOR_MATERIAL);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		//      gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
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

