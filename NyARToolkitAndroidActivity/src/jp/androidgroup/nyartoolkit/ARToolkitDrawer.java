package jp.androidgroup.nyartoolkit;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import jp.androidgroup.nyartoolkit.model.VoicePlayer;
import jp.nyatla.nyartoolkit.NyARException;
/*** for NyARToolkit JAVA I/F
import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.raster.rgb.NyARRgbRaster_RGB;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.core.types.NyARBufferType;
import jp.nyatla.nyartoolkit.detector.NyARDetectMarker;
import jp.nyatla.nyartoolkit.jogl.utils.NyARGLUtil;
***/
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;

public class ARToolkitDrawer {
	
	private static final int PATT_MAX = 1;
	private static final int MARKER_MAX = 1;
	
	/*** for NyARToolkit JAVA I/F
	private NyARDetectMarker nya = null;
	private NyARRgbRaster_RGB raster = null;
	private NyARGLUtil ar_util = null;
	private NyARParam ar_param = null;
	private NyARCode[] ar_code = new NyARCode[PATT_MAX];
	private NyARTransMatResult ar_transmat_result = new NyARTransMatResult();
	***/

	
	private ModelRenderer mRenderer = null;
	private InputStream camePara = null;
	private ArrayList<InputStream> patt = new ArrayList<InputStream>();
    private boolean mTranslucentBackground;
    private boolean isYuv420spPreviewFormat;
	
	private VoicePlayer mVoiceSound = null;

