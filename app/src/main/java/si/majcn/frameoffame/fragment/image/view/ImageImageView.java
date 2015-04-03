package si.majcn.frameoffame.fragment.image.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by majcn on 2015-04-03.
 */
public class ImageImageView extends ImageView {

    public ImageImageView(Context context) {
        super(context);
    }

    public ImageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
