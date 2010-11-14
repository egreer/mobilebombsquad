package jp.androidgroup.nyartoolkit;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import jp.androidgroup.nyartoolkit.hardware.CameraIF;
import jp.androidgroup.nyartoolkit.hardware.HT03ACamera;
import jp.androidgroup.nyartoolkit.hardware.UVCCamera;
import jp.androidgroup.nyartoolkit.hardware.N1Camera;
import jp.androidgroup.nyartoolkit.hardware.SocketCamera;
import jp.androidgroup.nyartoolkit.hardware.StaticCamera;
import jp.androidgroup.nyartoolkit.view.GLSurfaceView;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class NyARToolkitAndroidActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {
	
	public static final String TAG = "NyARToolkitAndroid";
	
	public static final int CROP_MSG = 1;
    public static final int FIRST_TIME_INIT = 2;
	public static final int RESTART_PREVIEW = 3;
	public static final int CLEAR_SCREEN_DELAY = 4;
	public static final int SHOW_LOADING = 5;
	public static final int HIDE_LOADING = 6;
	public static final int KEEP = 7;

    private static final int SCREEN_DELAY = 2 * 60 * 1000;
	

    private OrientationEventListener mOrientationListener;
    private int mLastOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
    private SharedPreferences mPreferences;

    private boolean mTranslucentBackground = true;
    private boolean isYuv420spPreviewFormat = false;

	private CameraIF mCameraDevice;
    private SurfaceHolder mSurfaceHolder = null;
	private GLSurfaceView mGLSurfaceView;
	private ModelRenderer mRenderer;

	private boolean mFirstTimeInitialized;

	private PreviewCallback mPreviewCallback = new JpegPreviewCallback();


	private ARToolkitDrawer arToolkit = null;
	
	private Handler mHandler = new MainHandler(); 

	private boolean isUseSerface = false;
	
	private boolean drawFlag = false;
	

    // Snapshots can only be taken after this is called. It should be called
    // once only. We could have done these things in onCreate() but we want to
    // make preview screen appear as soon as possible.
    private void initializeFirstTime() {
        if (mFirstTimeInitialized) return;

        Log.d(TAG, "initializeFirstTime");

        // Create orientation listenter. This should be done first because it
        // takes some time to get first orientation.
        mOrientationListener =
                new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                // We keep the last known orientation. So if the user
                // first orient the camera then point the camera to
                // floor/sky, we still have the correct orientation.
                if (orientation != ORIENTATION_UNKNOWN) {
                    mLastOrientation = orientation;
                }
            }
        };
        mOrientationListener.enable();

        mFirstTimeInitialized = true;
    }

    // If the activity is paused and resumed, this method will be called in
    // onResume.
    private void initializeSecondTime() {
		Log.d(TAG, "initializeSecondTime");

		// Start orientation listener as soon as possible because it takes
        // some time to get first orientation.
        mOrientationListener.enable();
    }

