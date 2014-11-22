package si.majcn.frameoffame;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class CustomContextImpl implements CustomContext {

    private static final int BITMAP_NR = 15;
    private Bitmap[] bitmaps = new Bitmap[BITMAP_NR];
    private int imageSize;

    private int bitmapIndex = 0;

    private int effectNr = 0;

    public CustomContextImpl(int imageSize) {
        this.imageSize = imageSize;
        for(int i=0; i<BITMAP_NR; i++) {
            bitmaps[i] = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        }
    }

    @Override
    public void fillBitmap(Mat orig) {
        if (bitmapIndex < BITMAP_NR) {
            Utils.matToBitmap(orig, bitmaps[bitmapIndex]);
            MainActivity.applyEffect(bitmaps[bitmapIndex], effectNr);
            bitmapIndex++;
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
