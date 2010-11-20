package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class GraffitiBuilderActivity extends AndARActivity implements OnColorChangedListener{
	
	private final int MENU_SCREENSHOT = 0;
	private final int MENU_SAVE = 1;
	private final int MENU_EXPORT = 2;
	private final int MENU_CHANGE_SHAPE = 3;
	private final int MENU_CHANGE_COLOR = 4;
	private final int MENU_REORGANIZE = 5;
	private final String SETTING_SHAPE_COLOR_KEY = "20";
	
	private int SETTING_SHAPE_COLOR = 0x00000000;
	private int SETTING_SHAPE_COLOR_DEFAULT = 0x00FFFFFF;
	
	private static int layerID = 1;
	
	GraffitiObject someObject;
	ARToolkit artoolkit;
	List<Layer> layers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		GraffitiRenderer renderer = new GraffitiRenderer(this);
		super.setNonARRenderer(renderer);//or might be omited
		layers = new LinkedList<Layer>();
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

			someObject = new GraffitiObject
			("test", "sweepmarker16.pat", 80.0, new double[]{0,0}, new SimpleBox());
			artoolkit.registerARObject(someObject);	


	/*		someObject = new CustomObject
			("test", "circles marker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	
*/
			someObject = new GraffitiObject
			("test", "shapemarker16.pat", 80.0, new double[]{0,0}, new SimpleBox());
			artoolkit.registerARObject(someObject);	
			
			

			
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
				break;
			case MENU_EXPORT:
				break;
			case MENU_REORGANIZE:
				break;
			case MENU_SAVE:
				new SaveNewLayer().execute();
				break;
		}
			
		return true;
	}
	
	class SaveNewLayer extends AsyncTask<Void, Void, Void> {

		private String errorMsg = null;
		
		@Override
		protected Void doInBackground(Void... params) {
			try{
			Layer newLayer = new Layer(layerID);
			
			Vector<ARObject> objects = artoolkit.getArobjects();
			//LinkedList<GraffitiObject> gObjects = new LinkedList<GraffitiObject>();
			for (ARObject obj : objects) {
				//if (obj.getId() != 0) {
					//GraffitiObject gObj = ((GraffitiObject) obj).generateCopy();
					GraffitiObject gObj = new GraffitiObject(obj);
					//gObj.setSaved(true);
					gObj.saveGLMatrix();
					newLayer.add(gObj);
				//}
				
			}
			
			addLayer(newLayer);
			layerID++;
			} catch(Exception e) {
				e.printStackTrace();
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

	
	
	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {
		
		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/AndARScreenshot"+new Date().getTime()+".png");
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
		// TODO Auto-generated method stub
		SETTING_SHAPE_COLOR = color;
		Toast.makeText(this, "Color: " + SETTING_SHAPE_COLOR, Toast.LENGTH_LONG).show();
	}
	
	
}
