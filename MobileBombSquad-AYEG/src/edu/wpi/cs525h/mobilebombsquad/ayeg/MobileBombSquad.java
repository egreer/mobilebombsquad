package edu.wpi.cs525h.mobilebombsquad.ayeg;

import android.app.Activity;
import android.os.Bundle;

public class MobileBombSquad extends Activity {
    //PlayableSurfaceView playrectangle;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //playrectangle = new PlayableSurfaceView(this);
        
        
        
        setContentView(R.layout.main);
        //setContentView(playrectangle);
        
        //gameLoop();
        
        
    }
    
    public void gameLoop() {
    	
    }
}