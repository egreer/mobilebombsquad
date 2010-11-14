package jp.androidgroup.nyartoolkit.hardware;

import java.io.IOException;

import jp.androidgroup.nyartoolkit.NyARToolkitAndroidActivity;
import jp.androidgroup.nyartoolkit.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Config;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * It is implementing of CameraIF for Nexus One.
 * 
 * @author Atsuo Igarashi
 *
 */
public class N1Camera implements CameraIF {
	
	private static final String TAG = "N1Camera";

	private PreviewCallback cb = null;
	
	public static final int SCREEN_DELAY = 2 * 60 * 1000;
    
	private android.hardware.Camera.Parameters mParameters;

	private SharedPreferences mPreferences;
	
	public static final int IDLE = 1;
	public static final int SNAPSHOT_IN_PROGRESS = 2;
	public static final int SNAPSHOT_COMPLETED = 3;
	
	private int mStatus = IDLE;

	private Camera mCameraDevice;
	
	private NyARToolkitAndroidActivity mMainActivity;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder = null;

	private int mViewFinderWidth, mViewFinderHeight;
	private boolean mPreviewing = false;
	
	private boolean mPausing = false;

	private LocationManager mLocationManager = null;

	private Handler mHandler = null; 
	
	
	private LocationListener [] mLocationListeners = new LocationListener[] {
		new LocationListener(LocationManager.GPS_PROVIDER),
		new LocationListener(LocationManager.NETWORK_PROVIDER)
	};
	

	public N1Camera(NyARToolkitAndroidActivity mMainActivity, SurfaceView mSurfaceView) {
		Log.d(TAG, "instance");
		
		this.mMainActivity = mMainActivity;
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
		mLocationManager = (LocationManager) mMainActivity.getSystemService(Context.LOCATION_SERVICE);

		this.mSurfaceView = mSurfaceView;
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(mMainActivity);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mHandler = mMainActivity.getMessageHandler();
	}
	
	@Override
	public Parameters getParameters() {
		return mCameraDevice.getParameters();
	}

	@Override
	public void setParameters(Parameters params) {
		mCameraDevice.setParameters(params);
	}

	@Override
	public void setPreviewCallback(PreviewCallback cb) {
		this.cb = cb;
		
	}

	
	@Override
	public void onStart() {
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}		

	@Override
	public void resetPreviewSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		
		mHandler.sendEmptyMessageDelayed(NyARToolkitAndroidActivity.CLEAR_SCREEN_DELAY, SCREEN_DELAY);
		
		mPausing = false;
		
		if (mPreferences.getBoolean("pref_camera_recordlocation_key", false))
			startReceivingLocationUpdates();
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onPause() {

		mPausing = true;
		stopPreview();

		closeCamera();

		stopReceivingLocationUpdates();

		mHandler.removeMessages(NyARToolkitAndroidActivity.CLEAR_SCREEN_DELAY);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	Log.d(TAG, "surfaceChanged");
    	
        // We need to save the holder for later use, even when the mCameraDevice
        // is null. This could happen if onResume() is invoked after this
        // function.
    	mSurfaceHolder = holder;

    	mViewFinderWidth = w;
    	mViewFinderHeight = h;

        // The mCameraDevice will be null if it fails to connect to the camera
        // hardware. In this case we will show a dialog and then finish the
        // activity, so it's OK to ignore it.
        if (mCameraDevice == null) return;

        // Sometimes surfaceChanged is called after onPause.
        // Ignore it.
        if (mPausing || mMainActivity.isFinishing()) return;
		
		Thread startPreviewThread = new Thread(new Runnable() {
			public void run() {
				startPreview();
			}
		});
		startPreviewThread.start();

		// Make sure preview is started.
		try {
			startPreviewThread.join();
		} catch (InterruptedException ex) {
			// ignore
		}
    	
        if (mPreviewing && holder.isCreating()) {
            // Set preview display if the surface is being created and preview
            // was already started. That means preview display was set to null
            // and we need to set it now.
            setPreviewDisplay(holder);
        } else {
            // 1. Restart the preview if the size of surface was changed. The
            // framework may not support changing preview display on the fly.
            // 2. Start the preview now if surface was destroyed and preview
            // stopped.
            restartPreview();
        }
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		stopPreview();
		mSurfaceHolder = null;
	}
	

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		
			case NyARToolkitAndroidActivity.RESTART_PREVIEW: {
				if (mStatus == SNAPSHOT_IN_PROGRESS) {
					// We are still in the processing of taking the picture, wait.
					// This is is strange.  Why are we polling?
					// TODO remove polling
					mHandler.sendEmptyMessageDelayed(NyARToolkitAndroidActivity.RESTART_PREVIEW, 100);
				}
				break;
			}
            
