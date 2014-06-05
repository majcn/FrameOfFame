package si.majcn.frameoffame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import utils.GLToolbox;

public class FilterGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private int[] mTextures;

    private FloatBuffer mTexVertices;
    private FloatBuffer mPosVertices;

    private boolean mInitialized = false;

    private int mProgram;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;
    private int mPosCoordHandle;

    public FilterGLSurfaceView(Context context) {
        super(context);
    }

    public FilterGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Bitmap image) {
        if(!mInitialized) {
            mTextures = new int[2];

            setEGLContextClientVersion(2);
            setRenderer(this);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

            mProgram = GLToolbox.createProgram("", "");
            mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "tex_sampler");
            mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texcoord");
            mPosCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_position");

            final float[] TEX_VERTICES = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            final float[] POS_VERTICES = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
            mTexVertices = ByteBuffer.allocateDirect(TEX_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mTexVertices.put(TEX_VERTICES).position(0);
            mPosVertices = ByteBuffer.allocateDirect(POS_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mPosVertices.put(POS_VERTICES).position(0);

            loadTexture(image);

            mInitialized = true;
        }
    }

    private void loadTexture(Bitmap image) {
        GLES20.glGenTextures(2, mTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        GLToolbox.initTexParams();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    }
}
