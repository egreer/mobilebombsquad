LOCAL_PATH:= $(call my-dir)

# for yuv420sp2rgb -------------------------------------------------------------

include $(CLEAR_VARS)

LOCAL_MODULE := yuv420sp2rgb

LOCAL_SRC_FILES := \
	yuv420sp2rgb/yuv420sp2rgb.c

LOCAL_CFLAGS := -DANDROID_NDK \
                -DDISABLE_IMPORTGL

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_SHARED_LIBRARY)

# for stlport ------------------------------------------------------------------

include $(CLEAR_VARS)

LOCAL_MODULE:= stlport

LOCAL_SRC_FILES:= \
          stlport/src/dll_main.cpp \
          stlport/src/fstream.cpp \
          stlport/src/strstream.cpp \
          stlport/src/sstream.cpp \
          stlport/src/ios.cpp \
          stlport/src/stdio_streambuf.cpp \
          stlport/src/istream.cpp \
          stlport/src/ostream.cpp \
          stlport/src/iostream.cpp \
          stlport/src/codecvt.cpp \
          stlport/src/collate.cpp \
          stlport/src/ctype.cpp \
          stlport/src/monetary.cpp \
          stlport/src/num_get.cpp \
          stlport/src/num_put.cpp \
          stlport/src/num_get_float.cpp \
          stlport/src/num_put_float.cpp \
          stlport/src/numpunct.cpp \
          stlport/src/time_facets.cpp \
          stlport/src/messages.cpp \
          stlport/src/locale.cpp \
          stlport/src/locale_impl.cpp \
          stlport/src/locale_catalog.cpp \
          stlport/src/facets_byname.cpp \
          stlport/src/complex.cpp \
          stlport/src/complex_io.cpp \
          stlport/src/complex_trig.cpp \
          stlport/src/string.cpp \
          stlport/src/bitset.cpp \
          stlport/src/allocators.cpp \
          stlport/src/c_locale.c \
          stlport/src/cxa.c

LOCAL_CFLAGS += -I$(LOCAL_PATH)/stlport/stlport \
		-mandroid --sysroot=build/platforms/android-5/arch-arm \
		-fuse-cxa-atexit -fvisibility=hidden \
		-D__SGI_STL_INTERNAL_PAIR_H \
		-D__SGI_STL_PORT \
		-D_GNU_SOURCE \

include $(BUILD_STATIC_LIBRARY)

# for NyARToolkitCPP -----------------------------------------------------------

include $(CLEAR_VARS)

LOCAL_MODULE := NyARToolkit

