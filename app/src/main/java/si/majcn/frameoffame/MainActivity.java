package si.majcn.frameoffame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;


public class MainActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{

    static {
        System.loadLibrary("imageprocessing");
    }

    private Bitmap original;
    private ImageView mImageView;
    private ImageView mImageViewGL;
    private TextView mTextView;
    private int effectNumber;

    private Camera mCamera;
    private SurfaceView mView;

    private Camera.Face foundFace;
    private boolean faceLock = false;

    private Camera.FaceDetectionListener faceDetectionListener = new Camera.FaceDetectionListener() {
        @Override
        public void onFaceDetection(final Camera.Face[] faces, Camera camera) {
            Log.w("onFaceDetection", "Number of Faces:" + faces.length);

            if (faces.length > 0) {
                foundFace  = faces[0];
            } else {
                foundFace = null;
            }

//                mCamera.takePicture(null, null, new Camera.PictureCallback() {
//                    @Override
//                    public void onPictureTaken(byte[] bytes, Camera camera) {
//                        original = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        Rect r = faces[0].rect;
//                        Log.e("majcn", String.format("%d %d", original.getWidth(), original.getHeight()));
//                        Log.d("majcn", String.format("left: %d, right: %d, bottom %d, top %d, height %d, width %d", r.left, r.right, r.bottom, r.top, r.height(), r.width()));
//                        original = Bitmap.createBitmap(original, r.left, r.top, original.getWidth()-r.left, original.getHeight()-r.top);
//                        mImageView.setImageBitmap(original);
//
//                        Bitmap image = original.copy(Bitmap.Config.ARGB_8888, true);
//                        applyEffect(image, 10);
//                        mImageViewGL.setImageBitmap(image);
//                    }
//                });
//            }

//            mCamera.stopFaceDetection();
            // Update the view now!
//            mFaceView.setFaces(faces);
        }
    };

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

        mView = new SurfaceView(this);
        addContentView(mView, new ViewGroup.LayoutParams(1, 1));

        mTextView = (TextView) findViewById(R.id.counter);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(original);
        mImageView.setOnClickListener(onClickNextEffect);

        mImageViewGL = (ImageView) findViewById(R.id.imageViewGL);
        mImageViewGL.setImageBitmap(original);
        mImageViewGL.setOnClickListener(onClickNextEffect);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("majcn", "surfaceCreated");
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            Log.e("majcn", "WTF", e);
        }
        mCamera.setFaceDetectionListener(faceDetectionListener);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        // And set them:
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        int mDisplayRotation = getDisplayRotation(this);
        int mDisplayOrientation = getDisplayOrientation(mDisplayRotation, 0);
        mCamera.setDisplayOrientation(mDisplayOrientation);

        Log.d("majcn", "surfaceChanged");
        mCamera.startPreview();
        mCamera.startFaceDetection();
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }

    public static int getDisplayOrientation(int degrees, int cameraId) {
        // See android.hardware.Camera.setDisplayOrientation for
        // documentation.
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.release();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (foundFace != null) {
            Log.w("majcn", "Found face");
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            YuvImage yuvImage = new YuvImage(bytes, parameters.getPreviewFormat(), size.width, size.height, null);

            Matrix matrix = new Matrix();
            matrix.postScale(size.width / 2000f, size.height / 2000f);
            matrix.postTranslate(size.width / 2f, size.height / 2f);

            Log.w("majcn", foundFace.rect.left + "");
            Log.w("majcn", foundFace.rect.top + "");
            int x = (foundFace.rect.left + 1000) * size.width / 2000;
            int y = (foundFace.rect.top + 1000) * size.height / 2000;

            x = 0;
            y = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(x, y, size.width-x, size.height-y), 50, out);
            byte[] imageBytes = out.toByteArray();
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            applyEffect(image, 12);
            mImageView.setImageBitmap(image);
        }
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
