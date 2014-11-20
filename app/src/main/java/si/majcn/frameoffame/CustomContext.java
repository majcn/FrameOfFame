package si.majcn.frameoffame;

import android.graphics.Bitmap;
import android.os.Parcelable;

import org.opencv.core.Mat;

/**
 * Created by majcn on 19.11.2014.
 */
public interface CustomContext {
    void fillBitmap(Mat orig);

    Bitmap[] getBitmaps();
    int  getImageSize();
}
