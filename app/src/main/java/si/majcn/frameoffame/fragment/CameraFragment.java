package si.majcn.frameoffame.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import si.majcn.frameoffame.CustomContext;
import si.majcn.frameoffame.MainActivity;
import si.majcn.frameoffame.R;
import si.majcn.frameoffame.opencv.FaceDetector;
import si.majcn.frameoffame.opencv.FaceDetectorImpl;

public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private Context mContext;
    private CustomContext mCustomContext;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);


    private FaceDetector mJavaDetector;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Mat mRgba;
    private Mat mGray;
    private Mat mRealView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(mContext) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                mJavaDetector = new FaceDetectorImpl(mContext);
                mJavaDetector.start();
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
        mCustomContext = ((MainActivity)activity).getCustomContext();
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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, mContext, mLoaderCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        mJavaDetector.stop();
        mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mJavaDetector.stop();
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

        Rect curFace = mJavaDetector.detectOne(mGray);
        if (curFace != null) {
            Mat intermediateMat = mRgba.submat(curFace);
            Imgproc.resize(intermediateMat, mRealView, new Size(mCustomContext.getImageSize(), mCustomContext.getImageSize()));
            intermediateMat.release();
            mCustomContext.doFace(mRealView);
        }

        if (curFace != null) {
            Core.rectangle(mGray, curFace.tl(), curFace.br(), FACE_RECT_COLOR, 3);
        }
        return mGray;
    }
}
