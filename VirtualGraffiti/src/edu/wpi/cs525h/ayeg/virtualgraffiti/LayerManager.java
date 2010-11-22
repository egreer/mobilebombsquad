package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LayerManager extends Dialog{

	OnLayersChangedListener listener;
	private List<Layer> layers;
	
	public LayerManager(Context context, OnLayersChangedListener listener, List<Layer> layers) {
		super(context);
		this.listener = listener;
		this.layers = layers;
	}


	/*
     * (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	//setContentView(new LayerManagerView(getContext(), layers));  
    	
        setContentView(new LayerManagerView(getContext(), layers));
    }
	
	/**
	 *	Classes that want to be updated with the information from the dialog implement this class
	 *
	 */
	public interface OnLayersChangedListener {
		
		/** Updates the Layer listener with the Shape
		 * 
		 * @param Layer	The new list of layers
		 */
        void layersChanged(ArrayList<Layer> layer);
    }

	
	public static class LayerManagerView extends ListView{

		public LayerManagerView(Context context, List<Layer> layers) {
			super(context);
			setAdapter(new ArrayAdapter<Layer>(context,android.R.layout.simple_list_item_1, layers));
			
		}	
	}
	
	/**public static class LayerManagerAdapter extends ArrayAdapter{
	   	 ArrayAdapter(Context context, int textViewResourceId, List<T> objects)	
	}*/
}
