package com.google.code.mobilebombsquad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class WelcomeView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;

	
	private Drawable welcome;
	final Button startButton;
	
	public WelcomeView(Context context) {
		super(context);
		
		welcome = context.getResources().getDrawable(R.drawable.mbstitlescnterry);
		welcome.setBounds(OFFSETX, OFFSETY, OFFSETX + welcome.getIntrinsicWidth(), OFFSETY + welcome.getIntrinsicHeight());
		
		startButton = new Button(context);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            }
        });

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		welcome.draw(canvas);
	}

}
