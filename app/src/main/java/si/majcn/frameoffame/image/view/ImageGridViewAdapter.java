package si.majcn.frameoffame.image.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import si.majcn.frameoffame.R;

/**
 * Created by majcn on 2015-05-05.
 */
class ImageGridViewAdapter extends BaseAdapter {

    private Random mRandomGenerator = new Random();

    private final int NUM_IMAGES;
    private final int TINT_ALPHA;

    private ArrayList<Bitmap> images = new ArrayList<>();
    private Bitmap smallImage;

    private Context mContext;

    public ImageGridViewAdapter(Context c, int numImages, int tintAlpha) {
        mContext = c;

        NUM_IMAGES = numImages;
        TINT_ALPHA = tintAlpha;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageImageView imageGridView;
        if (convertView == null) {
            imageGridView = new ImageImageView(mContext);
        } else {
            imageGridView = (ImageImageView) convertView;
        }

        int imageIndex = mRandomGenerator.nextInt(images.size());

        final Bitmap bitmap = images.get(imageIndex);
        imageGridView.setImageBitmap(bitmap);

        int c = smallImage.getPixel(position % NUM_IMAGES, position / NUM_IMAGES);
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

    public void setImages(Bitmap... bitmaps) {
        images.clear();

        for (int i = 0; i < bitmaps.length; i++) {
            Bitmap bmp = bitmaps[i];
            images.add(bmp);
        }

        smallImage = Bitmap.createScaledBitmap(bitmaps[mRandomGenerator.nextInt(bitmaps.length)], NUM_IMAGES, NUM_IMAGES, true);
    }
}
