#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>

#include "color-balance.h"
#include "gimplut.h"
#include "hue-saturation.h"

#define  LOG_TAG    "libimageprocessing"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static void applyEffect(AndroidBitmapInfo* info, void* pixels, int number) {
  ColorBalance cb;
  GimpLut lut;
  HueSaturation hs;

  number %= 34;

  switch(number) {
    case 0:
      color_balance_init(&cb, 30, -32, 16);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 1:
      color_balance_init(&cb, -36, -37, 39);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 2:
      gimp_lut_init(&lut, 2);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 3:
      color_balance_init(&cb, 100, -100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 4:
      color_balance_init(&cb, -100, -100, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 5:
      color_balance_init(&cb, -100, 100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 6:
      gimp_lut_init(&lut, 5);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      hue_saturation_init(&hs, -41, -15, 6);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);
      break;
    case 7:
      gimp_lut_init(&lut, 6);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -29, 40, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);
      break;
    case 8:
      hue_saturation_init(&hs, -41, -20, 25);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 9:
      color_balance_init(&cb, 100, -100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      hue_saturation_init(&hs, -41, -10, 20);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);
      break;
    case 10:
      color_balance_init(&cb, 34, -38, 24);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -65, 0, 0);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);
      break;
    case 11:
      color_balance_init(&cb, 40, -40, 28);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -100, -42, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);
      break;
    case 12:
      color_balance_init(&cb, 45, -45, 35);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 13:
      color_balance_init(&cb, 30, -35, 20);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 14:
      color_balance_init(&cb, 30, -32, 16);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 15:
      color_balance_init(&cb, -36, -37, 39);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 16:
      gimp_lut_init(&lut, 2);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucnabarvadanatocka(2, 2);
      break;
    case 17:
      color_balance_init(&cb, 100, -100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 18:
      color_balance_init(&cb, -100, -100, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 19:
      color_balance_init(&cb, -100, 100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 20:
      gimp_lut_init(&lut, 5);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      hue_saturation_init(&hs, -41, -15, 6);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 21:
      gimp_lut_init(&lut, 6);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -29, 40, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 22:
      hue_saturation_init(&hs, -41, -20, 25);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 23:
      color_balance_init(&cb, 100, -100, -100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      hue_saturation_init(&hs, -41, -10, 20);
      hue_saturation_setup(&hs);
      hue_saturation_process(&hs, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 24:
      color_balance_init(&cb, 34, -38, 24);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -65, 0, 0);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 25:
      color_balance_init(&cb, 40, -40, 28);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      color_balance_init(&cb, -100, -42, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 26:
      color_balance_init(&cb, 45, -45, 35);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 27:
      color_balance_init(&cb, 30, -35, 20);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
    case 28:
      gimp_lut_init(&lut, 2);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucnabarvadanatocka(3, 3);
      break;
    case 29:
      color_balance_init(&cb, 30, -35, 20);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 4);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucnabarvadanatocka(3, 3);
      break;
    case 30:
      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 31:
      color_balance_init(&cb, -100, -100, 100);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucnabarvadanatocka(4, 4);
      break;
    case 32:
      color_balance_init(&cb, 30, -40, 16);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);
      break;
    case 33:
      color_balance_init(&cb, 30, -40, 16);
      color_balance_setup(&cb);
      color_balance_process(&cb, info, pixels);

      gimp_lut_init(&lut, 3);
      gimp_lut_setup(&lut);
      gimp_lut_process(&lut, info, pixels);

      // nakljucniefekt(rr, gg, bb);
      break;
  }
}


JNIEXPORT void JNICALL Java_si_majcn_imagejni_app_MainActivity_applyEffect(JNIEnv * env, jobject  obj, jobject bitmap, jint effectNumber) {
    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
            LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
            return;
        }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    applyEffect(&info, pixels, effectNumber);

    AndroidBitmap_unlockPixels(env, bitmap);
}