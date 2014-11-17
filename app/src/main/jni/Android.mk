LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include $(OPENCV_HOME)/sdk/native/jni/OpenCV.mk
LOCAL_SRC_FILES  := imageprocessing.cpp
LOCAL_MODULE     := image_processing

include $(BUILD_SHARED_LIBRARY)
