package si.majcn.frameoffame.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import si.majcn.frameoffame.CustomContext;
import si.majcn.frameoffame.MainActivity;
import si.majcn.frameoffame.R;

/**
 * Created by majcn on 19.11.2014.
 */
public class ImageFragment extends Fragment {

    private CustomContext cc;

    private ImageView imageView;
    private AnimationDrawable anim;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        cc = ((MainActivity)activity).getCustomContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        anim = new AnimationDrawable();
        anim.setOneShot(false);
        imageView = (ImageView) w.findViewById(R.id.image123);
        for (Bitmap bmp : cc.getBitmaps()) {
            anim.addFrame(new BitmapDrawable(getResources(), bmp), 1000);
        }
        imageView.setImageDrawable(anim);

        return w;
    }
}
