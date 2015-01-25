package si.majcn.frameoffame.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import si.majcn.frameoffame.R;
import si.majcn.frameoffame.view.CameraPreview;

public class CameraFragment extends Fragment {

    // private static final int CAMERA_ID_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    // private static final int CAMERA_ID_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private CameraPreview mCameraPreview;
    public Bitmap pic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.camera_frag, container, false);

//        mCameraPreview = (CameraPreview) w.findViewById(R.id.camera_preview);

        final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                pic = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        };

//        w.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mCameraPreview.takePicture(pictureCallback);
//                return false;
//            }
//        });

        return w;
    }
}