	static {
        System.loadLibrary("yuv420sp2rgb");
    }
    public static native void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height, int type);
    public static native void decodeYUV420SP(byte[] rgb, byte[] yuv420sp, int width, int height, int type);

    /*** for NyARToolkit JNI I/F */
    static {
        System.loadLibrary("NyARToolkit");
    }
    public static native void initNyARParam(int width, int height, double[] projection, double[] dist);
    public static native void initNyARCode(byte[] patt);
    public static native void initNyARRgbRaster(int width, int height);
    public static native void wrapBuffer(byte[] buff);
    public static native void initNyARSingleDetectMarker();
    public static native boolean detectMarkerLite();
    public static native void initNyARGLUtil();
    public static native void toCameraFrustumRHf(float[] cameraRHf);
    public static native void getTransmationMatrix(float[] resultf);
    /***/
	
	public ARToolkitDrawer(InputStream camePara, ArrayList<InputStream> patt, ModelRenderer mRenderer, boolean mTranslucentBackground, boolean isYuv420spPreviewFormat) {
		this.camePara = camePara;
		this.patt = patt;
		this.mRenderer = mRenderer;
		this.mTranslucentBackground = mTranslucentBackground;
		this.isYuv420spPreviewFormat = isYuv420spPreviewFormat;

		/*** for NyARToolkit JNI I/F */
		try {
			byte[] buf = new byte[136];
			int w, h;
			double[] projection = new double[12];
			double[] dist = new double[4];

			camePara.read(buf);
			ByteBuffer bb = ByteBuffer.wrap(buf);
			w = bb.getInt();
			h = bb.getInt();
			for (int i = 0; i < 12; i++){
				projection[i] = bb.getDouble();
			}
			for (int i = 0; i < 4; i++) {
				dist[i] = bb.getDouble();
			}
			initNyARParam(320, 240, projection, dist);
		} catch (Exception e) {
		}
//		initNyARCode(patt.get(0));
		try {
			byte[] buf = new byte[12484];
//			byte[] pattern = new byte[12484];

			patt.get(0).read(buf);
/*			ByteBuffer bb = ByteBuffer.wrap(buf);
			for (int i = 0; i < 12; i++){
				pattern[i] = bb.get();
			}*/
			initNyARCode(buf);
		} catch (Exception e) {
		}
		initNyARRgbRaster(320, 240);
		initNyARSingleDetectMarker();
		initNyARGLUtil();
	    /***/
	}
	
	private void createNyARTool(int w, int h) {
		// NyARToolkit setting.
		/*** for NyARToolkit JAVA I/F
		try {
			if (ar_param == null) {
				ar_util = new NyARGLUtil();
				ar_param = new NyARParam();
				ar_param.loadARParam(camePara);
				for (int i = 0; i < PATT_MAX; i++) {
					ar_code[i] = new NyARCode(16, 16);
					ar_code[i].loadARPatt(patt.get(i));
				}
				//TODO 本当は、ここはifの外でないと行けない。だけど、出すとOutOfMemory
				ar_param.changeScreenSize(w, h);
				double[] width = new double[] {80.0, 80.0};
				nya = new NyARDetectMarker(ar_param, ar_code, width, PATT_MAX, NyARBufferType.BYTE1D_B8G8R8_24);
				nya.setContinueMode(false);
			}
			Log.d("nyar", "resources have been loaded");
		} catch (Exception e) {
			Log.e("nyar", "resource loading failed", e);
		}
		***/

	}

	public void setVoicePlayer(VoicePlayer mVoiceSound) {
		this.mVoiceSound = mVoiceSound;
	}
	
	public void draw(byte[] data) {
		
		if(data == null) {
			Log.d("AR draw", "data= null");
			return;
		}
		Log.d("AR draw", "data.length= " + data.length);
		int width = 320;
		int height = 240;

		Bitmap bitmap = null;
		if (!isYuv420spPreviewFormat) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			if (bitmap == null) {
				Log.d("AR draw", "data is not BitMap data.");
				return;
			}

			if(bitmap.getHeight() < 240) {
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (bitmap == null) {
					Log.d("AR draw", "data is not BitMap data.");
					return;
				}
			}

			width = bitmap.getWidth();
			height = bitmap.getHeight();
			Log.d("AR draw", "bitmap width * height()= " + width + " * " + height);

			mRenderer.setBgBitmap(bitmap);

		} else if (!mTranslucentBackground) {
			// assume YUV420SP
			int[] rgb = new int[(width * height)];
			try {
				bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			} catch (Exception e) {
				Log.d("AR draw", "bitmap create error.");
				return;
			}		
			long time1 = SystemClock.uptimeMillis();
			// convert YUV420SP to ARGB
			decodeYUV420SP(rgb, data, width, height, 2);
			long time2 = SystemClock.uptimeMillis();
			Log.d("ARToolkitDrawer", "ARGB decode time: " + (time2 - time1) + "ms");
			bitmap.setPixels(rgb, 0, width, 0, 0, width, height);

			mRenderer.setBgBitmap(bitmap);
		}

		// start coordinates calculation.
		byte[] buf = new byte[width * height * 4];

		if (!isYuv420spPreviewFormat) {
			int[] rgb = new int[(width * height)];

			bitmap.getPixels(rgb, 0, width, 0, 0, width, height);

			// convert ARGB to RGB24
			for (int i = 0; i < rgb.length; i++) {
				byte r = (byte) (rgb[i] & 0x00FF0000 >> 16);
				byte g = (byte) (rgb[i] & 0x0000FF00 >> 8);
				byte b = (byte) (rgb[i] & 0x000000FF);
				buf[i * 3] = r;
				buf[i * 3 + 1] = g;
				buf[i * 3 + 2] = b;
			}
		} else {
			// assume YUV420SP
			long time1 = SystemClock.uptimeMillis();
			// convert YUV420SP to ARGB
			decodeYUV420SP(buf, data, width, height, 2);
			long time2 = SystemClock.uptimeMillis();
			Log.d("ARToolkitDrawer", "RGB decode time: " + (time2 - time1) + "ms");
		}
		
//		float[][] resultf = new float[MARKER_MAX][16];
		final float[][] resultf = new float[MARKER_MAX][16];

