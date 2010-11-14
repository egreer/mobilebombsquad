package jp.androidgroup.nyartoolkit.GLLib;

import android.graphics.Color;

public class ColorABGR {
	public byte a;
	public byte r;
	public byte g;
	public byte b;

	public ColorABGR() {
		a = r = g = b = (byte)0xff;
	}
	public ColorABGR(int agbr) {
		a = (byte)Color.alpha(agbr);
		r = (byte)Color.red(agbr);
		g = (byte)Color.blue(agbr);
		b = (byte)Color.blue(agbr);
	}
	public ColorABGR(byte a, byte r, byte g, byte b) {
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void set(byte a, byte r, byte g, byte b) {
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int toARGB() {
		return Color.argb(a, r, g, b);
	}
	public int toABGR() {
		return Color.argb(a, b, g, r);
	}
}
