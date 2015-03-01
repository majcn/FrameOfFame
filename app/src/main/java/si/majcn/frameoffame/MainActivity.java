package si.majcn.frameoffame;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import java.util.Random;

import si.majcn.frameoffame.facecropper.FaceCropper;
import si.majcn.frameoffame.fragment.camera.CameraFragment;
import si.majcn.frameoffame.fragment.camera.OnImageTaken;
import si.majcn.frameoffame.fragment.image.ImageFragment;
import si.majcn.frameoffame.util.Util;

public class MainActivity extends FragmentActivity implements OnImageTaken {

    static {
        System.loadLibrary("imageprocessing");
    }

    public static native int getNumberOfEffects();
    public static native void applyEffect(Bitmap bmp, int i);

    private CameraFragment mCameraFragment;
    private ImageFragment mImageFragment;

    private FaceCropper mCropper;
    private Random mRandomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        FragmentPagerAdapter mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return mImageFragment = new ImageFragment();
                    case 1:
                        return mCameraFragment = new CameraFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        ((ViewPager) findViewById(R.id.pager)).setAdapter(mSectionsPagerAdapter);

        mCropper = new FaceCropper();
        mRandomGenerator = new Random();
    }

    @Override
    public void onImageTaken(Bitmap image) {
        Bitmap[] result = mCropper.getCroppedImages(image);
        if (!Util.isNullOrEmpty(result)) {
            Bitmap r = result[0];
            applyEffect(r, mRandomGenerator.nextInt(getNumberOfEffects()));
            mImageFragment.setImage(r);
        }
    }
}
