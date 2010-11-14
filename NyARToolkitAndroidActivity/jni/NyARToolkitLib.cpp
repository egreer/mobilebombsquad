#include <jni.h>
#include <android/log.h>

#include "NyAR_core.h"
#include "core/NyARRgbRaster_RGB.h"
#include "NyARGLUtil.h"

#define  LOG_TAG    "libNyARToolkit"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

using namespace NyARToolkitCPP;
using namespace NyARUtils;

NyARParam ap;
NyARCode code(16, 16);
#if 1
NyARRgbRaster_BGRA* ra;
#else
NyARRgbRaster_RGB* ra;
#endif
NyARSingleDetectMarker* ar;

NyARGLUtil* nyar_gl;
GLdouble gl_camera_frustum_rh[16];
GLdouble gl_camera_view_rh[16];

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_initNyARParam(JNIEnv * env, jobject obj, jint width, jint height, jdoubleArray i_projection, jdoubleArray i_factor)
{
	jdouble* factor = env->GetDoubleArrayElements(i_factor, NULL);
	jdouble* projection = env->GetDoubleArrayElements(i_projection, NULL);

	if ((projection != NULL) && (factor != NULL))
		ap.setValue(factor, projection);
	ap.changeScreenSize(width, height);

	if (projection != NULL)
		env->ReleaseDoubleArrayElements(i_projection, projection, JNI_ABORT);
	if (factor != NULL)
		env->ReleaseDoubleArrayElements(i_factor, factor, JNI_ABORT);
}
	
JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_initNyARCode(JNIEnv * env, jobject obj, jbyteArray i_patt)
{
	jbyte* patt;

	patt = env->GetByteArrayElements(i_patt, NULL);
	if (patt != NULL) {
		code.loadARPatt((unsigned char *)patt);
		env->ReleaseByteArrayElements(i_patt, patt, JNI_ABORT);
	}
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_initNyARRgbRaster(JNIEnv * env, jobject obj, jint width, jint hight)
{
#if 1
	ra = new NyARRgbRaster_BGRA(width, hight, false);
#else
	ra = new NyARRgbRaster_RGB(width, hight);
#endif
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_wrapBuffer(JNIEnv * env, jobject obj, jbyteArray i_buf)
{
	jbyte* buf;

	buf = env->GetByteArrayElements(i_buf, NULL);
	if (buf != NULL) {
		ra->wrapBuffer(buf);
		env->ReleaseByteArrayElements(i_buf, buf, JNI_ABORT);
	}
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_initNyARSingleDetectMarker(JNIEnv * env, jobject obj)
{
	ar = new NyARSingleDetectMarker(&ap, &code, 80.0, ra->getBufferType());
	ar->setContinueMode(false);
}

JNIEXPORT bool JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_detectMarkerLite(JNIEnv * env, jobject obj)
{
	return ar->detectMarkerLite(*ra, 100);
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_initNyARGLUtil(JNIEnv * env, jobject obj)
{
	nyar_gl = new NyARGLUtil();
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_toCameraFrustumRHf(JNIEnv * env, jobject obj, jfloatArray o_result)
{
	float result[16];
	int i;

	nyar_gl->toCameraFrustumRH(ap, gl_camera_frustum_rh);

	for (i = 0; i < 16; i++) {
		result[i] = (float)gl_camera_frustum_rh[i];
	}

	env->SetFloatArrayRegion(o_result, 0, 16, result);
}

JNIEXPORT void JNICALL Java_jp_androidgroup_nyartoolkit_ARToolkitDrawer_getTransmationMatrix(JNIEnv * env, jobject obj, jfloatArray o_result)
{
	float result[16];
	int i;
	NyARTransMatResult mat;

	ar->getTransmationMatrix(mat);
	nyar_gl->toCameraViewRH(mat, gl_camera_view_rh);

	for (i = 0; i < 16; i++) {
		result[i] = (float)gl_camera_view_rh[i];
	}

	env->SetFloatArrayRegion(o_result, 0, 16, result);
}

#ifdef __cplusplus
}
#endif
