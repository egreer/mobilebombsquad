package edu.wpi.cs525h.ayeg.virtualgraffiti;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
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
import edu.dhbw.andar.pub.CustomObject;
import edu.dhbw.andar.pub.CustomRenderer;
import edu.wpi.cs525h.ayeg.virtualgraffiti.R;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class GraffitiBuilderActivity extends AndARActivity {
	
	private final int MENU_SCREENSHOT = 0;

	private static int layerID = 1;
	
	CustomObject someObject;
	//GraffitiARToolkit artoolkit;
	ARToolkit artoolkit;
	List<Layer> layers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		super.setNonARRenderer(renderer);//or might be omited
		layers = new LinkedList<Layer>();
		try {
			artoolkit = super.getArtoolkit();
			//artoolkit.setBuilder(this);
			//artoolkit = new GraffitiARToolkit(super.getArtoolkit());
			someObject = new CustomObject
				("test", "patt.hiro", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);
			
			/*someObject = new CustomObject
			("test", "android.patt", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);*/

			someObject = new CustomObject
			("test", "toolmarker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	
			
			someObject = new CustomObject
			("test", "shapemarker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	

			
			someObject = new CustomObject
			("test", "circles marker16.pat", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);	

			
			someObject = new CustomObject
			("test", "sweepmarker16.pat", 80.0, new double[]{0,0});
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
	 * Adds a new Layer
	 * 
	 * @return
	 */
	public Layer addLayer(Layer newLayer) {
		//Layer newLayer = new Layer(layerID);
		layers.add(newLayer);
		//layerID++;
		return newLayer;
	}
	
	public List<Layer> getLayers() {
		return layers;
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		menu.add(0, MENU_SCREENSHOT, 0, getResources().getText(R.string.takescreenshot))
		.setIcon(R.drawable.screenshoticon);
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
		switch(item.getItemId()) {
		case MENU_SCREENSHOT:
			new TakeAsyncScreenshot().execute();
			break;
		}
		return true;
	}
	
	class SaveNewLayer extends AsyncTask<Void, Void, Void> {

		private String errorMsg = null;
		
		@Override
		protected Void doInBackground(Void... params) {
			Layer newLayer = new Layer(layerID);
			
			Vector<ARObject> objects = artoolkit.getArobjects();
			//LinkedList<GraffitiObject> gObjects = new LinkedList<GraffitiObject>();
			for (ARObject obj : objects) {
				GraffitiObject gObj = ((GraffitiObject) obj).generateCopy();
				//gObj.setSaved(true);
				gObj.saveGLMatrix();
				newLayer.add(gObj);
			}
			
			addLayer(newLayer);
			layerID++;
			
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.layercreatesuccess), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(GraffitiBuilderActivity.this, getResources().getText(R.string.layercreatefailure)+errorMsg, Toast.LENGTH_SHORT ).show();
		}
		
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
	
	
}
