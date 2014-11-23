package si.majcn.frameoffame;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import si.majcn.frameoffame.fragment.CameraFragment;
import si.majcn.frameoffame.fragment.ImageFragment;

public class MainActivity extends FragmentActivity {

    static {
        System.loadLibrary("imageprocessing");
    }

    public static native int getNumberOfEffects();
    public static native void applyEffect(Bitmap bmp, int i);

    private static final int IMAGE_SIZE = 500;
    private CustomContext customContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        FragmentPagerAdapter mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return i == 0 ? new ImageFragment() : new CameraFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        ((ViewPager) findViewById(R.id.pager)).setAdapter(mSectionsPagerAdapter);

        customContext = new CustomContextImpl(IMAGE_SIZE);
    }

    public CustomContext getCustomContext() {
        return customContext;
    }
}
