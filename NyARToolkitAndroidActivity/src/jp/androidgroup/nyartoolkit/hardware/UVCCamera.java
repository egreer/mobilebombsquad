package jp.androidgroup.nyartoolkit.hardware;

import java.io.*;

import jp.androidgroup.nyartoolkit.NyARToolkitAndroidActivity;
import jp.androidgroup.nyartoolkit.model.VoicePlayer;
import jp.androidgroup.nyartoolkit.view.GLSurfaceView;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Config;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class UVCCamera extends Activity implements CameraIF {
    
	public static final String TAG = "UVCCamera";

	private PreviewCallback mJpegPreviewCallback = null;
    
	private android.hardware.Camera.Parameters mParameters;

	// The parameter strings to communicate with camera driver.
	public static final String PARM_PREVIEW_SIZE = "preview-size";
	public static final String PARM_PICTURE_SIZE = "picture-size";
	public static final String PARM_JPEG_QUALITY = "jpeg-quality";
	public static final String PARM_ROTATION = "rotation";
	public static final String PARM_GPS_LATITUDE = "gps-latitude";
	public static final String PARM_GPS_LONGITUDE = "gps-longitude";
	public static final String PARM_GPS_ALTITUDE = "gps-altitude";
	public static final String PARM_GPS_TIMESTAMP = "gps-timestamp";
	public static final String SUPPORTED_ZOOM = "zoom-values";
	public static final String SUPPORTED_PICTURE_SIZE = "picture-size-values";
	
    private SharedPreferences mPreferences;
    
    public static final int IDLE = 1;
    public static final int SNAPSHOT_IN_PROGRESS = 2;
    public static final int SNAPSHOT_COMPLETED = 3;
    
    private int mStatus = IDLE;

    private android.hardware.Camera mCameraDevice;

	private NyARToolkitAndroidActivity mMainActivity;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder = null;
    
	private int mViewFinderWidth, mViewFinderHeight;

	private ImageCapture mImageCapture = null;

	private boolean mPreviewing;
	private boolean mPausing;
	private boolean mRecordLocation;

	private static final int FOCUS_NOT_STARTED = 0;
	private static final int FOCUSING = 1;
	private static final int FOCUSING_SNAP_ON_FINISH = 2;
	private static final int FOCUS_SUCCESS = 3;
	private static final int FOCUS_FAIL = 4;
	private int mFocusState = FOCUS_NOT_STARTED;
	
	private LocationManager mLocationManager = null;

    private final OneShotPreviewCallback mOneShotPreviewCallback = new OneShotPreviewCallback();
    private final AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();
    
    private String mFocusMode;
    
    private Handler mHandler = null;

    private MediaPlayer mVoiceSound = null;

    
    private LocationListener [] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    

    public UVCCamera(NyARToolkitAndroidActivity mMainActivity, SurfaceView mSurfaceView) {
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
	public void setPreviewCallback(PreviewCallback callback) {
		mJpegPreviewCallback = callback;
	}

	@Override
	public void setParameters(Parameters params) {
		mCameraDevice.setParameters(params);
	}
	
	@Override
	public Parameters getParameters() {
		return mCameraDevice.getParameters();
	}
	
	@Override
	public void resetPreviewSize(int width, int height) {
	}
    
    /**
     * This Handler is used to post message back onto the main thread of the
     * application
     */
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case NyARToolkitAndroidActivity.RESTART_PREVIEW: {
                if (mStatus == SNAPSHOT_IN_PROGRESS) {
                    // We are still in the processing of taking the picture, wait.
                    // This is is strange.  Why are we polling?
                    // TODO remove polling
                	Log.d(TAG, "sendEmptyMessageDelayed(RESTART_PREVIEW)");
                    mHandler.sendEmptyMessageDelayed(NyARToolkitAndroidActivity.RESTART_PREVIEW, 100);
                }
                else
                    restartPreview();
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

    private class LocationListener
    		implements android.location.LocationListener {
    	Location mLastLocation;
    	boolean mValid = false;
    	String mProvider;
        
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
	
    private final class OneShotPreviewCallback
    		implements android.hardware.Camera.PreviewCallback {

    	@Override
    	public void onPreviewFrame(byte[] data,
    								android.hardware.Camera camera) {
            Log.d(TAG, "OneShotPreviewCallback.onPreviewFrame");
            
        	if(data != null) {
				Log.d(TAG, "data exist");

			    autoFocus();
        	}
        }

    };    
    
    private final class AutoFocusCallback
    		implements android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(
        		boolean focused, android.hardware.Camera camera) {
			Log.d(TAG, "AutoFocusCallback.onAutoFocus");

			if (mFocusState == FOCUSING_SNAP_ON_FINISH) {
        		if (focused) {
        			mFocusState = FOCUS_SUCCESS;
        		} else {
        			mFocusState = FOCUS_FAIL;
        		}
        		mImageCapture.onSnap();
        	} else if (mFocusState == FOCUSING) {
        		if (focused) {
        			mFocusState = FOCUS_SUCCESS;
        		} else {
        			mFocusState = FOCUS_FAIL;
        		}
        		mImageCapture.onSnap();
        	} else if (mFocusState == FOCUS_NOT_STARTED) {
        	}
        }
    };
	
    private final class JpegPictureCallback implements PictureCallback {
    	Location mLocation;
    	
    	public JpegPictureCallback(Location loc) {
    		mLocation = loc;
    	}

    	public void onPictureTaken(
        		byte [] jpegData, android.hardware.Camera camera) {
            if (mPausing) {
                return;
            }

            Log.d(TAG, "JpegPictureCallback.onPictureTaken");
            
            mStatus = SNAPSHOT_COMPLETED;
            
            stopPreview();
            restartPreview();
        	if(jpegData != null) {
				Log.d(TAG, "jpegData exist");
				mJpegPreviewCallback.onPreviewFrame(jpegData, null);
        	}
        }
    };
	
	public class ImageCapture {
	    
	    private boolean mCancel = false;
	    
	    /*
	     * Initiate the capture of an image.
	     */
	    public void initiate() {
	        if (mCameraDevice == null) {
	            return;
	        }
	        
	        mCancel = true;

	        capture();
	    }
	  
	    private void capture() {

	        mParameters.remove(PARM_GPS_LATITUDE);
	        mParameters.remove(PARM_GPS_LONGITUDE);
	        mParameters.remove(PARM_GPS_ALTITUDE);
	        mParameters.remove(PARM_GPS_TIMESTAMP);
	        
            // Set GPS location.
	        Location loc = mRecordLocation ? getCurrentLocation() : null;
	        if (loc != null) {
	        	double lat = loc.getLatitude();
	        	double lon = loc.getLongitude();
	        	boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);

	        	if (hasLatLon) {
	        		String latString = String.valueOf(lat);
	        		String lonString = String.valueOf(lon);
	    	        mParameters.set(PARM_GPS_LATITUDE, latString);
	    	        mParameters.set(PARM_GPS_LONGITUDE, lonString);
	    	        if (loc.hasAltitude()) {
		    	        mParameters.set(PARM_GPS_ALTITUDE,
		    	        				String.valueOf(loc.getAltitude()));
	    	        } else {
                        // for NETWORK_PROVIDER location provider, we may have
                        // no altitude information, but the driver needs it, so
                        // we fake one.
		    	        mParameters.set(PARM_GPS_ALTITUDE, "0");
	    	        }
	    	        if (loc.getTime() != 0) {
                        // Location.getTime() is UTC in milliseconds.
                        // gps-timestamp is UTC in seconds.
	    	        	long utcTimeSeconds = loc.getTime() / 1000;
		    	        mParameters.set(PARM_GPS_TIMESTAMP,
    	        				String.valueOf(utcTimeSeconds));
	    	        }
	        	} else {
	        		loc = null;
		        }
	        }

	        mCameraDevice.setParameters(mParameters);
	   
	        mCameraDevice.takePicture(null, null, new JpegPictureCallback(loc));
	        mPreviewing = false;
	    }

	    public void onSnap() {
	    	// If we are already in the middle of taking a snapshot then ignore.
            if (mPausing || mStatus == SNAPSHOT_IN_PROGRESS) {
	    		return;
	        }

	        mStatus = SNAPSHOT_IN_PROGRESS;

	        mImageCapture.initiate();
	    }
	}
    
    @Override
    public void onStart() {
    	Log.d(TAG, "onStart");
    }

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}		
    
    @Override
    public void onResume() {
    	Log.d(TAG, "onResume");

        mPausing = false;
        mImageCapture = new ImageCapture();
    }

	@Override
	public void onStop() {
    	Log.d(TAG, "onStop");
	}

    @Override
    public void onPause() {
    	Log.d(TAG, "onPause");

    	mPausing = true;
        stopPreview();

        closeCamera();

        stopReceivingLocationUpdates();

        mImageCapture = null;
        
        mHandler.removeMessages(NyARToolkitAndroidActivity.CLEAR_SCREEN_DELAY);
        mHandler.removeMessages(NyARToolkitAndroidActivity.RESTART_PREVIEW);
        mHandler.removeMessages(NyARToolkitAndroidActivity.FIRST_TIME_INIT);
        mHandler.removeMessages(NyARToolkitAndroidActivity.SHOW_LOADING);
        mHandler.removeMessages(NyARToolkitAndroidActivity.HIDE_LOADING);

        if (mVoiceSound != null) {
        	mVoiceSound.release();
        	mVoiceSound = null;
        }
    }

    private boolean canTakePicture() {
    	return isCameraIdle() && mPreviewing;
    }
    
    private void autoFocus() {
        if (canTakePicture()) {
        	Log.v(TAG, "Start autofocus. ");
            mFocusState = FOCUSING;
            mCameraDevice.autoFocus(mAutoFocusCallback);
        }
    }
    
    private void clearFocusState() {
        mFocusState = FOCUS_NOT_STARTED;
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
        if (mPausing || isFinishing()) return;
		
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
    	Log.d(TAG, "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.d(TAG, "surfaceDestroyed");

    	stopPreview();
        mSurfaceHolder = null;
    }
    
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
        	Log.d(TAG, "ensureCameraDevice");
            mCameraDevice = android.hardware.Camera.open();
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

        mCameraDevice.setOneShotPreviewCallback(mOneShotPreviewCallback);

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
        	mCameraDevice.stopPreview();
        }
        mPreviewing = false;
        clearFocusState();
    }

    private void setCameraParameters() {
    	mParameters = mCameraDevice.getParameters();
//    	mParameters.setPreviewSize(mViewFinderWidth, mViewFinderHeight);
    	mParameters.setPreviewSize(352, 288);
    	
    	// 352x288 only
    	String previewSize = "352x288";
    	mParameters.set(PARM_PREVIEW_SIZE, previewSize);
    	
    	// 352x288 only, since 640x480 is too big for bitmap
    	String pictureSize = "352x288";
    	mParameters.set(PARM_PICTURE_SIZE, pictureSize);

    	// 85 only
    	String jpegQuality = "85";
    	mParameters.set(PARM_JPEG_QUALITY, jpegQuality);

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
                Log.d(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "provider does not exist " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 
                        1000, 
                        0F, 
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.d(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "provider does not exist " + ex.getMessage());
            }
        }
    }
    
    private void stopReceivingLocationUpdates() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.d(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    public Location getCurrentLocation() {
        // go in worst to best order
        for (int i = 0; i < mLocationListeners.length; i++) {
            Location l = mLocationListeners[i].current();
            if (l != null) return l;
        }        
        return null;
    }
    
    private boolean isCameraIdle() {
    	return mStatus == IDLE && mFocusState == FOCUS_NOT_STARTED;
    	//return mStatus == IDLE;
    }
}
