package si.majcn.frameoffame.util;

import android.hardware.Camera;

/**
 * Created by majcn on 2015-01-25.
 */
public final class CameraUtil {

    public static boolean checkCameraIndex(final int index) {
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (index == info.facing) {
                return true;
            }
        }
        return false;
    }
}
