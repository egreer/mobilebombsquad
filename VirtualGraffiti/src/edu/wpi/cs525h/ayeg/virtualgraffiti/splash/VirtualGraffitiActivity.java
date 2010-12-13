package edu.wpi.cs525h.ayeg.virtualgraffiti.splash;

import edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity;
import edu.wpi.cs525h.ayeg.virtualgraffiti.R;
import edu.wpi.cs525h.ayeg.virtualgraffiti.tag.TagModeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.View;

public class VirtualGraffitiActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		new CountDownTimer(2000,2000) {

			@Override
			public void onFinish() {
				Intent intent = new Intent(VirtualGraffitiActivity.this, TagModeActivity.class);
				startActivity(intent);
			}

			@Override
			public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			}
		}.start();

		
	}
	
	public void buildMode(View view) {
		Intent intent = new Intent(VirtualGraffitiActivity.this, GraffitiBuilderActivity.class);
		startActivity(intent);
	}
	
	public void tagMode(View view) {
		Intent intent = new Intent(VirtualGraffitiActivity.this, TagModeActivity.class);
		startActivity(intent);
	}
	
	public void quitApp(View view) {
		finish();
	}
}
