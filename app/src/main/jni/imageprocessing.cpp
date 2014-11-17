#include "imageprocessing.hpp"

#include <opencv2/core/core.hpp>

#include "gimp/rgba.h"
#include "gimp/gimpcolorspace.c"
#include "gimp/color-balance.c"
#include "gimp/gimplut.c"
#include "gimp/hue-saturation.c"

static void apply_color_balance(ColorBalance *cb, cv::Mat& rgbaMat) {
    for(int y = 0; y < rgbaMat.rows; y++) {
        for(int x = 0; x < rgbaMat.cols; x++) {
            RGBA& cur = rgbaMat.ptr<RGBA>(y)[x];
            color_balance_process(cb, &cur);
        }
    }
}

static void apply_lut(GimpLut *lut, cv::Mat& rgbaMat) {
    for(int y = 0; y < rgbaMat.rows; y++) {
        for(int x = 0; x < rgbaMat.cols; x++) {
            RGBA& cur = rgbaMat.ptr<RGBA>(y)[x];
            gimp_lut_process(lut, &cur);
        }
    }
}

static void apply_hue_saturation(HueSaturation *hs, cv::Mat& rgbaMat) {
    for(int y = 0; y < rgbaMat.rows; y++) {
        for(int x = 0; x < rgbaMat.cols; x++) {
            RGBA& cur = rgbaMat.ptr<RGBA>(y)[x];
            hue_saturation_process(hs, &cur);
        }
    }
}

JNIEXPORT void JNICALL Java_si_majcn_frameoffame_MainActivity_applyEffect(JNIEnv*, jobject, jlong addrRgba, jint effectNumber) {
    cv::Mat& mRgba = *(cv::Mat*)addrRgba;

    if (effectNumber < 0 || effectNumber > 13) {
        return;
    }

    ColorBalance cb;
    GimpLut lut;
    HueSaturation hs;

    switch (effectNumber) {
    case 0:
        color_balance_init(&cb, 30, -32, 16);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 1:
        color_balance_init(&cb, -36, -37, 39);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 2:
        gimp_lut_init(&lut, 2);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 3:
        color_balance_init(&cb, 100, -100, -100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 3);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 4:
        color_balance_init(&cb, -100, -100, 100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 3);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 5:
        color_balance_init(&cb, -100, 100, -100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 3);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 6:
        gimp_lut_init(&lut, 5);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);

        hue_saturation_init(&hs, -41, -15, 6);
        hue_saturation_setup(&hs);
        apply_hue_saturation(&hs, mRgba);
        break;
    case 7:
        gimp_lut_init(&lut, 6);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);

        color_balance_init(&cb, -29, 40, 100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);
        break;
    case 8:
        hue_saturation_init(&hs, -41, -20, 25);
        hue_saturation_setup(&hs);
        apply_hue_saturation(&hs, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 9:
        color_balance_init(&cb, 100, -100, -100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 3);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);

        hue_saturation_init(&hs, -41, -10, 20);
        hue_saturation_setup(&hs);
        apply_hue_saturation(&hs, mRgba);
        break;
    case 10:
        color_balance_init(&cb, 34, -38, 24);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);

        color_balance_init(&cb, -65, 0, 0);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);
        break;
    case 11:
        color_balance_init(&cb, 40, -40, 28);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);

        color_balance_init(&cb, -100, -42, 100);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);
        break;
    case 12:
        color_balance_init(&cb, 45, -45, 35);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    case 13:
        color_balance_init(&cb, 30, -35, 20);
        color_balance_setup(&cb);
        apply_color_balance(&cb, mRgba);

        gimp_lut_init(&lut, 4);
        gimp_lut_setup(&lut);
        apply_lut(&lut, mRgba);
        break;
    }
}
