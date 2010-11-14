package jp.androidgroup.nyartoolkit.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class GLBg {
	private int textureWidth = 0;
	private int textureHeight = 0;

	static final int [] vertices = {
		-0x10000, -0x10000,
		-0x10000, 0x10000,
		0x10000, 0x10000,
		0x10000, -0x10000,
	};
	static final int [] texCoords = {
		0, 		  0x10000,  
		0,        0,
		0x10000,  0, 
		0x10000,  0x10000,
	};
	private IntBuffer mVertexBuffer;
	private IntBuffer mTexCoordsBuffer;


	public GLBg() {
		init();
	}
	
	private void init() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoords.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoordsBuffer = cbb.asIntBuffer();
        mTexCoordsBuffer.put(texCoords);
        mTexCoordsBuffer.position(0);
	}

	public int createTexture(GL10 gl) {
 		int [] textureNameBuf = { -1 };
 		gl.glGenTextures(1, textureNameBuf, 0);
 		return textureNameBuf[0];
 	}

	private int clz(int x) {
		if (x == 0) return 32;
		int e = 31;
		if ((x&0xFFFF0000) != 0) { e -=16; x >>=16; }
		if ((x&0x0000FF00) != 0) { e -= 8; x >>= 8; }
		if ((x&0x000000F0) != 0) { e -= 4; x >>= 4; }
		if ((x&0x0000000C) != 0) { e -= 2; x >>= 2; }
		if ((x&0x00000002) != 0) { e -= 1; }
		return e;
	}

 	public void loadTexture(GL10 gl, int textureName, Bitmap bitmap) {
		// Log.i("GLBg", "loadTexture in");

 		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		int bitmap_w = bitmap.getWidth();
		int bitmap_h = bitmap.getHeight();
		int texture_w;
		int texture_h;

		texture_w = 1 << (31 - clz(bitmap_w));
		texture_h = 1 << (31 - clz(bitmap_h));
		if (texture_w < bitmap_w) texture_w <<= 1;
		if (texture_h < bitmap_h) texture_h <<= 1;
		
		// Log.i("GLBg", "texture_w: "+texture_w);
		// Log.i("GLBg", "texture_h: "+texture_h);
		
		if (texture_w == bitmap_w && texture_h == bitmap_h) {
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		} else {
			int iformat = GLUtils.getInternalFormat(bitmap);
			gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0,
							iformat, texture_w, texture_h, 0,
							iformat, GL10.GL_UNSIGNED_BYTE, null);
			GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, bitmap);
		}
		textureWidth = texture_w;
		textureHeight = texture_h;

		gl.glDisable(GL10.GL_TEXTURE_2D);
 		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		// Log.i("GLBg", "loadTexture out");
	}

	public void deleteTexture(GL10 gl, int textureName) {
		int ts[] = { textureName };
		gl.glDeleteTextures(1, ts, 0);
	}

  	public void draw(GL10 gl, int textureName, Bitmap bitmap) {
  		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
  		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glDisable(GL10.GL_DEPTH_TEST);

        gl.glFrontFace(gl.GL_CW);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glTexEnvx(GL10.GL_TEXTURE_ENV, 
					 GL10.GL_TEXTURE_ENV_MODE,
					 GL10.GL_REPLACE);
 		gl.glTexParameterx(GL10.GL_TEXTURE_2D, 
 						   GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
 		gl.glTexParameterx(GL10.GL_TEXTURE_2D, 
 						   GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
  		gl.glTexParameterx(GL10.GL_TEXTURE_2D, 
  						   GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
  		gl.glTexParameterx(GL10.GL_TEXTURE_2D, 
  						   GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

		// Set texture
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		int tw = textureWidth;
		int th = textureHeight;
		int bitmap_w = bitmap.getWidth();
		int bitmap_h = bitmap.getHeight();
		if (tw != bitmap_w || th != bitmap_h) {
			if (tw < bitmap_w) tw <<= 1;
			if (th < bitmap_h) th <<= 1;
			float ws = (float)bitmap_w / tw;
			float hs = (float)bitmap_h / th;
			gl.glScalef(ws, hs, 1.0f);
		}

		// Set projection
  		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
  		gl.glLoadIdentity();
 		gl.glOrthox(-0x10000, 0x10000, -0x10000, 0x10000, -0x10000, 0x10000);
 		
		// Draw model
   		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
  		gl.glLoadIdentity();

		// gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTexCoordsBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glDisable(GL10.GL_TEXTURE_2D);
  		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	 		
		gl.glMatrixMode(GL10.GL_TEXTURE);
 		gl.glPopMatrix();
   		gl.glMatrixMode(GL10.GL_MODELVIEW);
 		gl.glPopMatrix();
 		gl.glMatrixMode(GL10.GL_PROJECTION);
 		gl.glPopMatrix();

  	}

}
