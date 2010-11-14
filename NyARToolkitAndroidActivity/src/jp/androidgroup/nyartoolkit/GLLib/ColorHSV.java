package jp.androidgroup.nyartoolkit.GLLib;

import android.graphics.Color;

class ColorHSV {
	public int h;
	public float s;
	public float v;

	public ColorHSV() {
		h = 0;
		s = v = 0;
	}
	public ColorHSV(int argb) {
		rgbToHsv(((float)Color.red(argb) / 255.0f),
				 ((float)Color.green(argb) / 255.0f),
				 ((float)Color.blue(argb) / 255.0f));
	}
	
	public int toARGB() {
		ColorFloat c = new ColorFloat();
		c.a = 1.0f;
		hsvToRgb(c);
		return c.toARGB();
	}

	public int toABGR() {
		ColorFloat c = new ColorFloat();
		c.a = 1.0f;
		hsvToRgb(c);
		return c.toABGR();
	}

	public void hsvToRgb(ColorFloat c) {
// 		Log.i("HSV", "h: "+h);
// 		Log.i("HSV", "s: "+s);
// 		Log.i("HSV", "v: "+v);

		if (s <= Float.MIN_VALUE) {
			c.r = c.b = c.g = v;
		} else {
			int i = (int)Math.floor((double)h/60);
			float f = (float)h / 60 - i;
			float m = v * (1 - s);
			float n = v * (1 - s * f);
			float k = v * (1 - s * (1 - f));

// 			Log.i("HSV", "m: "+m);
// 			Log.i("HSV", "n: "+n);
// 			Log.i("HSV", "k: "+k);
// 			Log.i("HSV", "i: "+i);

			switch (i) {
			case 0: c.r = v; c.g = k; c.b = m; break;
			case 1: c.r = n; c.g = v; c.b = m; break;
			case 2: c.r = m; c.g = v; c.b = k; break;
			case 3: c.r = m; c.g = n; c.b = v; break;
			case 4: c.r = k; c.g = m; c.b = v; break;
			case 5: c.r = v; c.g = m; c.b = n; break;
			default:
				// Error
				break;
			}
		}
// 		Log.i("HSV", "toRGB r: "+c.r);
// 		Log.i("HSV", "toRGB g: "+c.g);
// 		Log.i("HSV", "toRGB b: "+c.b);
	}

	public void rgbToHsv(float r, float g, float b) {
		float max = Math.max(Math.max(r, g), b);
		float min = Math.min(Math.min(r, g), b);
		v = max;

// 		Log.i("HSV", "toHSV r: "+r);
// 		Log.i("HSV", "toHSV g: "+g);
// 		Log.i("HSV", "toHSV b: "+b);

// 		Log.i("HSV", "max: "+max);
// 		Log.i("HSV", "min: "+min);
// 		Log.i("HSV", "v: "+v);
		
		if (v <= Float.MIN_VALUE) {
			s = 0;
			h = 0;
		} else {
			float mm = max - min;
			s = mm / max;
			float cr = (max - r) / mm;
			float cg = (max - g) / mm;
			float cb = (max - b) / mm;
			float ht;

// 			Log.i("HSV", "cr: "+cr);
// 			Log.i("HSV", "cg: "+cg);
// 			Log.i("HSV", "cb: "+cb);
			
			if (max - r <= Float.MIN_VALUE) {
				ht = cb - cg;
			} else if (max - g <= Float.MIN_VALUE) {
				ht = 2 + cr - cb;
			} else {
				ht = 4 + cg - cr;
			}
			ht *= 60;
			if (ht < 0)
				ht += 360;
			h = Math.round(ht);
		}

// 		Log.i("HSV", "h: "+h);
// 		Log.i("HSV", "s: "+s);
// 		Log.i("HSV", "v: "+v);
		
	}
}
