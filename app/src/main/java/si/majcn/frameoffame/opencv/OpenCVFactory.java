package si.majcn.frameoffame.opencv;

import android.content.Context;
import android.util.Log;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class OpenCVFactory {

    private static final String TAG = "FrameOfFame::OpenCVFactory";

    public static CascadeClassifier getClassifier(Context context, int classifierResourceId) {
        CascadeClassifier javaDetector = null;

        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        try {
            // load cascade file from application resources
            InputStream is = context.getResources().openRawResource(classifierResourceId);
            File cascadeFile = new File(cascadeDir, "classifier.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            javaDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (javaDetector.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                javaDetector = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
        } finally {
            cascadeDir.delete();
        }

        return javaDetector;
    }
}
