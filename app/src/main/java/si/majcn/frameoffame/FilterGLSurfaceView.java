package si.majcn.frameoffame;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import si.majcn.frameoffame.utils.GLToolbox;
import si.majcn.frameoffame.utils.RawResourceReader;

public class FilterGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private int[] mTextures;

    private FloatBuffer mTexVertices;
    private FloatBuffer mPosVertices;

    private boolean mIsCreated;

    private Bitmap mImage;
    private int mEffectNumber;
    private Context mContext;

    private int mProgram;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;
    private int mPosCoordHandle;
    private int mFilterIndexHandle;

    public FilterGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public FilterGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mTextures = new int[]{0};
        mIsCreated = false;

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setImage(Bitmap image) {
        mImage = image;
        if (mIsCreated) {
            initTexture();
        }
    }

    public void setEffect(int i) {
        mEffectNumber = i;
        requestRender();
    }

    private void initTexture() {
        Log.d("majcn", "initTexture");
        GLES20.glGenTextures(1, mTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mImage, 0);
        GLToolbox.initTexParams();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d("majcn", "onSurfaceCreated");
        if(mImage != null) {
            initTexture();
        }
        mIsCreated = true;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        final float[] TEX_VERTICES = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        final float[] POS_VERTICES = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        mTexVertices = ByteBuffer.allocateDirect(TEX_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexVertices.put(TEX_VERTICES).position(0);
        mPosVertices = ByteBuffer.allocateDirect(POS_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosVertices.put(POS_VERTICES).position(0);

        mProgram = GLToolbox.createProgram(RawResourceReader.readTextFileFromRawResource(mContext, R.raw.vertex_shader), RawResourceReader.readTextFileFromRawResource(mContext, R.raw.fragment_shader));
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "tex_sampler");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texcoord");
        mPosCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_position");
        mFilterIndexHandle = GLES20.glGetUniformLocation(mProgram, "filter_index");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d("majcn", "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
        GLToolbox.checkGlError("glViewport");

        if (mPosVertices != null) {
            float imgAspectRatio = mImage.getWidth() / (float) mImage.getHeight();
            float viewAspectRatio = width / (float)height;
            float relativeAspectRatio = viewAspectRatio / imgAspectRatio;
            float x0, y0, x1, y1;
            if (relativeAspectRatio > 1.0f) {
                x0 = -1.0f / relativeAspectRatio;
                y0 = -1.0f;
                x1 = 1.0f / relativeAspectRatio;
                y1 = 1.0f;
            } else {
                x0 = -1.0f;
                y0 = -relativeAspectRatio;
                x1 = 1.0f;
                y1 = relativeAspectRatio;
            }
            float[] coords = new float[] { x0, y0, x1, y0, x0, y1, x1, y1 };
            mPosVertices.put(coords).position(0);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.d("majcn", "onDrawFrame");
        if(mTextures[0] == 0) {
            Log.e("majcn", "No texture, no draw!");
            return;
        }

        // Use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        GLES20.glUniform1i(mFilterIndexHandle, mEffectNumber);

        // Draw
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
