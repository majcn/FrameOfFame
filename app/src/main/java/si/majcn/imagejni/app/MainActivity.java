package si.majcn.imagejni.app;

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

    private ImageView imageView;

    private int effectNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        original = BitmapFactory.decodeResource(getResources(), R.drawable.lena);

        effectNumber = 0;
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(original);

        imageView.setOnClickListener((view) -> {
            new BitmapWorkerTask(imageView).execute(effectNumber++);
        });
    }

    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
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