//----------------------- Override Methods ------------------------
	
	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Renderer
		String[] modelName = new String[2];
		modelName[0] = "droid.mqo";
		modelName[1] = "miku01.mqo";
		float[] modelScale = new float[] {0.008f, 0.01f};
		mRenderer = new ModelRenderer(mTranslucentBackground, getAssets(), modelName, modelScale);
		mRenderer.setMainHandler(mHandler);

		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// init Camera.
		if(getString(R.string.camera_name).equals("jp.androidgroup.nyartoolkit.hardware.SocketCamera")) {
			mCameraDevice = new SocketCamera(getString(R.string.server_addr),
					Integer.valueOf(getString(R.string.server_port)));
			setContentView(R.layout.camera);
			mGLSurfaceView = (GLSurfaceView) findViewById(R.id.GL_view);
			// OpenGL Verw
			mGLSurfaceView.setRenderer(mRenderer);
			mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
			
		} else if(getString(R.string.camera_name).equals("jp.androidgroup.nyartoolkit.hardware.StaticCamera")) {
			mCameraDevice = new StaticCamera(getAssets());
			setContentView(R.layout.camera);
			mGLSurfaceView = (GLSurfaceView) findViewById(R.id.GL_view);
			// OpenGL Verw
			mGLSurfaceView.setRenderer(mRenderer);
			mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
			
		} else if(getString(R.string.camera_name).equals("jp.androidgroup.nyartoolkit.hardware.HT03ACamera")) {
			isUseSerface = true;
			isYuv420spPreviewFormat = true;

			if (mTranslucentBackground) {
				mGLSurfaceView = new GLSurfaceView(this);
				mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
				mGLSurfaceView.setRenderer(mRenderer);
				mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			
				SurfaceView mSurfaceView = new SurfaceView(this);
				mCameraDevice = new HT03ACamera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				setContentView(mGLSurfaceView);
				addContentView(mSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				setContentView(R.layout.ht03acamera);
				SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.HT03A_camera_preview);
				mCameraDevice = new HT03ACamera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				mGLSurfaceView = (GLSurfaceView) findViewById(R.id.HT03A_GL_view);
				mGLSurfaceView.setRenderer(mRenderer);
			}
		} else if(getString(R.string.camera_name).equals("jp.androidgroup.nyartoolkit.hardware.UVCCamera")) {
			isUseSerface = true;
			
			if (mTranslucentBackground) {
				mGLSurfaceView = new GLSurfaceView(this);
				mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
				mGLSurfaceView.setRenderer(mRenderer);
				mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			
				SurfaceView mSurfaceView = new SurfaceView(this);
				mCameraDevice = new UVCCamera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				setContentView(mGLSurfaceView);
				addContentView(mSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				setContentView(R.layout.uvccamera);
				SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.UVC_camera_preview);
				mCameraDevice = new UVCCamera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				mGLSurfaceView = (GLSurfaceView) findViewById(R.id.UVC_GL_view);
				mGLSurfaceView.setRenderer(mRenderer);
			}
		} else if (getString(R.string.camera_name).equals("jp.androidgroup.nyartoolkit.hardware.N1Camera")) {
			isUseSerface = true;
			isYuv420spPreviewFormat = true;

			if (mTranslucentBackground) {
				mGLSurfaceView = new GLSurfaceView(this);
				mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
				mGLSurfaceView.setRenderer(mRenderer);
				mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

				SurfaceView mSurfaceView = new SurfaceView(this);
				mCameraDevice = new N1Camera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				setContentView(mGLSurfaceView);
				addContentView(mSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				setContentView(R.layout.n1camera);
				SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.N1_camera_preview);
				mCameraDevice = new N1Camera(this, mSurfaceView);
				mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

				mGLSurfaceView = (GLSurfaceView) findViewById(R.id.N1_GL_view);
				mGLSurfaceView.setRenderer(mRenderer);
			}
		}
		
		mCameraDevice.setPreviewCallback(mPreviewCallback);
		
		// init ARToolkit.
		InputStream camePara = getResources().openRawResource(
				R.raw.camera_para);
		ArrayList<InputStream> patt = new ArrayList<InputStream>();
		patt.add(getResources().openRawResource(R.raw.patthiro));
		patt.add(getResources().openRawResource(R.raw.pattkanji));
		arToolkit = new ARToolkitDrawer(camePara, patt, mRenderer, mTranslucentBackground, isYuv420spPreviewFormat);
		
		//TODO init VoicePlayer for ARToolkit.
