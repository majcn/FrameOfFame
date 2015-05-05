package si.majcn.frameoffame.fragment.image.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import si.majcn.frameoffame.R;

/**
 * Created by majcn on 2015-05-05.
 */
public class ImageGridViewAdapter extends BaseAdapter {

    private static int NUM_IMAGES = 40;
    private static int TINT_ALPHA = 50;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<Bitmap> smallImages = new ArrayList<>();

    private Context mContext;

    public ImageGridViewAdapter(Context c) {
        mContext = c;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageImageView imageGridView;
        if (convertView == null) {
            imageGridView = new ImageImageView(mContext);
        } else {
            imageGridView = (ImageImageView) convertView;
        }

        final Bitmap bitmap = images.get(0);
        imageGridView.setImageBitmap(bitmap);

        int c = smallImages.get(0).getPixel(position % NUM_IMAGES, position / NUM_IMAGES);
        int red = Color.red(c);
        int green = Color.green(c);
        int blue = Color.blue(c);

        final int colorFilter = Color.argb(TINT_ALPHA, red, green, blue);

        imageGridView.setColorFilter(colorFilter);

        imageGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageView expandedImageView = (ImageView) ((Activity) mContext).findViewById(R.id.expanded_image);
                expandedImageView.setImageBitmap(bitmap);
                expandedImageView.setColorFilter(colorFilter);

                expandedImageView.setVisibility(View.VISIBLE);
                expandedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setVisibility(View.GONE);
                    }
                });
            }
        });

        return imageGridView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return images.isEmpty() ? 0 : NUM_IMAGES * NUM_IMAGES;
    }

    public void setImage(Bitmap bitmap) {
        images.add(0, bitmap);
        smallImages.add(0, Bitmap.createScaledBitmap(bitmap, NUM_IMAGES, NUM_IMAGES, true));
    }
}
