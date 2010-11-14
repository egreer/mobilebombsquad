package jp.androidgroup.nyartoolkit.GLLib;

import android.graphics.Color;

public class ColorFloat {
	public float a;
	public float r;
	public float g;
	public float b;

	public ColorFloat() {
		a = r = g = b = 1.0f;
	}
	public ColorFloat(int argb) {
		a = ((float)((argb & 0xff000000) >> 24) / 255.0f);
		r = ((float)((argb & 0x00ff0000) >> 16) / 255.0f);
		g = ((float)((argb & 0x0000ff00) >> 8) / 255.0f);
		b = ((float)( argb & 0x000000ff) / 255.0f);
	}
	public ColorFloat(float r, float g, float b, float a) {
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int toARGB() {
		return Color.argb((int)(255*a), (int)(255*r), 
						  (int)(255*g), (int)(255*b));	
	}
	public int toABGR() {
		return Color.argb((int)(255*a), (int)(255*b), 
						  (int)(255*g), (int)(255*r));
	}
}
