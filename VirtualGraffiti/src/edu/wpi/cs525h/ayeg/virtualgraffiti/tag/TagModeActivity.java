package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import edu.wpi.cs525h.ayeg.virtualgraffiti.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TagModeActivity extends Activity {

	static final String SDCARD_LOCATION = "file:///sdcard/";
	public static final String TYPE_EXTERNAL = null;
	final String LAYAR_INTENT = "http://m.layar.com/open/" + ConnectionUtil.LAYER_NAME;
	final String LAYAR_INTENT_PARAM = LAYAR_INTENT + "?";
	final String LAYAR_REFRESH = "action=refresh";
	
	/**
	 * Request Codes
	 */
	private final int FILE_CHOOSER = 1;
	private final int FILE_TAGGER = 2;
	
	/**
	 * Response Codes
	 */
	static final int RESULT_FAIL = Activity.RESULT_FIRST_USER;
	
	String currentImage="";
	LocationUtil gps;
	LocationHandler handler;
	double latitude;
	double longitude;
	double altitude;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tagmode);
		handler = new LocationHandler(this);
		gps = new LocationUtil(this, handler);
		
		findViewById(R.id.selecttag).setOnClickListener(new selectTagClickListener());
		findViewById(R.id.tagworld).setOnClickListener(new tagWorldClickListener());
		findViewById(R.id.viewtags).setOnClickListener(new viewTagsClickListener());
	}
	
	class selectTagClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			chooseAnImage();
		}
		
	}
	
	class tagWorldClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			tagAnImage();
			
		}
		
	}
	
	class viewTagsClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			openLayar();
			
		}
		
	}
	
	void chooseAnImage() {
		Intent chooser = new Intent (TagModeActivity.this, CheckFileManagerActivity.class);
		//chooser.setData(Uri.parse(SDCARD_LOCATION));
		Bundle fileLocInfo = new Bundle();
		fileLocInfo.putString("cardloc", SDCARD_LOCATION);
		chooser.putExtras(fileLocInfo);
		startActivityForResult(chooser, FILE_CHOOSER);
	}
	
	void tagAnImage() {
		Intent tagger = new Intent(TagModeActivity.this, GraffitiTaggerActivity.class);
		Bundle tagInfo = new Bundle();
		tagInfo.putString("path", currentImage);
		//tagInfo.putDouble("latitude", gps.latitude);
		//tagInfo.putDouble("longitude", gps.longitude);
		//tagInfo.putDouble("altitude", gps.altitude);
		tagInfo.putDouble("latitude", latitude);
		tagInfo.putDouble("longitude", longitude);
		tagInfo.putDouble("altitude", altitude);
		tagger.putExtras(tagInfo);
		//tagger.putExtra("imagePath", currentImage);
		startActivityForResult(tagger, FILE_TAGGER);
	}
	
	void openLayar() {
		Intent layar = new Intent(Intent.ACTION_VIEW);
		layar.addCategory(Intent.CATEGORY_BROWSABLE);
		layar.setData(Uri.parse(LAYAR_INTENT));
		//layar.setData(Uri.parse(LAYAR_INTENT));
		startActivity(layar);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case FILE_CHOOSER:
				switch(resultCode) {
					case Activity.RESULT_OK:
						Bundle dataBundle = data.getExtras();
						//currentImage = data.getDataString();
						currentImage = dataBundle.getString("path");
						((TextView) findViewById(R.id.filepathname)).setText(currentImage);
						break;
					case Activity.RESULT_CANCELED:
					default:
						break;
				}
				break;
			case FILE_TAGGER:
				switch(resultCode) {
					case Activity.RESULT_OK:
						//toast tag successful
						Toast.makeText(this, "Tagging was a success!", Toast.LENGTH_SHORT).show();
						break;
					case TagModeActivity.RESULT_FAIL:
						//toast tag failure
						Toast.makeText(this, "Tagging has failed...", Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
					default:
						break;
				}
				break;
			default:
				break;
		}
	}
	
}
