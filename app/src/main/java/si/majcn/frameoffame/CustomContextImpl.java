package si.majcn.frameoffame;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class CustomContextImpl implements CustomContext {

    private Bitmap image;
    private int imageSize;

    public CustomContextImpl(int imageSize) {
        this.imageSize = imageSize;
        image = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void doFace(Mat orig) {
        synchronized (image) {
            Utils.matToBitmap(orig, image);
        }
    }

    @Override
    public Bitmap getImage() {
        synchronized (image) {
            return image.copy(image.getConfig(), true);
        }
    }

    @Override
    public int getImageSize() {
        return imageSize;
    }
}
