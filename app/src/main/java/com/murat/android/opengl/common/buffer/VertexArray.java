package com.murat.android.opengl.common.buffer;

import android.opengl.GLES20;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.murat.android.opengl.common.data.Constants.Bytes_Per_Float;

public class VertexArray {

    private final FloatBuffer mFloatBuffer;

    public VertexArray(float[] vertices) {
        mFloatBuffer = ByteBuffer
                .allocateDirect(vertices.length * Bytes_Per_Float)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        mFloatBuffer.position(0);
    }

    public void setVertexAttributePointer(final VertexAttributeArray layout) {
        ArrayList<VertexAttributeArray.VertexAttribute> elements = layout.getElements();
        int offset = 0;
        for (VertexAttributeArray.VertexAttribute element : elements) {
            mFloatBuffer.position(offset);
            setVertexAttributePointer(element.location, element.dimension, element.type, element.normalized, layout.getStride(), mFloatBuffer);
            offset += element.dimension;
        }
        mFloatBuffer.position(0);
    }

    private void setVertexAttributePointer(int location, int count, int type, boolean normalized, int stride, Buffer buffer) {
        GLES20.glVertexAttribPointer(location, count, type, normalized, stride, buffer);
        GLES20.glEnableVertexAttribArray(location);
    }

    public void updateVertex(float[] vertex, int offset, int length) {
        mFloatBuffer.position(offset);
        mFloatBuffer.put(vertex, offset, length);
        mFloatBuffer.position(0);
    }

}
