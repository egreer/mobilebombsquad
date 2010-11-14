package jp.androidgroup.nyartoolkit.model;

import javax.microedition.khronos.opengles.GL10;

/**
 * It is an interface of 3D object which ARtoolkit draws. 
 * 
 * @author noritsuna
 *
 */
public interface ARModel {
	public void draw(GL10 gl);
}
