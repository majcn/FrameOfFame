package si.majcn.frameoffame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import si.majcn.frameoffame.MainActivity;

public class ImageView15sec extends ImageView {

    private static final String TAG = "View::ImageView15sec";

    private static final int DEFAULT_TIMEOUT = 15000;

//    private CustomContext mCustomContext;

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

    private static int getEffectNr() {
        return (int)Math.round(Math.random() * MainActivity.getNumberOfEffects());
    }

    private void init() {
        setRefreshTimer(DEFAULT_TIMEOUT);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                synchronized (ImageView15sec.this) {
                    if (refreshing) {
                        Bitmap image = null; //mCustomContext.getImage();
                        MainActivity.applyEffect(image, getEffectNr());
                        ImageView15sec.this.setImageBitmap(image);
                        ImageView15sec.this.invalidate();
                        Log.d(TAG, "image refreshed");
                        sendMessageDelayed(obtainMessage(MSG), refreshTimeMillis);
                    }
                }
            }
        };
    }

//    public void setCustomContext(CustomContext cc) {
//        mCustomContext = cc;
//    }

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