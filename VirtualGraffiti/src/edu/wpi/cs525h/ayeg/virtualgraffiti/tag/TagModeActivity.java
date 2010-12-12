package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import android.app.Activity;
import android.os.Bundle;

public class TagModeActivity extends Activity {

	final String LAYAR_INTENT = "http://m.layar.com/open/" + ConnectionUtil.LAYER_NAME;
	final String LAYAR_REFRESH = "?action=refresh";
	
	String currentImage="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
}
