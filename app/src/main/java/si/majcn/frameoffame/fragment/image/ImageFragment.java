package si.majcn.frameoffame.fragment.image;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import si.majcn.frameoffame.R;

public class ImageFragment extends Fragment {

    private static final String TAG = "FrameOfFame::ImageFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);
        return w;
    }
}
