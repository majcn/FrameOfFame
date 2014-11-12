package si.majcn.frameoffame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
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

        setContentView(R.layout.activity_main);

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

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mGray = inputFrame.gray();
        mRgba = inputFrame.rgba();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, FACE_DETECT_FLAGS, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            Rect face = facesArray[0];
            if (DEBUG_MODE) {
                Core.rectangle(mRgba, face.tl(), face.br(), FACE_RECT_COLOR, 3);
                mRealView.release();
            } else {
                Mat intermediateMat = mRgba.submat(face);
                Imgproc.resize(intermediateMat, mRealView, mRgba.size());
                intermediateMat.release();
                applyEffect(mRealView.getNativeObjAddr(), 0);
                return mRealView;
            }
        }

        if (mRealView.empty()) {
            return mRgba;
        } else {
            return mRealView;
        }
    }

    public native void applyEffect(long matAddrRgba, int effectNumber);
}
