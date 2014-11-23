package si.majcn.frameoffame.opencv;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public interface FaceDetector {

    public Rect detectOne(Mat image);
}
