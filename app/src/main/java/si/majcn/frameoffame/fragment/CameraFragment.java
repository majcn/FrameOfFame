package si.majcn.frameoffame.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import si.majcn.frameoffame.CustomContext;
import si.majcn.frameoffame.MainActivity;
import si.majcn.frameoffame.opencv.OpenCVFactory;
import si.majcn.frameoffame.R;

/**
 * Created by majcn on 19.11.2014.
 */
public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private MainActivity context;

    private static final String TAG = "FrameOfFame::CameraFragment";

    private static final int FACE_DETECT_FLAGS = Objdetect.CASCADE_FIND_BIGGEST_OBJECT | Objdetect.CASCADE_DO_ROUGH_SEARCH;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);


    private CascadeClassifier mJavaDetector;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Mat mRgba;
    private Mat mGray;
    private Mat mRealView;

    private int mAbsoluteFaceSize = 0;
    private float mRelativeFaceSize = 0.2f;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

//                System.loadLibrary("image_processing");

                mJavaDetector = OpenCVFactory.getClassifier(context, R.raw.haarcascade_frontalface_default);
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public CameraFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        context = (MainActivity)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        context = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.camera_frag, container, false);

        mOpenCvCameraView = (CameraBridgeViewBase) w.findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setCvCameraViewListener(this);

        return w;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, context, mLoaderCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
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
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.height();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(mGray, faces, 1.05, 3, FACE_DETECT_FLAGS, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();
        Rect curFace = null;
        if (facesArray.length > 0) {
            Rect face = facesArray[0];
            curFace = getResizedRect(face, mRgba.width(), mRgba.height());

            CustomContext cc = context.getCustomContext();
            Mat intermediateMat = mRgba.submat(curFace);
            Imgproc.resize(intermediateMat, mRealView, new Size(cc.getImageSize(), cc.getImageSize()));
            intermediateMat.release();
            cc.fillBitmap(mRealView);
        }

        if (curFace != null) {
            Core.rectangle(mGray, curFace.tl(), curFace.br(), FACE_RECT_COLOR, 3);
        }
        return mGray;
    }

    private Rect getResizedRect(Rect orig, int maxWidth, int maxHeight) {
        int x = Math.max(orig.x - orig.width / 4, 0);
        int w = Math.min(orig.width + orig.width / 2, maxWidth - x);
        int y = Math.max(orig.y - orig.height / 4, 0);
        int h = Math.min(orig.height + orig.height / 2, maxHeight - y);

        return new Rect(x, y, w, h);
    }
}
