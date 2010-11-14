package jp.androidgroup.nyartoolkit.GLLib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ColorHSVDialog extends Dialog {
	
    public interface OnColorChangedListener {
        void colorChanged(int color);
    }
	
    private OnColorChangedListener mListener;
    private ColorHSV mInitialColor;

	private static class ColorHSVView extends View {
        private Paint mPaint;
        private Paint mCenterPaint;
		private Paint textPaint;

        private final int[] mColors;

        private OnColorChangedListener mListener;
		private ColorHSV hsv;

        private static final int CENTER_X = 100;
        private static final int CENTER_Y = 100;
        private static final int CENTER_RADIUS = 32;
        private static final int SV_RADIUS = 64;
		
        ColorHSVView(Context c, OnColorChangedListener l, ColorHSV hsv) {
            super(c);
            mListener = l;
            mColors = new int[] {
                0xFFFF0000, 
                0xFFFFFF00,
				0xFF00FF00,
				0xFF00FFFF,
				0xFF0000FF,
				0xFFFF00FF,
				0xFFFF0000
            };
            Shader s = new SweepGradient(0, 0, mColors, null);
            
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(32);

            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(hsv.toARGB());
			mCenterPaint.setStrokeWidth(5);

			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
								  |Paint.DEV_KERN_TEXT_FLAG);
			textPaint.setColor(Color.WHITE);

			this.hsv = hsv;
		}

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(CENTER_X*2, CENTER_Y*2);
        }

        private boolean mTrackingCenter;
        private boolean mHighlightCenter;
        private boolean mSVCenter;
		
        @Override 
        protected void onDraw(Canvas canvas) {
            float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;
            canvas.translate(CENTER_X, CENTER_X);
            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

            if (mTrackingCenter) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                
				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else {
					mCenterPaint.setAlpha(0x80);
				}
                canvas.drawCircle(0, 0,
                                  CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                                  mCenterPaint);
                
                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }

 			canvas.drawText("H: "+hsv.h, 0, 15, textPaint);
 			canvas.drawText("S: "+hsv.s, 0, 30, textPaint);
 			canvas.drawText("V: "+hsv.v, 0, 45, textPaint);
		}

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - CENTER_X;
            float y = event.getY() - CENTER_Y;
			int len = (int)java.lang.Math.sqrt(x*x + y*y);
            boolean inCenter = len <= CENTER_RADIUS;
			boolean inSV = !inCenter && len <= SV_RADIUS;
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTrackingCenter = inCenter;
					mSVCenter = inSV;
                    if (inCenter) {
                        mHighlightCenter = true;
                        invalidate();
                        break;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (mTrackingCenter) {
                        if (mHighlightCenter != inCenter) {
                            mHighlightCenter = inCenter;
                            invalidate();
                        }
					} else if (mSVCenter) {
						float v = (float)(x + SV_RADIUS) / (SV_RADIUS * 2);
						float s = (float)(y + SV_RADIUS) / (SV_RADIUS * 2);
						s =  1 - s;
// 						Log.i("ColorHSVDialog", "s: "+s);
// 						Log.i("ColorHSVDialog", "v: "+v);
						hsv.s = s < 0 ? 0 : s > 1 ? 1 : s;
						hsv.v = v < 0 ? 0 : v > 1 ? 1 : v;
                        mCenterPaint.setColor(hsv.toARGB());
                        invalidate();
                    } else {
						int h = (int)Math.toDegrees(java.lang.Math.atan2(y, x));
						if (h < 0) h += 360;
// 						Log.i("ColorHSVDialog", "h: "+h);
						hsv.h = h;
                        mCenterPaint.setColor(hsv.toARGB());
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTrackingCenter) {
                        if (inCenter) {
                            mListener.colorChanged(mCenterPaint.getColor());
                        }
                        mTrackingCenter = false;    // so we draw w/o halo
                        invalidate();
                    }
                    break;
            }
            return true;
        }
	}

    public ColorHSVDialog(Context context,
						  OnColorChangedListener listener,
						  int initialColor) {
        super(context);
        
        mListener = listener;
        mInitialColor = new ColorHSV(initialColor);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Have the system blur any windows behind this one.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
							 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };

        setContentView(new ColorHSVView(getContext(), l, mInitialColor));
        setTitle("HSV Color");
    }
}
