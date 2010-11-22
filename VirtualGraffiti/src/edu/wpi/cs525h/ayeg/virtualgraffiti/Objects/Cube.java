package edu.wpi.cs525h.ayeg.virtualgraffiti.Objects;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import edu.dhbw.andar.util.GraphicsUtil;
import edu.wpi.cs525h.ayeg.virtualgraffiti.GLObject;

public class Cube implements GLObject {
	private FloatBuffer box;
	private FloatBuffer normals;
	public Cube() {
		float boxf[] =  {
			// FRONT
			-25.0f, -25.0f,  25.0f,
			 25.0f, -25.0f,  25.0f,
			-25.0f,  25.0f,  25.0f,
			 25.0f,  25.0f,  25.0f,
			// BACK
			-25.0f, -25.0f, -25.0f,
			-25.0f,  25.0f, -25.0f,
			 25.0f, -25.0f, -25.0f,
			 25.0f,  25.0f, -25.0f,
			// LEFT
			-25.0f, -25.0f,  25.0f,
			-25.0f,  25.0f,  25.0f,
			-25.0f, -25.0f, -25.0f,
			-25.0f,  25.0f, -25.0f,
			// RIGHT
			 25.0f, -25.0f, -25.0f,
			 25.0f,  25.0f, -25.0f,
			 25.0f, -25.0f,  25.0f,
			 25.0f,  25.0f,  25.0f,
			// TOP
			-25.0f,  25.0f,  25.0f,
			 25.0f,  25.0f,  25.0f,
			 -25.0f,  25.0f, -25.0f,
			 25.0f,  25.0f, -25.0f,
			// BOTTOM
			-25.0f, -25.0f,  25.0f,
			-25.0f, -25.0f, -25.0f,
			 25.0f, -25.0f,  25.0f,
			 25.0f, -25.0f, -25.0f,
		};
		float normalsf[] =  {
			// FRONT
			0.0f, 0.0f,  1.0f,
			0.0f, 0.0f,  1.0f,
			0.0f, 0.0f,  1.0f,
			0.0f, 0.0f,  1.0f,
			// BACK
			0.0f, 0.0f,  -1.0f,
			0.0f, 0.0f,  -1.0f,
			0.0f, 0.0f,  -1.0f,
			0.0f, 0.0f,  -1.0f,
			// LEFT
			-1.0f, 0.0f,  0.0f,
			-1.0f, 0.0f,  0.0f,
			-1.0f, 0.0f,  0.0f,
			-1.0f, 0.0f,  0.0f,
			// RIGHT
			1.0f, 0.0f,  0.0f,
			1.0f, 0.0f,  0.0f,
			1.0f, 0.0f,  0.0f,
			1.0f, 0.0f,  0.0f,
			// TOP
			0.0f, 1.0f,  0.0f,
			0.0f, 1.0f,  0.0f,
			0.0f, 1.0f,  0.0f,
			0.0f, 1.0f,  0.0f,
			// BOTTOM
			0.0f, -1.0f,  0.0f,
			0.0f, -1.0f,  0.0f,
			0.0f, -1.0f,  0.0f,
			0.0f, -1.0f,  0.0f,
		};
			
			box = GraphicsUtil.makeFloatBuffer(boxf);
			normals = GraphicsUtil.makeFloatBuffer(normalsf);
		}
		
		
	public final void draw(GL10 gl, int color) {	
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	    //gl.glEnable(GL10.GL_COLOR_MATERIAL);
		    
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, box);
	    gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
	    gl.glColor4x(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		    
	    //gl.glDisable(GL10.GL_COLOR_MATERIAL);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
}
