package com.google.code.mobilebombsquad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PlayableSurfaceView extends View {

	final static int OFFSETX = 10;
	final static int OFFSETY = 10;
	final static int WIDTH = 300;
	final static int HEIGHT = 500;
	
	private ShapeDrawable playable;
	TriggerBubble bubble;
	TargetCircle circle;

	ArrayList<TouchPoint> touchpoints = new ArrayList<TouchPoint>();

	boolean explosion;
	
	public PlayableSurfaceView(Context context, Player player) {
		super(context);
		
		playable = new ShapeDrawable(new RectShape());
		//playable.getPaint().setColor(initialColor);
		playable.getPaint().setColor(player.getBackgroundColor());
		playable.setBounds(OFFSETX, OFFSETY, OFFSETX + WIDTH, OFFSETY + HEIGHT);
		
		bubble = new TriggerBubble();
		circle = new TargetCircle(40, player.getTargetcircleColor());
		
		explosion = false;
	}

	protected void onDraw(Canvas canvas) {
		if (explosion) {
			this.getContext().getResources().getDrawable(R.drawable.explode).draw(canvas);
		} else {
			playable.draw(canvas);
			circle.draw(canvas);
			bubble.draw(canvas);
			drawTouchPoints(canvas);
		}
	}
	
	boolean checkBubbleCircle() {
		return circle.getBounds().contains(bubble.getBounds());
	}
	
	
	void drawTouchPoints(Canvas canvas) {
		for (TouchPoint point : touchpoints) {
			point.draw(canvas);
		}
	}

	void addNewTouchPoint(int color) {
		touchpoints.add(new TouchPoint(color));
	}
	
	void removeTouchPoints(int color) {
		ArrayList<TouchPoint> removable = new ArrayList<TouchPoint>();
		
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				removable.add(point);
			}
		}
		
		touchpoints.removeAll(removable);
	}

	public void changeBackgroundColor(int color) {
		playable.getPaint().setColor(color);
		//playable.invalidateSelf();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		for (int i=0; i<event.getPointerCount(); i++) {

			int action = event.getActionMasked();
			int x = (int) event.getX(i);
			int y = (int) event.getY(i); 

			//if (action == MotionEvent.ACTION_DOWN) {
			for (TouchPoint point : touchpoints) {
				if (action == MotionEvent.ACTION_MOVE){
					if (point.contains(x, y)){
						if(!point.isSelected()){
							point.setSelected(true);
							((MobileBombSquad) this.getContext()).touchPointPressed();
						}
					//}else{
						/*Toast ate = Toast.makeText(this.getContext(), "Move doesn't contain point", Toast.LENGTH_SHORT);
						ate.show();
						boolean contained = false; 
						//for (int h = 0 ; h < event.getHistorySize() ; h++){
						//for (int h = event.getHistorySize() ; h >= 0 ; h--){
						int minimum = (int) Math.min(5,event.getHistorySize());
						for (int h = 0; h < minimum; h++) {
							
							int hisX  = (int) event.getHistoricalX(i, event.getHistorySize()-h);
							int hisY = (int) event.getHistoricalY(i, event.getHistorySize()-h);
							
							if (point.contains(hisX, hisY) && point.isSelected()){
								point.setSelected(false);
								((MobileBombSquad) this.getContext()).touchPointReleased();
								//contained = true;
								//if ( h <= /*event.getHistorySize() - 5){
									//contained = false;
									break;
								//}
							}
						}
						
						if (contained && point.isSelected()){
							point.setSelected(false);
							((MobileBombSquad) this.getContext()).touchPointReleased();	
						}*/
						
					}
				}
				
				else if (point.contains(x, y)) {
					//point.setSelected(true); //Uncomment to do multitouch more then 3 points implements a toggle that  
					if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
						point.setSelected(true);
						//tell MobileBombSquad a point and its color is selected
						//((MobileBombSquad) this.getContext()).touchPointPressed(point.getColor());
						((MobileBombSquad) this.getContext()).touchPointPressed();
					} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
						Toast t = Toast.makeText(this.getContext(), "setting point to false", Toast.LENGTH_SHORT);
						point.setSelected(false);
						t.show();
						//tell MobileBombSquad a point and its color is unselected
						//((MobileBombSquad) this.getContext()).touchPointReleased(point.getColor());
						t = Toast.makeText(this.getContext(), "set point to flase", Toast.LENGTH_SHORT);
						t.show();
						((MobileBombSquad) this.getContext()).touchPointReleased();
						
					}
				}
				
				
				}
			}
		
		return true;
	}
	
	/**
	 * Assumes a touchpoint with that color is available
	 * @param color
	 * @return
	 */
	public boolean allTouchPointsPressed(int color) {
		boolean colorAvailable = false;
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				if (!point.isSelected()) {
					return false;
				}
				colorAvailable = true;
			}
		}
		
		return colorAvailable;
	}
	
	/**
	 * Assumes a touchpoint with that color is available
	 * @param color
	 * @return
	 */
	public boolean allTouchPointsReleased(int color) {
		boolean colorAvailable = false;
		for (TouchPoint point : touchpoints) {
			if (point.getColor() == color) {
				if (point.isSelected()) {
					return false;
				}
				colorAvailable = true;
			}
		}
		return colorAvailable;
	}
	
	public void drawExplosion() {
		explosion = true;
		this.invalidate();
	}
	
	public void changePlayer(Player player) {
		changeBackgroundColor(player.getBackgroundColor());
		circle.changeColor(player.getTargetcircleColor());
		circle.generatePosition();
		this.invalidate();
	}
	
}