package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import edu.wpi.cs525h.ayeg.virtualgraffiti.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TagModeActivity extends Activity {

	static final String SDCARD_LOCATION = "file:///sdcard/";
	public static final String TYPE_EXTERNAL = null;
	final String LAYAR_INTENT = "http://m.layar.com/open/" + ConnectionUtil.LAYER_NAME;
	final String LAYER_INTENT_PARAM = LAYAR_INTENT + "?";
	final String LAYAR_REFRESH = "action=refresh";
	
	/**
	 * Request Codes
	 */
	private final int FILE_CHOOSER = 1;
	private final int FILE_TAGGER = 2;
	private final int RESULT_ERROR = 3;
	
	String currentImage="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tagmode);
	}
	
	void chooseAnImage() {
		Intent chooser = new Intent (TagModeActivity.this, CheckFileManagerActivity.class);
		chooser.setData(Uri.parse(SDCARD_LOCATION));
		startActivityForResult(chooser, FILE_CHOOSER);
	}
	
	void tagAnImage() {
		Intent tagger = new Intent(TagModeActivity.this, GraffitiTaggerActivity.class);
		tagger.putExtra("imagePath", currentImage);
		startActivityForResult(tagger, FILE_TAGGER);
	}
	
	void openLayar() {
		Intent layar = new Intent("");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case FILE_CHOOSER:
				switch(resultCode) {
					case Activity.RESULT_OK:
						currentImage = data.getDataString();
						break;
					case Activity.RESULT_CANCELED:
					default:
						break;
				}
				break;
			case FILE_TAGGER:
				break;
			case RESULT_ERROR:
			default:
				break;
		}
	}
	
}
