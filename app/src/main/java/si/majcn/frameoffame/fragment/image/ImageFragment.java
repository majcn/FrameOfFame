package si.majcn.frameoffame.fragment.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import si.majcn.frameoffame.R;
import si.majcn.frameoffame.fragment.image.view.ImageImageView;

public class ImageFragment extends Fragment {

    private static final String TAG = "FoF::ImageFragment";

    private static final int NUM_IMAGES = 40;

//    private ImageView mImage
GridView gridview;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private Bitmap img;
    private Bitmap smallImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.image_frag, container, false);

        gridview = (GridView) w.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        gridview.setNumColumns(NUM_IMAGES);
//        mImage = (ImageView) w.findViewById(R.id.faceimg);
        return w;
    }

    public void addImages(Bitmap[] images) {
        if (images.length > 0) {
            img = images[0];
            smallImg = Bitmap.createScaledBitmap(img, NUM_IMAGES, NUM_IMAGES, true);

            gridview.invalidateViews();
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return img != null ? NUM_IMAGES * NUM_IMAGES : 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            int c = smallImg.getPixel(position % NUM_IMAGES, position / NUM_IMAGES);

            int red = Color.red(c);
            int green = Color.green(c);
            int blue = Color.blue(c);

            imageView.setImageBitmap(img);
            imageView.setColorFilter(Color.argb(50, red, green, blue));
            return imageView;
        }
    }
}
