package si.majcn.frameoffame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import org.opencv.core.Mat;

import si.majcn.frameoffame.fragment.CameraFragment;
import si.majcn.frameoffame.fragment.ImageFragment;


public class MainActivity extends FragmentActivity implements CustomContext {

    private static final String TAG = "FrameOfFame::Activity";
    private int effectNr = 0;


//    private CascadeClassifier mJavaDetector;
//    private CameraBridgeViewBase mOpenCvCameraView;
//
//    private AnimationDrawable animationDrawable;
//    private ImageView imageView;
//
//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            if (status == LoaderCallbackInterface.SUCCESS) {
//                Log.i(TAG, "OpenCV loaded successfully");
//
//                System.loadLibrary("image_processing");
//
//                mJavaDetector = OpenCVFactory.getClassifier(MainActivity.this, R.raw.haarcascade_frontalface_default);
//                mOpenCvCameraView.enableView();
//            } else {
//                super.onManagerConnected(status);
//            }
//        }
//    };

    private ImageFragment imageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);


        FragmentPagerAdapter mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0) {
                    return imageFragment = new ImageFragment();

                } else {
                    return new CameraFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        ((ViewPager) findViewById(R.id.pager)).setAdapter(mSectionsPagerAdapter);

//        animationDrawable = new AnimationDrawable();
//        for(int i=0; i<15; i++) {
//            bitArray[i] = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
//            animationDrawable.addFrame(new BitmapDrawable(getResources(), bitArray[i]), 500);
//        }
//
//        setContentView(R.layout.activity_main);
//
//
//        imageView = (ImageView) findViewById(R.id.image);
//        imageView.setImageDrawable(animationDrawable);
    }

    //
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        mGray = new Mat();
//        mRgba = new Mat();
//
//        mRealView = new Mat();
//    }

//    @Override
//    public void onCameraViewStopped() {
//        mGray.release();
//        mRgba.release();
//        mRealView.release();
//    }

//    private Rect getResizedRect(Rect orig, int maxWidth, int maxHeight) {
//        int x = Math.max(orig.x - orig.width / 4, 0);
//        int w = Math.min(orig.width + orig.width / 2, maxWidth - x);
//        int y = Math.max(orig.y - orig.height / 4, 0);
//        int h = Math.min(orig.height + orig.height / 2, maxHeight - y);
//
//        return new Rect(x, y, w, h);
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        animationDrawable.start();
//        if (index == 15) {
//            full = true;
//            Log.e("majcn", "START");
//        }
//
//        if (full && index != 0) {
//            index--;
//            return inputFrame.gray();
////            return array[15 - index--];
//        }
//        full = false;
////        animationDrawable.stop();
//
//        mGray = inputFrame.gray();
//        mRgba = inputFrame.rgba();
//
//        if (mAbsoluteFaceSize == 0) {
//            int height = mGray.height();
//            if (Math.round(height * mRelativeFaceSize) > 0) {
//                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//            }
//        }
//
//        MatOfRect faces = new MatOfRect();
//        mJavaDetector.detectMultiScale(mGray, faces, 1.05, 3, FACE_DETECT_FLAGS, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
//
//        Rect[] facesArray = faces.toArray();
//        if (facesArray.length > 0) {
//            Rect face = facesArray[0];
//            if (curFace == null || !(curFace.contains(face.tl()) && curFace.contains(face.br()))) {
//                curFace = getResizedRect(face, mRgba.width(), mRgba.height());
//            }
//
//            Mat intermediateMat = mRgba.submat(curFace);
//            Imgproc.resize(intermediateMat, mRealView, new Size(300, 300));
//            intermediateMat.release();
//            applyEffect(mRealView.getNativeObjAddr(), 0);
//            Utils.matToBitmap(mRealView, bitArray[index]);
//            array[index++] = mRealView.clone();
//        }
//
////        if (curFace == null) {
////            return mRgba;
////        } else {
////            if (DEBUG_MODE) {
////                Core.rectangle(mRgba, curFace.tl(), curFace.br(), FACE_RECT_COLOR, 3);
////                mRealView.release();
////            } else {
////                Mat intermediateMat = mRgba.submat(curFace);
////                Imgproc.resize(intermediateMat, mRealView, mRgba.size());
////                intermediateMat.release();
////                applyEffect(mRealView.getNativeObjAddr(), 0);
////                return mRealView;
////            }
////        }
//
////        if (mRealView.empty()) {
//            return mRgba;
////        } else {
////            return mRealView;
////        }
//    }

    public native void applyEffect(long matAddrRgba, int effectNumber);

    @Override
    public void fillBitmap(Mat orig) {
        applyEffect(orig.getNativeObjAddr(), effectNr);
        if (imageFragment.setNextImage(orig)) {
            effectNr = (effectNr+1) % 14;
        }
    }
}
