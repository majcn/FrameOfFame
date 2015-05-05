package si.majcn.frameoffame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import si.majcn.frameoffame.facecropper.FaceCropper;
import si.majcn.frameoffame.fragment.image.view.ImageGridView;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("imageprocessing");
    }

    public static native int getNumberOfEffects();
    public static native void applyEffect(Bitmap bmp, int i);

    public static int TAKE_PICTURE = 1;

    private FaceCropper mCropper;

    private ImageButton mCameraButton;

    private ImageGridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton = (ImageButton) findViewById(R.id.cameraButton);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        mGridView = (ImageGridView) findViewById(R.id.gridview);
        mGridView.init(60, 100);

        mCropper = new FaceCropper();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");

            Bitmap srcBmp = mCropper.getCroppedImage(image);
            Bitmap dstBmp;

            if (srcBmp.getWidth() >= srcBmp.getHeight()) {
                dstBmp = Bitmap.createBitmap(
                        srcBmp,
                        srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                        0,
                        srcBmp.getHeight(),
                        srcBmp.getHeight()
                );

            } else {
                dstBmp = Bitmap.createBitmap(
                        srcBmp,
                        0,
                        srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                        srcBmp.getWidth(),
                        srcBmp.getWidth()
                );
            }

            Bitmap[] result = new Bitmap[getNumberOfEffects()];
            for (int i = 0; i < result.length; i++) {
                result[i] = Bitmap.createBitmap(dstBmp);
                applyEffect(result[i], i);
            }

            mGridView.setImages(result);
        }
    }
}
