package edu.wpi.cs525h.mobilebombsquad.ayeg.accel;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class TriggerBubble {

	OvalShape bubble;
	Drawable drawable;
	Paint paint;
	
	public TriggerBubble() {
		bubble = new OvalShape();
		bubble.resize(25, 25);

		drawable = new ShapeDrawable(bubble);
		
		
		paint = new Paint();
		paint.setARGB(0, 0, 255, 0);
	}
	
	Drawable getDrawable() {
		return drawable;
	}
	
	Paint getPaint() {
		return paint;
	}
	
	
	
	
	
}
