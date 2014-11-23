package si.majcn.frameoffame.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ImageView15sec extends ImageView {

    private static final String TAG = "View::ImageView15sec";

    private Handler mHandler;
    private static final int MSG = 1;

    private boolean refreshing = false;
    private int refreshTimeMillis = 0;

    public ImageView15sec(Context context) {
        super(context);
        init();
    }

    public ImageView15sec(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageView15sec(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setRefreshTimer(15000);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                synchronized (ImageView15sec.this) {
                    if (refreshing) {
                        ImageView15sec.this.invalidate();
                        Log.d(TAG, "image refreshed");
                        sendMessageDelayed(obtainMessage(MSG), refreshTimeMillis);
                    }
                }
            }
        };
    }

    public void setRefreshTimer(int millis) {
        refreshTimeMillis = millis;
    }

    public void startRefreshTimer() {
        if (refreshTimeMillis > 0) {
            refreshing = true;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
        }
    }

    public void stopRefreshTimer() {
        refreshing = false;
    }
}