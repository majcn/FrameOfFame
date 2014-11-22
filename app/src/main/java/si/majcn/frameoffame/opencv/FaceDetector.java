package si.majcn.frameoffame.opencv;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public interface FaceDetector {

    public void start();
    public void stop();

    public Rect detectOne(Mat image);
}
