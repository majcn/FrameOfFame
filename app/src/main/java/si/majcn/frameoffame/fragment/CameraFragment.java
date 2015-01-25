package si.majcn.frameoffame.fragment;

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
import si.majcn.frameoffame.util.CameraUtil;
import si.majcn.frameoffame.view.CameraPreview;

public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";

    private Context mContext;

    private Camera mCamera;
    private int mCameraIndex = -1;

    private FrameLayout mCameraPreviewContainer;

    public Bitmap pic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        View w = inflater.inflate(R.layout.camera_frag, container, false);

        mCameraPreviewContainer = (FrameLayout) w.findViewById(R.id.camera_preview_container);

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

        final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                pic = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        };

        return w;
    }

    private boolean initCamera() {
        Log.d(TAG, "initCamera()");
        releaseCamera();
        if (CameraUtil.checkCameraIndex(mCameraIndex)) {
            try {
                mCamera = Camera.open(mCameraIndex);
                mCameraPreviewContainer.addView(new CameraPreview(mContext, mCamera));
                return true;
            } catch (Exception e) {
                Log.e(TAG, String.format("Camera with index %d is not available", mCameraIndex));
            }
        }
        return false;
    }

    private void releaseCamera() {
        Log.d(TAG, "releaseCamera()");
        mCameraPreviewContainer.removeAllViews();
        if (mCamera != null) {

            mCamera.release();
            mCamera = null;
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
