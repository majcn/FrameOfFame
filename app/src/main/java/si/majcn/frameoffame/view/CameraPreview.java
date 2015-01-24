package si.majcn.frameoffame.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by majcn on 2015-01-24.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private boolean isPreviewOn;

    public CameraPreview(Context context) {
        super(context);
        isPreviewOn = false;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private void stopPreview() {
        if (isPreviewOn) {
            try {
                mCamera.stopPreview();
                isPreviewOn = false;
            } catch (Exception e) {
                Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
            }
        }
    }

    private void startPreview() {
        if (!isPreviewOn) {
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                isPreviewOn = true;
            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        stopPreview();

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(w, h);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);

        startPreview();
    }
}