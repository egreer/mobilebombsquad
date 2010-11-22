package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.pub.SimpleBox;
import edu.wpi.cs525h.ayeg.virtualgraffiti.ColorPickerDialog.OnColorChangedListener;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Cube;
import edu.wpi.cs525h.ayeg.virtualgraffiti.Objects.Pyramid;
import edu.wpi.cs525h.ayeg.virtualgraffiti.ShapePickerDialog.OnShapeChangedListener;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class GraffitiBuilderActivity extends AndARActivity implements OnColorChangedListener, OnShapeChangedListener{
	
	private final int MENU_SCREENSHOT = 0;
	private final int MENU_SAVE = 1;
	private final int MENU_EXPORT = 2;
	private final int MENU_CHANGE_SHAPE = 3;
	private final int MENU_CHANGE_COLOR = 4;
	private final int MENU_REORGANIZE = 5;
	private final String SETTING_SHAPE_COLOR_KEY = "20";
	
	private int SETTING_SHAPE_COLOR = 0x00000000;
	private int SETTING_SHAPE_COLOR_DEFAULT = 0x00FFFFFF;
	
	private Shape SETTING_SHAPE = Shape.CUBE;
	
	private static int layerID = 1;
	
	ArrayList<GraffitiObject> objects = new ArrayList<GraffitiObject>();
	ARToolkit artoolkit;
	List<Layer> layers = new LinkedList<Layer>();
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		GraffitiRenderer renderer = new GraffitiRenderer(this);
		super.setNonARRenderer(renderer);//or might be omited
		//layers = new LinkedList<Layer>();
		try {
			artoolkit = super.getArtoolkit();
/*			someObject = new CustomObject
				("test", "patt.hiro", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);
			
			/*someObject = new CustomObject
			("test", "android.patt", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);*/

		/*	someObject = new CustomObject
			("test", "toolmarker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	
			*/

			objects.add(new GraffitiObject
			("test", "sweepmarker16.pat", 80.0, new double[]{0,0}, new Cube()));
			artoolkit.registerARObject(objects.get(0));	


	/*		someObject = new CustomObject
			("test", "circles marker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	
*/
			objects.add(new GraffitiObject
			("test", "shapemarker16.pat", 80.0, new double[]{0,0}, new Pyramid()));
			artoolkit.registerARObject(objects.get(1));	
			
			

			
			//someObject = new CustomObject
			//("test", "barcode.patt", 80.0, new double[]{0,0});
			//artoolkit.registerARObject(someObject);
			
		} catch (AndARException ex){
			//handle the exception, that means: show the user what happened
			System.out.println("");
		}			
	}

	/**
	 * Inform the user about exceptions that occurred in background threads.
	 * This exception is rather severe and can not be recovered from.
	 * Inform the user and shut down the application.
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AndAR EXCEPTION", ex.getMessage());
		finish();
	}	
	
	/**
	 * Creates and adds a new Layer
	 * 
	 * @return
	 */
	public Layer addLayer(Layer newLayer) {
		//Layer newLayer = new Layer(layerID);
		layers.add(newLayer);
		//layerID++;
		return newLayer;
	}
	
	public List<Layer> getLayers(){
		return layers;
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Resources re = getResources();
		
		//Change Shape
		menu.add(0, MENU_CHANGE_SHAPE, 1, re.getText(R.string.changeshape))
		.setIcon(android.R.drawable.ic_menu_gallery);

		//Screen Shot
		menu.add(0, MENU_SCREENSHOT, 2, re.getText(R.string.takescreenshot))
		.setIcon(R.drawable.screenshoticon);
		
		//Save
		menu.add(0, MENU_SAVE, 3, re.getText(R.string.save))
		.setIcon(android.R.drawable.ic_menu_save);
		
		//Change Color
		menu.add(0, MENU_CHANGE_COLOR, 4, re.getText(R.string.changecolor))
		.setIcon(android.R.drawable.ic_menu_save);
		
		//Export
		menu.add(0, MENU_EXPORT, 5, re.getText(R.string.exportdrawing))
		.setIcon(android.R.drawable.ic_menu_set_as);
				
		//Reorganize
		menu.add(0, MENU_REORGANIZE, 6, re.getText(R.string.reorganize))
		.setIcon(android.R.drawable.ic_menu_edit);
		
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*if(item.getItemId()==1) {
			artoolkit.unregisterARObject(someObject);
		} else if(item.getItemId()==0) {
			try {
				someObject = new CustomObject
				("test", "patt.hiro", 80.0, new double[]{0,0});
				artoolkit.registerARObject(someObject);
			} catch (AndARException e) {
				e.printStackTrace();
			}
		}*/
		//Handles the Menu Actions 
		switch(item.getItemId()) {
			case MENU_SCREENSHOT:
				new TakeAsyncScreenshot().execute();
				break;
			case MENU_CHANGE_COLOR:
				new ColorPickerDialog(this, this, SETTING_SHAPE_COLOR_KEY, SETTING_SHAPE_COLOR, SETTING_SHAPE_COLOR_DEFAULT).show();
				break;
			case MENU_CHANGE_SHAPE:
				new ShapePickerDialog(this, this, SETTING_SHAPE).show();
				break;
			case MENU_EXPORT:
				new ExportPicture().execute();
				break;
			case MENU_REORGANIZE:
				break;
			case MENU_SAVE:
				new SaveNewLayer().execute();
				break;
		}
			
		return true;
	}
	
	/**
	 * Saves a layer with the shape in place
	 *
	 */
	class SaveNewLayer extends AsyncTask<Void, Void, Void> {

		private String errorMsg = null;
	
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try{
			Layer newLayer = new Layer(layerID);
			Vector<ARObject> objects = artoolkit.getArobjects();
			for (ARObject obj : objects) {
				GraffitiObject gObj = new GraffitiObject(obj);
				gObj.saveGLMatrix();
				newLayer.add(gObj);				
			}
			
			addLayer(newLayer);
			layerID++;
			} catch(Exception e) {
				e.printStackTrace();
				//errorMsg = " Exception! ";
				errorMsg = e.getMessage();
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.layercreatesuccess), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.layercreatefailure)+errorMsg, Toast.LENGTH_SHORT ).show();
		};
		
	}

	class ExportPicture extends AsyncTask<Void, Void, Void> {
		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = savePicture();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/dcim/ARPaintImage-"+new Date().getTime()+".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();					
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}	
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.picturesaved), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.picturefailed)+errorMsg, Toast.LENGTH_SHORT ).show();
		};
		
	}
	
	
	/** Takes a screen shot of the Model and the surrounding background
	 */
	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {
		
		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/dcim/AndARScreenshot"+new Date().getTime()+".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();					
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}	
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.screenshotsaved), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.screenshotfailed)+errorMsg, Toast.LENGTH_SHORT ).show();
		};
		
	}


	/*
	 * (non-Javadoc)
	 * @see edu.wpi.cs525h.ayeg.virtualgraffiti.ColorPickerDialog.OnColorChangedListener#colorChanged(java.lang.String, int)
	 */
	@Override
	public void colorChanged(String key, int color) {
		SETTING_SHAPE_COLOR = color;
		Toast.makeText(this, "Color: " + SETTING_SHAPE_COLOR, Toast.LENGTH_LONG).show();
		
		//Sets all the colors
		Iterator<GraffitiObject> it = objects.iterator();
		while(it.hasNext()){
			GraffitiObject g = it.next();
			g.setColor(color);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.wpi.cs525h.ayeg.virtualgraffiti.ShapePickerDialog.OnShapeChangedListener#shapeChanged(java.lang.String, edu.wpi.cs525h.ayeg.virtualgraffiti.Shape)
	 */
	@Override
	public void shapeChanged(Shape shape) {
		SETTING_SHAPE = shape;
		Toast.makeText(this, "Shape: " + SETTING_SHAPE.name(), Toast.LENGTH_LONG).show();
		objects.get(1).setObject(SETTING_SHAPE.getObject());	
	}
	
	
}
