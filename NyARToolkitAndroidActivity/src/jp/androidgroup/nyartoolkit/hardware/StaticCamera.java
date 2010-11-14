package jp.androidgroup.nyartoolkit.hardware;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import android.content.res.AssetManager;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Message;
import android.util.Log;

/**
 * It is implementing of CameraIF for Static pictures. 
 * 
 * @author noritsuna
 *
 */
public class StaticCamera implements CameraIF {

	int width = 320;
	int height = 240;
	int picCount = 3;
	
	private List<byte[]> bgArray = new LinkedList<byte[]>();
	private PreviewCallback cb = null;
	private CaptureThread mCaptureThread = null;
	
	public StaticCamera(AssetManager am) {
		for (int i = 0; i <= picCount; i++)	loadBg(i, am);
	}
	
	private InputStream getInputStreamFromAm(AssetManager am, String path) throws Throwable {
		return am.open(path);
	}
	
	private void loadBg(int no, AssetManager am) {
		try {
			InputStream is = null;
			try {
				is = getInputStreamFromAm(am, "CapTest_00"+no+"00.bmp");
				byte[] buf = new byte[width * height * 9];
				is.read(buf, 0 , buf.length);
				
				bgArray.add(buf);
			} finally {
				if (is != null)	is.close();
			}
		} catch (Throwable e) {
		}
	}
	

	@Override
	public Parameters getParameters() {
		return null;
	}

	@Override
	public void onDestroy() {
		this.onStop();
		this.mCaptureThread = null;
	}

	@Override
	public void setParameters(Parameters params) {
	}

	@Override
	public void setPreviewCallback(PreviewCallback cb) {
		this.cb = cb;
	}

	@Override
	public void onStart() {
		Log.d("StaticCamera", "call onStart");
		if(this.mCaptureThread == null) {
			this.mCaptureThread = new CaptureThread();
		}
		if(this.mCaptureThread.mDone) {
			this.mCaptureThread.mDone = false;
			this.mCaptureThread.start();
		}
	}

	@Override
	public void onStop() {
		Log.d("AttachedCamera", "call onStop");
		this.mCaptureThread.mDone = true;
	}		

	@Override
	public void onPause() {
		this.onStop();
	}
	@Override
	public void onResume() {
		this.onStart();
	}

	
	@Override
	public void resetPreviewSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	
	private class CaptureThread extends Thread {
		private boolean mDone = true;
		
		public CaptureThread() {
			super();
			Log.d("CaptureThread", "new");
		}
		@Override
		public void run() {
			Log.d("CaptureThread", "in capture thread");
			while(!mDone) {
				for (byte[] buf: bgArray) {
					try {
						Log.d("CaptureThread", "show static pics.");
						cb.onPreviewFrame(buf, null);
						// Wait next buf.
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