LOCAL_SRC_FILES := \
	NyARToolkitLib.cpp \
	NyARToolkitCPP/src/core/INyARCameraDistortionFactor.cpp \
	NyARToolkitCPP/src/core/INyARColorPatt.cpp \
	NyARToolkitCPP/src/core/INyARDoubleMatrix.cpp \
	NyARToolkitCPP/src/core/INyARMatchPatt.cpp \
	NyARToolkitCPP/src/core/INyARPca2d.cpp \
	NyARToolkitCPP/src/core/INyARRaster.cpp \
	NyARToolkitCPP/src/core/INyARRasterFilter_Rgb2Bin.cpp \
	NyARToolkitCPP/src/core/INyARRasterThresholdAnalyzer.cpp \
	NyARToolkitCPP/src/core/INyARRgbPixelReader.cpp \
	NyARToolkitCPP/src/core/INyARRgbRaster.cpp \
	NyARToolkitCPP/src/core/INyARRotTransOptimize.cpp \
	NyARToolkitCPP/src/core/INyARTransMat.cpp \
	NyARToolkitCPP/src/core/INyARTransportVectorSolver.cpp \
	NyARToolkitCPP/src/core/NyARBinRaster.cpp \
	NyARToolkitCPP/src/core/NyARCameraDistortionFactor.cpp \
	NyARToolkitCPP/src/core/NyARCode.cpp \
	NyARToolkitCPP/src/core/NyARColorPatt_O3.cpp \
	NyARToolkitCPP/src/core/NyARColorPatt_Perspective.cpp \
	NyARToolkitCPP/src/core/NyARColorPatt_Perspective_O2.cpp \
	NyARToolkitCPP/src/core/NyARColorPatt_PseudoAffine.cpp \
	NyARToolkitCPP/src/core/NyARContourPickup.cpp \
	NyARToolkitCPP/src/core/NyARCoord2Linear.cpp \
	NyARToolkitCPP/src/core/NyARCoord2SquareVertexIndexes.cpp \
	NyARToolkitCPP/src/core/NyARCustomSingleDetectMarker.cpp \
	NyARToolkitCPP/src/core/NyARDetectMarker.cpp \
	NyARToolkitCPP/src/core/NyARDoubleMatrix22.cpp \
	NyARToolkitCPP/src/core/NyARDoubleMatrix33.cpp \
	NyARToolkitCPP/src/core/NyARDoubleMatrix34.cpp \
	NyARToolkitCPP/src/core/NyARDoubleMatrix44.cpp \
	NyARToolkitCPP/src/core/NyARDoubleMatrixProcessor.cpp \
	NyARToolkitCPP/src/core/NyAREquationSolver.cpp \
	NyARToolkitCPP/src/core/NyARException.cpp \
	NyARToolkitCPP/src/core/NyARGrayscaleRaster.cpp \
	NyARToolkitCPP/src/core/NyARHistogram.cpp \
	NyARToolkitCPP/src/core/NyARHistogramAnalyzer_SlidePTile.cpp \
	NyARToolkitCPP/src/core/NyARHsvRaster.cpp \
	NyARToolkitCPP/src/core/NyARIntPoint2dStack.cpp \
	NyARToolkitCPP/src/core/NyARIntRectStack.cpp \
	NyARToolkitCPP/src/core/NyARLabelInfo.cpp \
	NyARToolkitCPP/src/core/NyARLabelInfoStack.cpp \
	NyARToolkitCPP/src/core/NyARLabelOverlapChecker.cpp \
	NyARToolkitCPP/src/core/NyARLabelingImage.cpp \
	NyARToolkitCPP/src/core/NyARLabelingLabel.cpp \
	NyARToolkitCPP/src/core/NyARLabelingLabelStack.cpp \
	NyARToolkitCPP/src/core/NyARLabeling_ARToolKit.cpp \
	NyARToolkitCPP/src/core/NyARLabeling_Rle.cpp \
	NyARToolkitCPP/src/core/NyARMat.cpp \
	NyARToolkitCPP/src/core/NyARMatchPattDeviationBlackWhiteData.cpp \
	NyARToolkitCPP/src/core/NyARMatchPattDeviationColorData.cpp \
	NyARToolkitCPP/src/core/NyARMatchPatt_Color_WITHOUT_PCA.cpp \
	NyARToolkitCPP/src/core/NyARMath.cpp \
	NyARToolkitCPP/src/core/NyARObserv2IdealMap.cpp \
	NyARToolkitCPP/src/core/NyARParam.cpp \
	NyARToolkitCPP/src/core/NyARPartialDifferentiationOptimize.cpp \
	NyARToolkitCPP/src/core/NyARPca2d_MatrixPCA_O2.cpp \
	NyARToolkitCPP/src/core/NyARPerspectiveParamGenerator.cpp \
	NyARToolkitCPP/src/core/NyARPerspectiveParamGenerator_O1.cpp \
	NyARToolkitCPP/src/core/NyARPerspectiveProjectionMatrix.cpp \
	NyARToolkitCPP/src/core/NyARRaster.cpp \
	NyARToolkitCPP/src/core/NyARRasterAnalyzer_Histogram.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_ARToolkitThreshold.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_CustomToneTable.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_EqualizeHist.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_GaussianSmooth.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_Reverse.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_Rgb2Hsv.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_Roberts.cpp \
	NyARToolkitCPP/src/core/NyARRasterFilter_SimpleSmooth.cpp \
	NyARToolkitCPP/src/core/NyARRasterThresholdAnalyzer_SlidePTile.cpp \
	NyARToolkitCPP/src/core/NyARRaster_BasicClass.cpp \
	NyARToolkitCPP/src/core/NyARRectOffset.cpp \
	NyARToolkitCPP/src/core/NyARRgbPixelReader_BYTE1D_B8G8R8X8_32.cpp \
	NyARToolkitCPP/src/core/NyARRgbPixelReader_BYTE1D_R8G8B8_24.cpp \
	NyARToolkitCPP/src/core/NyARRgbPixelReader_BYTE1D_X8R8G8B8_32.cpp \
	NyARToolkitCPP/src/core/NyARRgbPixelReader_INT1D_GRAY_8.cpp \
	NyARToolkitCPP/src/core/NyARRgbPixelReader_INT1D_X8R8G8B8_32.cpp \
	NyARToolkitCPP/src/core/NyARRgbRaster.cpp \
	NyARToolkitCPP/src/core/NyARRgbRaster_BGRA.cpp \
	NyARToolkitCPP/src/core/NyARRgbRaster_BasicClass.cpp \
	NyARToolkitCPP/src/core/NyARRgbRaster_RGB.cpp \
	NyARToolkitCPP/src/core/NyARRleLabelFragmentInfoStack.cpp \
	NyARToolkitCPP/src/core/NyARRotMatrix.cpp \
	NyARToolkitCPP/src/core/NyARRotMatrix_ARToolKit.cpp \
	NyARToolkitCPP/src/core/NyARRotMatrix_ARToolKit_O2.cpp \
	NyARToolkitCPP/src/core/NyARRotTransOptimize_O2.cpp \
	NyARToolkitCPP/src/core/NyARRotVector.cpp \
	NyARToolkitCPP/src/core/NyARSingleDetectMarker.cpp \
	NyARToolkitCPP/src/core/NyARSquare.cpp \
	NyARToolkitCPP/src/core/NyARSquareContourDetector.cpp \
	NyARToolkitCPP/src/core/NyARSquareContourDetector_ARToolKit.cpp \
	NyARToolkitCPP/src/core/NyARSquareContourDetector_Rle.cpp \
	NyARToolkitCPP/src/core/NyARSquareStack.cpp \
	NyARToolkitCPP/src/core/NyARSystemofLinearEquationProcessor.cpp \
	NyARToolkitCPP/src/core/NyARTransMat.cpp \
	NyARToolkitCPP/src/core/NyARTransMatResult.cpp \
	NyARToolkitCPP/src/core/NyARTransportVectorSolver.cpp \
	NyARToolkitCPP/src/core/NyARVec.cpp \
	NyARToolkitCPP/src/core/TNyARMatchPattResult.cpp \
	NyARToolkitCPP/src/utils/NyStdLib.cpp \
	NyARToolkitCPP/src.utils/opengl/NyARGLUtil.cpp

LOCAL_CFLAGS := -DANDROID_NDK \
                -DDISABLE_IMPORTGL

LOCAL_CFLAGS += -I$(LOCAL_PATH)/stlport/stlport \
		-I$(LOCAL_PATH)/NyARToolkitCPP/inc \
		-I$(LOCAL_PATH)/NyARToolkitCPP/inc/core \
		-I$(LOCAL_PATH)/NyARToolkitCPP/inc/utils \
		-I$(LOCAL_PATH)/NyARToolkitCPP/src.utils/opengl \
		-mandroid --sysroot=build/platforms/android-5/arch-arm \
		-D__SGI_STL_INTERNAL_PAIR_H \
		-D__SGI_STL_PORT

LOCAL_LDLIBS := -ldl -llog

LOCAL_STATIC_LIBRARIES += \
    libstlport

include $(BUILD_SHARED_LIBRARY)
