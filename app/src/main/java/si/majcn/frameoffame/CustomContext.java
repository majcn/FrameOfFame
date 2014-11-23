package si.majcn.frameoffame;

import android.graphics.Bitmap;
import android.os.Parcelable;

import org.opencv.core.Mat;

public interface CustomContext {
    void doFace(Mat orig);

    Bitmap[] getBitmaps();
    int  getImageSize();
}
