package si.majcn.frameoffame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.mod.android.CameraBridgeViewBase;

import java.util.ResourceBundle;

/**
 * Created by majcn on 19.11.2014.
 */
public class ImageFragment extends Fragment {

    private ImageView imageView;
    private Bitmap[] bitmaps;
    private AnimationDrawable anim;
    private int i = 0;

    public ImageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        anim = new AnimationDrawable();
        anim.setOneShot(false);
        imageView = (ImageView) w.findViewById(R.id.image123);
        bitmaps = new Bitmap[15];
        for(int i=0; i<15; i++) {
            bitmaps[i] = Bitmap.createBitmap(1280, 960, Bitmap.Config.ARGB_8888);
            anim.addFrame(new BitmapDrawable(getResources(), bitmaps[i]), 1000);
        }
        imageView.setImageDrawable(anim);

        return w;
    }

    public boolean setNextImage(Mat image) {
        Utils.matToBitmap(image, bitmaps[i]);
//            anim.addFrame(new BitmapDrawable(mainActivity.getResources(), bitmaps[i]), 1000);
            i = (i + 1) % 15;
        Log.e("majcn", ""+i);
//        }
        anim.start();
        return i == 0;
    }
}
