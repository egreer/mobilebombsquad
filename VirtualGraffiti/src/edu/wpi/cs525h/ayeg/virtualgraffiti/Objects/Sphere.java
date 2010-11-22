package edu.wpi.cs525h.ayeg.virtualgraffiti.Objects;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.util.GraphicsUtil;
import edu.wpi.cs525h.ayeg.virtualgraffiti.GLObject;

/**
 * 
 * Sphere object based off of Jeff LaMarche's Procedural Sphere in OpenGL ES
 * http://www.iphonemobilephones.com/procedural-spheres-in-opengl-es.html
 * 
 * @author Andrew Yee
 *
 */
public class Sphere implements GLObject {

	private final int slices = 25;
	private final int stacks = 25;
	private final float radius = 25.0f;
	
	private float[] triangleFanVertices;// = new float[(slices + 2)*3];
	private float[] triangleFanNormals;// = new float[(slices + 2)*3];
	
	private float[] triangleStripVertices;
	private float[] triangleStripNormals;
			
	private FloatBuffer fanVertices;
	private FloatBuffer fanNormals;
	private FloatBuffer stripVertices;
	private FloatBuffer stripNormals;
	
	public Sphere() {
		calculateSphereTriangleFan();
		calculateSphereTriangleStrips();
		
		fanVertices = GraphicsUtil.makeFloatBuffer(triangleFanVertices);
		fanNormals = GraphicsUtil.makeFloatBuffer(triangleFanNormals);
		stripVertices = GraphicsUtil.makeFloatBuffer(triangleStripVertices);
		stripNormals = GraphicsUtil.makeFloatBuffer(triangleStripNormals);
	}
	
	/**
	 * Calculates the sphere's Fans
	 */
	void calculateSphereTriangleFan() {
		int fanVertexCount = (slices + 2) * 3;
		triangleFanVertices = new float[fanVertexCount];
		triangleFanNormals = new float[fanVertexCount];
		
		double drho = Math.PI / stacks;
		double dtheta = 2.0 * Math.PI / slices;
		
		triangleFanVertices[0] = 0.0f;
		triangleFanVertices[1] = 0.0f;
		triangleFanVertices[2] = radius;
		int counter = 3;
		for (int j = 0; j <= slices; j++) {
			double theta = (j == slices) ? 0.0 : j * dtheta;
			Double xD = new Double((-1.0)*Math.sin(theta) * Math.sin(drho));
			Double yD = new Double(Math.cos(theta) * Math.sin(drho));
			Double zD = new Double(Math.cos(drho));
			triangleFanVertices[counter++] = xD.floatValue() * radius;
			triangleFanVertices[counter++] = yD.floatValue() * radius;
			triangleFanVertices[counter++] = zD.floatValue() * radius;
		}
		
		triangleFanNormals = normalize(triangleFanVertices);
		
	}
	/**
	 *  Calculates the triangles for the sphere
	 */
	void calculateSphereTriangleStrips() {
		int stripVertexCount = ((slices + 1) * 2 * stacks) * 3;
		triangleStripVertices = new float[stripVertexCount];
		triangleStripNormals = new float[stripVertexCount];
		
		double drho = Math.PI / stacks;
		double dtheta = 2.0 * Math.PI / slices;
		
		int counter = 0;
		for (int i = 0; i < stacks; i++) {
			double rho = i * drho;
			for (int j = 0; j <=slices; j++) {
				double theta = (j == slices) ? 0.0 : j * dtheta;
				Double xD = new Double((-1.0)*Math.sin(theta) * Math.sin(rho));
				Double yD = new Double(Math.cos(theta) * Math.sin(rho));
				Double zD = new Double(Math.cos(rho));
				triangleStripVertices[counter++] = xD.floatValue() * radius;
				triangleStripVertices[counter++] = yD.floatValue() * radius;
				triangleStripVertices[counter++] = zD.floatValue() * radius;
				
				xD = new Double((-1.0)*Math.sin(theta) * Math.sin(rho+drho));
				yD = new Double(Math.cos(theta) * Math.sin(rho+drho));
				zD = new Double(Math.cos(rho+drho));
				triangleStripVertices[counter++] = xD.floatValue() * radius;
				triangleStripVertices[counter++] = yD.floatValue() * radius;
				triangleStripVertices[counter++] = zD.floatValue() * radius;
			}	
		}
		
		triangleStripNormals = normalize(triangleStripVertices);
		
	}
	
	float[] normalize(float[] vertices) {
		int vertexCount = vertices.length;
		float[] normals = new float[vertexCount];
		
		for (int i = 0; i < vertexCount; i += 3) {
			double x2 = new Float(vertices[i] * vertices[i]).doubleValue();
			double y2 = new Float(vertices[i+1] * vertices[i+1]).doubleValue();
			double z2 = new Float(vertices[i+2] * vertices[i+2]).doubleValue();
			double mag = Math.sqrt(x2 + y2 + z2);
			
			float xN = new Double(x2/mag).floatValue();
			float yN = new Double(y2/mag).floatValue();
			float zN = new Double(z2/mag).floatValue();
			
			normals[i] = xN;
			normals[i+1] = yN;
			normals[i+2] = zN;
			
		}
		
		return normals;
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
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fanVertices);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, fanNormals);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, triangleFanVertices.length/3); 
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, stripVertices);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, stripNormals);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, triangleStripVertices.length/3);	
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sphere";
	}

}
