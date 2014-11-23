package si.majcn.frameoffame.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import si.majcn.frameoffame.CustomContext;
import si.majcn.frameoffame.MainActivity;
import si.majcn.frameoffame.R;
import si.majcn.frameoffame.view.ImageView15sec;

public class ImageFragment extends Fragment {

    private CustomContext mCustomContext;

    private ImageView15sec imageView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCustomContext = ((MainActivity)activity).getCustomContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        imageView = (ImageView15sec) w.findViewById(R.id.faceimg);
        imageView.setCustomContext(mCustomContext);

        return w;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.startRefreshTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        imageView.stopRefreshTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageView.stopRefreshTimer();
    }
}
