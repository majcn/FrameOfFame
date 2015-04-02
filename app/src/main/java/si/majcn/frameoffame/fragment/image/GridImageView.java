package si.majcn.frameoffame.fragment.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by majcn on 2015-04-02.
 */
public class GridImageView extends ImageView {
    public GridImageView(Context context) {
        super(context);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
