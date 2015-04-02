package si.majcn.frameoffame.fragment.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by majcn on 2015-04-02.
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(size, size);
    }
}
