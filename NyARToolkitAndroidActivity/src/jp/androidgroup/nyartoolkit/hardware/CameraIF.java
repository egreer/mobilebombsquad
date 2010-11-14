package jp.androidgroup.nyartoolkit.hardware;

import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Message;

/**
 * It is an interface for cameras. 
 * 
 * @author noritsuna
 *
 */
public interface CameraIF {
	
    public void setPreviewCallback(PreviewCallback cb);
	
    public void setParameters(Parameters params);
    public Parameters getParameters();
    
    public void resetPreviewSize(int width, int height);
    
    
    public void onStart();
    public void onResume();
    public void onStop();
    public void onPause();
	public void onDestroy();
	
	
	public void handleMessage(Message msg);

}
