package si.majcn.frameoffame.fragment.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import si.majcn.frameoffame.R;
import si.majcn.frameoffame.fragment.image.view.ImageGridView;
import si.majcn.frameoffame.fragment.image.view.ImageImageView;

public class ImageFragment extends Fragment {

    private static final String TAG = "FoF::ImageFragment";

    private static final int NUM_IMAGES = 40;
    private static final int TINT_ALPHA = 50;

    private Context mContext;

    private ImageGridView mGridView;
    private BaseAdapter mGridViewAdapter;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private Bitmap img;
    private Bitmap smallImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        mGridView = (ImageGridView) w.findViewById(R.id.gridview);
        mGridView.setNumColumns(NUM_IMAGES);
        mGridView.setAdapter(getGridViewAdapter());
        return w;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mContext = null;
    }

    private BaseAdapter getGridViewAdapter() {
        if (mGridViewAdapter == null) {
            mGridViewAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return img == null ? 0 : NUM_IMAGES * NUM_IMAGES;
                }

                @Override
                public Object getItem(int i) {
                    return null;
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int position, View view, ViewGroup viewGroup) {
                    Log.d(TAG, "create ImageView for position: " + position);

                    if (mContext == null) {
                        Log.e(TAG, "something went terribly wrong -> context is NULL!!!");
                        return null;
                    }

                    ImageView imageView;
                    if (view == null) {
                        imageView = new ImageImageView(mContext);
                    } else {
                        imageView = (ImageView) view;
                    }

                    imageView.setImageBitmap(img);

                    int c = smallImg.getPixel(position % NUM_IMAGES, position / NUM_IMAGES);
                    int red = Color.red(c);
                    int green = Color.green(c);
                    int blue = Color.blue(c);
                    imageView.setColorFilter(Color.argb(TINT_ALPHA, red, green, blue));

                    return imageView;
                }
            };
        }

        return mGridViewAdapter;
    }

    public void addImages(Bitmap[] images) {
        if (images.length > 0) {
            img = images[0];
            smallImg = Bitmap.createScaledBitmap(img, NUM_IMAGES, NUM_IMAGES, true);

            invalidateView();
        }
    }

    public void invalidateView() {
        mGridView.invalidateViews();
    }
}