            case NyARToolkitAndroidActivity.SHOW_LOADING: {
            	stopPreview();
    			break;
    		}

            case NyARToolkitAndroidActivity.HIDE_LOADING: {
            	startPreview();
    			break;
    		}
		}
	}
	
//----------------------- Camera's method ------------------------
	
	private void closeCamera() {
		if (mCameraDevice != null) {
        	stopPreview();
			mCameraDevice.release();
			mCameraDevice = null;
			mPreviewing = false;
		}
	}
	
	private boolean ensureCameraDevice() {
		if (mCameraDevice == null) {
			mCameraDevice = Camera.open();
		}
		return mCameraDevice != null;
	}
	
	public void restartPreview() {
    	Log.d(TAG, "restartPreview");
		// make sure the surfaceview fills the whole screen when previewing
        mSurfaceView.requestLayout();
        mSurfaceView.invalidate();
        startPreview();
	}
    
    private void setPreviewDisplay(SurfaceHolder holder) {
    	try {
    		mCameraDevice.setPreviewDisplay(holder);
    	} catch (Throwable ex) {
			closeCamera();
			throw new RuntimeException("setPreviewDisplay failed", ex);
		}
    }

	private void startPreview() {
        if (mPausing || mMainActivity.isFinishing()) return;

        ensureCameraDevice();

        // If we're previewing already, stop the preview first (this will blank
        // the screen).
        if (mPreviewing) stopPreview();

    	setPreviewDisplay(mSurfaceHolder);
        setCameraParameters();

        //mCameraDevice.setPreviewCallback(mPreviewCallback);
        mCameraDevice.setPreviewCallback(cb);

		try {
			Log.v(TAG, "startPreview");
			mCameraDevice.startPreview();
		} catch (Throwable ex) {
			closeCamera();
			throw new RuntimeException("startPreview failed", ex);
		}
        mPreviewing = true;    	
        mStatus = IDLE;
	}

	private void stopPreview() {
        if (mCameraDevice != null && mPreviewing) {
        	Log.v(TAG, "stopPreview");
            mCameraDevice.setPreviewCallback(null);
        	mCameraDevice.stopPreview();
        }
        mPreviewing = false;
	}

    private void setCameraParameters() {
    	mParameters = mCameraDevice.getParameters();

//    	mParameters.setPreviewSize(mViewFinderWidth, mViewFinderHeight);
    	mParameters.setPreviewSize(320, 240);

    	mCameraDevice.setParameters(mParameters);
    }
	
	private void startReceivingLocationUpdates() {
		if (mLocationManager != null) {
			try {
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 
						1000, 
						0F, 
						mLocationListeners[1]);
			} catch (java.lang.SecurityException ex) {
				// ok
			} catch (IllegalArgumentException ex) {
				if (Config.LOGD) {
					Log.d(TAG, "provider does not exist " + ex.getMessage());
				}
			}
			try {
				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 
						1000, 
						0F, 
						mLocationListeners[0]);
			} catch (java.lang.SecurityException ex) {
				// ok
			} catch (IllegalArgumentException ex) {
				if (Config.LOGD) {
					Log.d(TAG, "provider does not exist " + ex.getMessage());
				}
			}
		}
	}
	
	private void stopReceivingLocationUpdates() {
		if (mLocationManager != null) {
			for (int i = 0; i < mLocationListeners.length; i++) {
				try {
					mLocationManager.removeUpdates(mLocationListeners[i]);
				} catch (Exception ex) {
					// ok
				}
			}
		}
	}
	
	

	public Location getCurrentLocation() {
		Location l = null;
		
		// go in worst to best order
		for (int i = 0; i < mLocationListeners.length && l == null; i++) {
			l = mLocationListeners[i].current();
		}		
		return l;
	}
	private class LocationListener implements android.location.LocationListener {
		private Location mLastLocation;
		private boolean mValid = false;
		private String mProvider;
		
		public LocationListener(String provider) {
			mProvider = provider;
			mLastLocation = new Location(mProvider);
		}
		
		public void onLocationChanged(Location newLocation) {
			if (newLocation.getLatitude() == 0.0 && newLocation.getLongitude() == 0.0) {
				// Hack to filter out 0.0,0.0 locations
				return;
			}
			mLastLocation.set(newLocation);
			mValid = true;
		}
		
		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
			mValid = false;
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			if (status == LocationProvider.OUT_OF_SERVICE) {
				mValid = false;
			}
		}
		
		public Location current() {
			return mValid ? mLastLocation : null;
		}
	};
}
