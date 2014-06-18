package si.majcn.frameoffame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity {

    static {
        System.loadLibrary("imageprocessing");
    }

    private Bitmap original;
    private ImageView mImageView;
    private FilterGLSurfaceView mImageViewGL;
    private int effectNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        original = BitmapFactory.decodeResource(getResources(), R.drawable.lena);

        effectNumber = 0;

        View.OnClickListener onClickNextEffect = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BitmapWorkerTask(mImageView).execute(effectNumber);
                mImageViewGL.setEffect(effectNumber);
                effectNumber = (effectNumber + 1) % 34;
            }
        };

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(original);
        mImageView.setOnClickListener(onClickNextEffect);

        mImageViewGL = (FilterGLSurfaceView) findViewById(R.id.imageViewGL);
        mImageViewGL.setImage(original);
        mImageViewGL.setOnClickListener(onClickNextEffect);
    }

    @Override
    protected void onPause() {
        mImageViewGL.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mImageViewGL.onResume();
        super.onResume();
    }

    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap image = original.copy(Bitmap.Config.ARGB_8888, true);
            applyEffect(image, params[0]);
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public native void applyEffect(Bitmap bmp, int effectNumber);
}