//		int found_markers;
		boolean is_marker_exist = false;
		int ar_code_index[] = new int[MARKER_MAX];
		
		createNyARTool(width, height);
		// Marker detection 
		/*** for NyARToolkit JAVA I/F
		try {
			Log.d("AR draw", "Marker detection.");
			raster = new NyARRgbRaster_RGB(width, height);
			raster.wrapBuffer(buf);
			found_markers = nya.detectMarkerLite(raster, 130);
		} catch (NyARException e) {
			Log.e("AR draw", "marker detection failed", e);
			return;
		}
		***/
		/*** for NyARToolkit JNI I/F */
		wrapBuffer(buf);
		long time1 = SystemClock.uptimeMillis();
		is_marker_exist = detectMarkerLite();
		long time2 = SystemClock.uptimeMillis();
		Log.d("ARToolkitDrawer", "detect Marker time: " + (time2 - time1) + "ms");
		/***/
		
		// An OpenGL object will be drawn if matched.
//		if (found_markers > 0) {
		if (is_marker_exist) {
//			Log.d("AR draw", "!!!!!!!!!!!exist marker." + found_markers + "!!!!!!!!!!!");
			Log.d("AR draw", "!!!!!!!!!!!exist marker.!!!!!!!!!!!");
			// Projection transformation.
//			float[] cameraRHf = new float[16];
			final float[] cameraRHf = new float[16];
			/*** for NyARToolkit JAVA I/F
			ar_util.toCameraFrustumRHf(ar_param, cameraRHf);
			***/
			/*** for NyARToolkit JNI I/F */
			toCameraFrustumRHf(cameraRHf);
Log.d("AR draw", "cameraRHf: " + cameraRHf[0] + ", " + cameraRHf[1] + ", " + cameraRHf[2] + ", " + cameraRHf[3] + ", " + cameraRHf[4] + ", " + cameraRHf[5] + ", " + cameraRHf[6] + ", " + cameraRHf[7] +
		", " + cameraRHf[8] + ", " + cameraRHf[9] + ", " + cameraRHf[10] + ", " + cameraRHf[11] + ", " + cameraRHf[12] + ", " + cameraRHf[13] + ", " + cameraRHf[14] + ", " + cameraRHf[15]);
			/***/

//			if (found_markers > MARKER_MAX)
//				found_markers = MARKER_MAX;
//			for (int i = 0; i < found_markers; i++) {
				/*** for NyARToolkit JAVA I/F
				try {
					ar_code_index[i] = nya.getARCodeIndex(i);
					NyARTransMatResult transmat_result = ar_transmat_result;
					nya.getTransmationMatrix(i, transmat_result);
					ar_util.toCameraViewRHf(transmat_result, resultf[i]);
				} catch (NyARException e) {
					Log.e("AR draw", "getCameraViewRH failed", e);
					return;
				}
			***/
			/*** for NyARToolkit JNI I/F */
			getTransmationMatrix(resultf[0]);
Log.d("AR draw", "resultf: " + resultf[0][0] + ", " + resultf[0][1] + ", " + resultf[0][2] + ", " + resultf[0][3] + ", " + resultf[0][4] + ", " + resultf[0][5] + ", " + resultf[0][6] + ", " + resultf[0][7] +
		", " + resultf[0][8] + ", " + resultf[0][9] + ", " + resultf[0][10] + ", " + resultf[0][11] + ", " + resultf[0][12] + ", " + resultf[0][13] + ", " + resultf[0][14] + ", " + resultf[0][15]);
			/***/
//			}
//			mRenderer.objectPointChanged(found_markers, ar_code_index, resultf, cameraRHf);
			mRenderer.objectPointChanged(1, ar_code_index, resultf, cameraRHf);
			if(mVoiceSound != null)
				mVoiceSound.startVoice();
		} else {
			Log.d("AR draw", "not exist marker.");
			if(mVoiceSound != null)
				mVoiceSound.stopVoice();
			mRenderer.objectClear();
		}

	}
	
}
