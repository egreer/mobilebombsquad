package edu.wpi.cs525h.ayeg.virtualgraffiti.splash;

import edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity;
import edu.wpi.cs525h.ayeg.virtualgraffiti.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

public class VirtualGraffitiActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//Intent intent = new Intent(); 
		
		/*CountDownTimer timer = new CountDownTimer(1000,1000) {

			Intent intent = new Intent();
			
			@Override
			public void onFinish() {
				intent.setClassName("edu.wpi.cs525h.ayeg.virtualgraffiti", "edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity");		
				startActivity(intent);
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		timer.start();*/
		
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 1000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					
				} finally {
					//finish();
					Intent intent = new Intent(); 
					intent.setClassName("edu.wpi.cs525h.ayeg.virtualgraffiti", "edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity");
					startActivity(intent);
				}
			}
		};
		splashThread.start();
		
		/*Intent intent = new Intent(); 
		intent.setClassName("edu.wpi.cs525h.ayeg.virtualgraffiti", "edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity");
		startActivity(intent);*/
		//setContentView(R.layout.main);
	}
	
	public void buildMode(View view) {
		//Intent intent = new Intent(VirtualGraffitiActivity.this, GraffitiBuilderActivity.class);
		//intent.setAction(Intent.ACTION_VIEW);
        //startActivity(intent);
		Intent intent = new Intent();
		intent.setClassName("edu.wpi.cs525h.ayeg.virtualgraffiti", "edu.wpi.cs525h.ayeg.virtualgraffiti.GraffitiBuilderActivity");
		startActivity(intent);
	}
	
	public void quitApp(View view) {
		finish();
	}
}