//		VoicePlayer mVoiceSound = new VoicePlayer();
//		mVoiceSound.initVoice(getResources().openRawResourceFd(R.raw.xxx_voice));
//		arToolkit.setVoicePlayer(mVoiceSound);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mCameraDevice.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
		Log.d("Main", "onResume");
		
		mCameraDevice.onResume();

        if (mSurfaceHolder != null) {
            // If first time initialization is not finished, put it in the
            // message queue.
            if (!mFirstTimeInitialized) {
                mHandler.sendEmptyMessage(FIRST_TIME_INIT);
            } else {
                initializeSecondTime();
            }
        }
        
        mHandler.sendEmptyMessageDelayed(CLEAR_SCREEN_DELAY, SCREEN_DELAY);
	}

	@Override
	public void onStop() {
		mCameraDevice.onStop();
		super.onStop();
	}

	@Override
	protected void onPause() {
		mOrientationListener.disable();
		mCameraDevice.onPause();
		mGLSurfaceView.onPause();
		super.onPause();
	}
	
	@Override  
	protected void onDestroy() {
		mCameraDevice.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		;;
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;

			case MotionEvent.ACTION_MOVE:
				break;

			case MotionEvent.ACTION_UP:
				break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CROP_MSG: {
				Intent intent = new Intent();
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						intent.putExtras(extras);
					}
				}
				setResult(resultCode, intent);
				finish();
				break;
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.d(TAG, "surfaceChanged");

		// Make sure we have a surface in the holder before proceeding.
        if (holder.getSurface() == null) {
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }

        // We need to save the holder for later use, even when the mCameraDevice
        // is null. This could happen if onResume() is invoked after this
        // function.
        mSurfaceHolder = holder;

        if(!isUseSerface) {
			return;
		}
		
		if(mCameraDevice instanceof HT03ACamera) {
			HT03ACamera cam = (HT03ACamera)mCameraDevice;
			cam.surfaceChanged(holder, format, w, h);
		}
		else if(mCameraDevice instanceof UVCCamera) {
			UVCCamera cam = (UVCCamera)mCameraDevice;
			cam.surfaceChanged(holder, format, w, h);
		}
		else if (mCameraDevice instanceof N1Camera) {
			N1Camera cam = (N1Camera)mCameraDevice;
			cam.surfaceChanged(holder, format, w, h);
		}

        // If first time initialization is not finished, send a message to do
        // it later. We want to finish surfaceChanged as soon as possible to let
        // user see preview first.
        if (!mFirstTimeInitialized) {
            mHandler.sendEmptyMessage(FIRST_TIME_INIT);
        } else {
            initializeSecondTime();
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(!isUseSerface) {
			return;
		}
		if(mCameraDevice instanceof HT03ACamera) {
			HT03ACamera cam = (HT03ACamera)mCameraDevice;
			cam.surfaceCreated(holder);
		}
		else if(mCameraDevice instanceof UVCCamera) {
			UVCCamera cam = (UVCCamera)mCameraDevice;
			cam.surfaceCreated(holder);
		}
		else if (mCameraDevice instanceof N1Camera) {
			N1Camera cam = (N1Camera)mCameraDevice;
			cam.surfaceCreated(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(!isUseSerface) {
			return;
		}
		if(mCameraDevice instanceof HT03ACamera) {
			HT03ACamera cam = (HT03ACamera)mCameraDevice;
			cam.surfaceDestroyed(holder);
		}
		else if(mCameraDevice instanceof UVCCamera) {
			UVCCamera cam = (UVCCamera)mCameraDevice;
			cam.surfaceDestroyed(holder);
		}
		else if (mCameraDevice instanceof N1Camera) {
			N1Camera cam = (N1Camera)mCameraDevice;
			cam.surfaceDestroyed(holder);
		}

		mSurfaceHolder = null;
	}
	
	
	
	
// ---------------------- getter & setter ---------------------------	

	public int getLastOrientation() {
		return mLastOrientation;
	}
	
	
// ---------------------------- Utils ---------------------------------	

	public static int roundOrientation(int orientationInput) {
		Log.d("roundOrientation", "orientationInput:" + orientationInput);
		int orientation = orientationInput;
		if (orientation == -1)
			orientation = 0;
		
		orientation = orientation % 360;
		int retVal;
		if (orientation < (0*90) + 45) {
			retVal = 0;
		} else if (orientation < (1*90) + 45) {
			retVal = 90;
		} else if (orientation < (2*90) + 45) {
			retVal = 180;
		} else if (orientation < (3*90) + 45) {
			retVal = 270;
		} else {
			retVal = 0;
		}

		return retVal;
	}	
	public static Matrix GetDisplayMatrix(Bitmap b, ImageView v) {
		Matrix m = new Matrix();
		float bw = (float)b.getWidth();
		float bh = (float)b.getHeight();
		float vw = (float)v.getWidth();
		float vh = (float)v.getHeight();
		float scale, x, y;
		if (bw*vh > vw*bh) {
			scale = vh / bh;
			x = (vw - scale*bw)*0.5F;
			y = 0;
		} else {
			scale = vw / bw;
			x = 0;
			y = (vh - scale*bh)*0.5F;
		}
		m.setScale(scale, scale, 0.5F, 0.5F);
		m.postTranslate(x, y);
		return m;
	}
	
// ---------------------------- Callback classes ---------------------------------	
	
	
	private final class JpegPreviewCallback implements PreviewCallback {

		@Override
		public void onPreviewFrame(byte [] jpegData, Camera camera) {
			Log.d(TAG, "JpegPictureCallback.onPreviewFrame");
			
			if(jpegData != null) {
				Log.d(TAG, "data exist");
				arToolkit.draw(jpegData);
			} else {
				try {
					// The measure against over load. 
					Thread.sleep(500);
				} catch (InterruptedException e) {
					;
				}
			}
		}

	};	
	
	
// ---------------------------- Handler classes ---------------------------------	
	
	/** This Handler is used to post message back onto the main thread of the application */
	private class MainHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mCameraDevice.handleMessage(msg);
			switch (msg.what) {
				case KEEP: {
					if (msg.obj != null) {
						mHandler.post((Runnable)msg.obj);
					}
					break;
				}
			
				case CLEAR_SCREEN_DELAY: {
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					break;
				}

                case FIRST_TIME_INIT: {
                    initializeFirstTime();
                    break;
                }

                case SHOW_LOADING: {
					showDialog(DIALOG_LOADING);
					break;
				}
				case HIDE_LOADING: {
					try {
						dismissDialog(DIALOG_LOADING);
						removeDialog(DIALOG_LOADING);
					} catch (IllegalArgumentException e) {
					}
					break;
				}
			}
		}
	}

	public Handler getMessageHandler() {
		return this.mHandler;
	}
	
	
	
	private static final int DIALOG_LOADING = 0;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOADING: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Loading ...");
			// dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.getWindow().setFlags
				(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			return dialog;
		}
		default:
			return super.onCreateDialog(id);
		}
	}
	
}

