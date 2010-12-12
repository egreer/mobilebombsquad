package edu.wpi.cs525h.ayeg.virtualgraffiti;


import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

/** This is the dialog for picking a shape 
 * 
 * @author Eric Greer
 * @author Andrew Yee
 *
 */
public class ShapePickerDialog extends Dialog {
    
	/**
	 *	Classes that want to be updated with the information from the dialog implement this class
	 *
	 */
	public interface OnShapeChangedListener {
		/** Updates the Shape listener with the Shape
		 * 
		 * @param shape		The new shape
		 */
        void shapeChanged(Shape shape);
    }

    private OnShapeChangedListener mListener;
    private Shape mInitialShape;

    /** The View for the Shape Picker
     * 
     */
	private static class ShapePickerView extends LinearLayout {
		private Paint mPaint;
		private Shape mCurrentShape;
		private OnShapeChangedListener mListener;
		private ArrayList<View> images = new ArrayList<View>();

		private int maxHeight = 20;
		private int maxWidth = 20;
		
		/** Constructor for the shape view
		 * 
		 * @param c		The context to draw on
		 * @param l		The listener to notify
		 * @param shape	The shape currently selected
		 */
		ShapePickerView(Context c, OnShapeChangedListener l, Shape shape) {
			super(c);
			mListener = l;
						
			mCurrentShape = shape;

			// Initializes the Paint that will draw the View
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setTextSize(12);
			
			Resources res = this.getResources();
			
			for(final Shape id : Shape.values()){
				ImageView i = new ImageView(this.getContext());
				i.setImageDrawable(res.getDrawable(id.getIcon()));
				images.add(i);
				
				i.setMaxHeight(maxHeight);
				i.setMaxWidth(maxWidth);
				i.setScaleType(ScaleType.FIT_CENTER);
				i.setHapticFeedbackEnabled(true);
				i.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		            	 mCurrentShape = id;
		            	 mListener.shapeChanged(mCurrentShape);
		             }
				});
				this.addView(i);
			}	
			
		}
	}

	/** The dialog constructor
	 * 
	 * @param context	The context to draw on
	 * @param listener   The listener to notify
	 * @param initialShape The shape currently selected
	 */
    public ShapePickerDialog(Context context, OnShapeChangedListener listener, Shape initialShape) {
        super(context);

        mListener = listener;
        mInitialShape = initialShape;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnShapeChangedListener l = new OnShapeChangedListener() {
            public void shapeChanged(Shape shape) {
                mListener.shapeChanged(shape);
                dismiss();
            }
        };

        setContentView(new ShapePickerView(getContext(), l, mInitialShape));
        setTitle(R.string.settings_bg_shape_dialog);       
    }
}
