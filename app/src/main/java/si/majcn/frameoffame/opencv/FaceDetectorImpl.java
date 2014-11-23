package si.majcn.frameoffame.opencv;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import si.majcn.frameoffame.R;

public class FaceDetectorImpl implements FaceDetector {

    private static final String TAG = "OpenCV::FaceDetector";

    private static final int FACE_DETECT_FLAGS_ONE = Objdetect.CASCADE_FIND_BIGGEST_OBJECT | Objdetect.CASCADE_DO_ROUGH_SEARCH;
    private static final double SCALE_FACTOR = 1.05;
    private static final int MIN_NEIGHBORS = 3;

    private float mRelativeFaceSizeMin = 0.3f;
    private Size mFaceSizeMin;

    private float mRelativeFaceSizeMax = 0.5f;
    private Size mFaceSizeMax;

    private CascadeClassifier mFaceDetector;
    private CascadeClassifier mEyeDetector;

    private MatOfRect faces;
    private MatOfRect eyes;
    private Mat resizedFaceMat;

    public FaceDetectorImpl(Context context) {
        mFaceDetector = OpenCVFactory.getClassifier(context, R.raw.haarcascade_frontalface_default);
        mEyeDetector = OpenCVFactory.getClassifier(context, R.raw.haarcascade_eye);

        faces = new MatOfRect();
        eyes = new MatOfRect();
        resizedFaceMat = new Mat();
    }

    @Override
    public Rect detectOne(Mat image) {
        if (mFaceSizeMin == null) {
            int min = Math.round(image.height() * mRelativeFaceSizeMin);
            mFaceSizeMin = new Size(min, min);
        }

        if (mFaceSizeMax == null) {
            int max = Math.round(image.height() * mRelativeFaceSizeMax);
            mFaceSizeMax = new Size(max, max);
        }

        faces.release();
        mFaceDetector.detectMultiScale(image, faces, SCALE_FACTOR, MIN_NEIGHBORS, FACE_DETECT_FLAGS_ONE, mFaceSizeMin, mFaceSizeMax);

        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            Rect curFace = getResizedRect(facesArray[0], image.width(), image.height());
            Mat intermediateMat = image.submat(curFace);
            Imgproc.resize(intermediateMat, resizedFaceMat, new Size(300, 300));
            intermediateMat.release();

            if (detectTwoEyes(resizedFaceMat)) {
                return curFace;
            }
        }

        return null;
    }

    private boolean detectTwoEyes(Mat faceMat) {
        int maxSize = Math.round(faceMat.height() * 0.3f);

        eyes.release();
        mEyeDetector.detectMultiScale(faceMat, eyes, SCALE_FACTOR, MIN_NEIGHBORS, Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(maxSize, maxSize));
        return eyes.toArray().length == 2;
    }

    private Rect getResizedRect(Rect orig, int maxWidth, int maxHeight) {
        int x = Math.max(orig.x - orig.width / 4, 0);
        int w = Math.min(orig.width + orig.width / 2, maxWidth - x);
        int y = Math.max(orig.y - orig.height / 4, 0);
        int h = Math.min(orig.height + orig.height / 2, maxHeight - y);

        return new Rect(x, y, w, h);
    }
}
