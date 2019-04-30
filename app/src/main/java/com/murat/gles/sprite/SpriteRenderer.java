package com.murat.gles.sprite;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.murat.gles.actions.Action;
import com.murat.gles.actions.ActionInterval;
import com.murat.gles.common.data.Constants;
import com.murat.gles.common.GLRenderer;
import com.murat.gles.common.data.Vertices;
import com.murat.gles.common.texture.Texture;
import com.murat.gles.common.buffer.VertexArray;
import com.murat.gles.common.buffer.VertexBuffer;
import com.murat.gles.common.buffer.VertexBufferLayout;


public class SpriteRenderer implements GLRenderer.GLRenderable, Action {

    private VertexArray mVertexArray;
    private VertexBuffer mVertexBuffer;
    private VertexBufferLayout mVertexBufferLayout;
    private Texture mTexture;
    private SpriteShader mRectShader;
    private ActionInterval mActionInterval;
    private int mResourceId;

    private float[] mVertices;

    public SpriteRenderer(int resourceId) {
        mActionInterval = new ActionInterval(this);
        mResourceId = resourceId;
        mVertices = Vertices.Position4f_TexCoord2f;
    }

    @Override
    public GLRenderer.GLRenderable init(Context context) {
        mRectShader = new SpriteShader(context);
        mTexture = new Texture(context, mResourceId);
        mVertexBufferLayout = new VertexBufferLayout();
        mVertexArray = new VertexArray(mVertices);
        mVertexBufferLayout.push(0, 4, GLES20.GL_FLOAT, Constants.BYTES_PER_FLOAT, false);
        mVertexBufferLayout.push(1, 2, GLES20.GL_FLOAT, Constants.BYTES_PER_FLOAT, false);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mModelViewMatrix, 0);
        Matrix.setIdentityM(mModelViewProjectionMatrix, 0);
        mColor[0] = 1.0f;
        mColor[1] = 1.0f;
        mColor[2] = 1.0f;
        mColor[3] = 1.0f;
        return this;
    }

    @Override
    public GLRenderer.GLRenderable bind() {
        mRectShader.bind();
        mTexture.bind();
        mVertexArray.setVertexAttributePointer(mVertexBufferLayout);
        return this;
    }

    @Override
    public GLRenderer.GLRenderable unbind() {
        mRectShader.unbind();
        mTexture.unbind();
        return this;
    }

    private final float[] mColor = new float[4];
    private final float[] mModelMatrix = new float[16];
    private final float[] mModelViewMatrix = new float[16];
    private final float[] mModelViewProjectionMatrix = new float[16];

    @Override
    public GLRenderer.GLRenderable render(float[] projectionMatrix, float[] viewMatrix) {
        Matrix.multiplyMM(mModelViewMatrix, 0, viewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, projectionMatrix, 0, mModelViewMatrix, 0);
        mRectShader.setUniformMatrix4fv(mRectShader.getMVPMatrixLocation(), mModelViewProjectionMatrix);

        if (mColor[0] > 1.0f) mColor[0] = 1.0f;
        if (mColor[0] < 0.0f) mColor[0] = 0.0f;
        if (mColor[1] > 1.0f) mColor[1] = 1.0f;
        if (mColor[1] < 0.0f) mColor[1] = 0.0f;
        if (mColor[2] > 1.0f) mColor[2] = 1.0f;
        if (mColor[2] < 0.0f) mColor[2] = 0.0f;
        if (mColor[3] > 1.0f) mColor[3] = 1.0f;
        if (mColor[3] < 0.0f) mColor[3] = 0.0f;
        mRectShader.setUniform4f(mRectShader.getColorLocation(), mColor);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDisable(GLES20.GL_BLEND);
        return this;
    }

    @Override
    public Action translate(float x, float y, float z) {
        float[] translation = new float[16];
        Matrix.setIdentityM(translation, 0);
        Matrix.translateM(translation, 0, translation, 0, x, y, z);

        Matrix.multiplyMM(mModelMatrix, 0, translation, 0, mModelMatrix, 0);
        return this;
    }

    @Override
    public Action rotate(float a, float x, float y, float z) {
        float[] rotation = new float[16];
        Matrix.setIdentityM(rotation, 0);
        Matrix.rotateM(rotation, 0, rotation, 0, a, x, y, z);
        Matrix.multiplyMM(mModelMatrix, 0, rotation, 0, mModelMatrix, 0);
        return this;
    }

    @Override
    public Action scale(float x, float y, float z) {
        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, scale, 0, x + 1f, y + 1f, z + 1f);
        Matrix.multiplyMM(mModelMatrix, 0, scale, 0, mModelMatrix, 0);
        return this;
    }

    @Override
    public Action fade(float a) {
        mColor[3] += a;
        return this;
    }

    @Override
    public Action tint(float r, float g, float b) {
        mColor[0] += r;
        mColor[1] += g;
        mColor[2] += b;
        return this;
    }

    public ActionInterval getActionInterval() {
        return mActionInterval;
    }

}