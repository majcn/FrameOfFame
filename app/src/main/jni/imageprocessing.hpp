#ifndef __IMAGEPROCESSING_H__
#define __IMAGEPROCESSING_H__

#include <jni.h>

extern "C" {
    JNIEXPORT void JNICALL Java_si_majcn_frameoffame_MainActivity_applyEffect(JNIEnv*, jobject, jlong addrRgba, jint effectNumber);
}

#endif /* __IMAGEPROCESSING_H__ */