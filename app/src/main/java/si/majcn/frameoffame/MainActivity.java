package si.majcn.frameoffame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity {

    static {
        System.loadLibrary("imageprocessing");
    }

    private Bitmap original;
    private ImageView mImageView;
    private ImageView mImageViewGL;
    private TextView mTextView;
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
                int resID = getResources().getIdentifier("lena" + effectNumber, "drawable", getPackageName());
                mImageViewGL.setImageBitmap(BitmapFactory.decodeResource(getResources(), resID));
                mTextView.setText("Efekt: " + effectNumber);
                effectNumber = (effectNumber + 1) % 14;
            }
        };

        mTextView = (TextView) findViewById(R.id.counter);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(original);
        mImageView.setOnClickListener(onClickNextEffect);

        mImageViewGL = (ImageView) findViewById(R.id.imageViewGL);
        mImageViewGL.setImageBitmap(original);
        mImageViewGL.setOnClickListener(onClickNextEffect);
    }

    @Override
    protected void onPause() {
        // mImageViewGL.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // mImageViewGL.onResume();
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
