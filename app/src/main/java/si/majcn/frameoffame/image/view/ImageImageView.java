package si.majcn.frameoffame.image.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * Created by majcn on 2015-04-03.
 */
public class ImageImageView extends ImageView {

    public ImageImageView(Context context) {
        super(context);

        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
