package si.majcn.frameoffame;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class CustomContextImpl implements CustomContext {

    private static final int BITMAP_NR = 1;
    private Bitmap[] bitmaps = new Bitmap[BITMAP_NR];
    private int imageSize;

    public CustomContextImpl(int imageSize) {
        this.imageSize = imageSize;
        for(int i=0; i<BITMAP_NR; i++) {
            bitmaps[i] = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        }
    }

    private static int getEffectNr() {
        return (int)Math.round(Math.random() * MainActivity.getNumberOfEffects());
    }

    @Override
    public void doFace(Mat orig) {
        synchronized (this) {
            Utils.matToBitmap(orig, bitmaps[0]);
            MainActivity.applyEffect(bitmaps[0], getEffectNr());
        }
    }

    @Override
    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    @Override
    public int getImageSize() {
        return imageSize;
    }
}
