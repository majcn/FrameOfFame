package si.majcn.frameoffame.fragment.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import si.majcn.frameoffame.R;
import si.majcn.frameoffame.util.Util;

public class ImageFragment extends Fragment {

    private static final String TAG = "FrameOfFame::ImageFragment";

    private ImageView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        mImage = (ImageView) w.findViewById(R.id.faceimg);
        return w;
    }

    public void addImages(Bitmap[] images) {
        if (!Util.isNullOrEmpty(images)) {
            mImage.setImageBitmap(images[0]);
        }
    }
}
