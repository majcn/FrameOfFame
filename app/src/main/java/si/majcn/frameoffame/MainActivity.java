package si.majcn.frameoffame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.mod.android.CameraBridgeViewBase;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "FrameOfFame::Activity";

    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private static final int FACE_DETECT_FLAGS = Objdetect.CASCADE_FIND_BIGGEST_OBJECT | Objdetect.CASCADE_DO_ROUGH_SEARCH;

    private CascadeClassifier mJavaDetector;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Mat mRgba;
    private Mat mGray;
    private Mat mRealView;

    private int mAbsoluteFaceSize = 0;
    private float mRelativeFaceSize = 0.2f;

    private boolean DEBUG_MODE = false;
    private int cameraId = 0;

    private Rect curFace = null;

    private AnimationDrawable animationDrawable;
    private ImageView imageView;

    private Mat[] array = new Mat[15];
    private Bitmap[] bitArray = new Bitmap[15];
    private int index = 0;
    private boolean full = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

                System.loadLibrary("image_processing");

                mJavaDetector = OpenCVFactory.getClassifier(MainActivity.this, R.raw.haarcascade_frontalface_default);
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        animationDrawable = new AnimationDrawable();
        for(int i=0; i<15; i++) {
            bitArray[i] = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            animationDrawable.addFrame(new BitmapDrawable(getResources(), bitArray[i]), 2000);
        }

        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(animationDrawable);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(cameraId);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DEBUG_MODE = !DEBUG_MODE;
            }
        });
        mOpenCvCameraView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cameraId = (cameraId + 1) % 2;
                mOpenCvCameraView.setCameraIndex(cameraId);
                mOpenCvCameraView.disableView();
                mOpenCvCameraView.enableView();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOpenCvCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();

        mRealView = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        mRealView.release();
    }

    private Rect getResizedRect(Rect orig, int maxWidth, int maxHeight) {
        int x = Math.max(orig.x - orig.width / 4, 0);
        int w = Math.min(orig.width + orig.width / 2, maxWidth - x);
        int y = Math.max(orig.y - orig.height / 4, 0);
        int h = Math.min(orig.height + orig.height / 2, maxHeight - y);

        return new Rect(x, y, w, h);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (index == 15) {
            full = true;
            animationDrawable.start();
        }

        if (full && index != 0) {

            return array[15 - index--];
        }
        full = false;

        mGray = inputFrame.gray();
        mRgba = inputFrame.rgba();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.height();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(mGray, faces, 1.05, 3, FACE_DETECT_FLAGS, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            Rect face = facesArray[0];
            if (curFace == null || !(curFace.contains(face.tl()) && curFace.contains(face.br()))) {
                curFace = getResizedRect(face, mRgba.width(), mRgba.height());
            }

            Mat intermediateMat = mRgba.submat(curFace);
            Imgproc.resize(intermediateMat, mRealView, mRgba.size());
            intermediateMat.release();
            applyEffect(mRealView.getNativeObjAddr(), 0);
            Utils.matToBitmap(mRealView, bitArray[index]);
            array[index++] = mRealView.clone();
        }

//        if (curFace == null) {
//            return mRgba;
//        } else {
//            if (DEBUG_MODE) {
//                Core.rectangle(mRgba, curFace.tl(), curFace.br(), FACE_RECT_COLOR, 3);
//                mRealView.release();
//            } else {
//                Mat intermediateMat = mRgba.submat(curFace);
//                Imgproc.resize(intermediateMat, mRealView, mRgba.size());
//                intermediateMat.release();
//                applyEffect(mRealView.getNativeObjAddr(), 0);
//                return mRealView;
//            }
//        }

        if (mRealView.empty()) {
            return mRgba;
        } else {
            return mRealView;
        }
    }

    public native void applyEffect(long matAddrRgba, int effectNumber);
}
