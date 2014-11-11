#include <jni.h>
#include <opencv2/core/core.hpp>

#include "gimplut.cpp"

static void applyEffect(cv::Mat& rgba, int number) {
    GimpLut lut;

    gimp_lut_init(&lut, 2);
    gimp_lut_setup(&lut);
    gimp_lut_process(&lut, rgba);
}

extern "C" {
JNIEXPORT void JNICALL Java_si_majcn_frameoffame_MainActivity_applyEffect(JNIEnv*, jobject, jlong addrRgba, jint effectNumber);


JNIEXPORT void JNICALL Java_si_majcn_frameoffame_MainActivity_applyEffect(JNIEnv*, jobject, jlong addrRgba, jint effectNumber)
{
    cv::Mat& mRgb = *(cv::Mat*)addrRgba;
    applyEffect(mRgb, effectNumber);
}
}