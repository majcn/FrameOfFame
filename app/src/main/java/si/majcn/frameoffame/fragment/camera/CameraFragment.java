package si.majcn.frameoffame.fragment.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import si.majcn.frameoffame.R;
import si.majcn.frameoffame.fragment.camera.util.CameraUtil;
import si.majcn.frameoffame.fragment.camera.view.CameraPreview;

public class CameraFragment extends Fragment {

    private static final String TAG = "FrameOfFame::CameraFragment";

    private final Object lock = new Object();

    private Context mContext;

    private Camera mCamera;
    private int mCameraIndex = Camera.CameraInfo.CAMERA_FACING_BACK;

    private FrameLayout mCameraPreviewContainer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
        initCamera();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        releaseCamera();
        mContext = null;
    }

    public void takeImage(final OnImageTaken callback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    synchronized (lock) {
                        Bitmap pic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        callback.onImageTaken(pic);
                        initCamera();
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.camera_frag, container, false);

        mCameraPreviewContainer = (FrameLayout) w.findViewById(R.id.camera_preview_container);
        mCameraPreviewContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                takeImage((OnImageTaken)mContext);
                return false;
            }
        });

        Button backCameraButton = (Button) w.findViewById(R.id.camera_back);
        backCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraIndex = Camera.CameraInfo.CAMERA_FACING_BACK;
                initCamera();
            }
        });

        Button frontCameraButton = (Button) w.findViewById(R.id.camera_front);
        frontCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraIndex = Camera.CameraInfo.CAMERA_FACING_FRONT;
                initCamera();
            }
        });

        initCamera();
        return w;
    }

    private boolean isAttached() {
        return isAdded() && mCameraPreviewContainer != null;
    }

    private boolean initCamera() {
        Log.d(TAG, "initCamera()");
        releaseCamera();
        if (!isAttached()) {
            return false;
        }

        if (CameraUtil.checkCameraIndex(mCameraIndex)) {
            try {
                mCamera = Camera.open(mCameraIndex);
                mCameraPreviewContainer.addView(new CameraPreview(mContext, mCamera));
                return true;
            } catch (RuntimeException e) {
                Log.e(TAG, String.format("Camera with index %d is not available", mCameraIndex));
            }
        }
        return false;
    }

    private void releaseCamera() {
        Log.d(TAG, "releaseCamera()");
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

        if (isAttached()) {
            mCameraPreviewContainer.removeAllViews();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();

        releaseCamera();
    }
}
