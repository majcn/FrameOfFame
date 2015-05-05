package si.majcn.frameoffame.fragment.image.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by majcn on 2015-04-03.
 */
public class ImageGridView extends GridView {

    private ImageGridViewAdapter mImageGridAdapter;

    public ImageGridView(Context context) {
        super(context);
    }

    public ImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(size, size);
    }

    public void init(int numColumns) {
        mImageGridAdapter = new ImageGridViewAdapter(getContext(), numColumns, 50);

        setNumColumns(numColumns);
        setAdapter(mImageGridAdapter);
    }

    public void setImages(Bitmap... bitmaps) {
        if (bitmaps.length > 0) {
            mImageGridAdapter.setImages(bitmaps);
            invalidateViews();
        }
    }

    public void clearImages() {
        mImageGridAdapter.setImages();
    }
}
