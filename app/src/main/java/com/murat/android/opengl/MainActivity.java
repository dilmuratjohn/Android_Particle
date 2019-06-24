package com.murat.android.opengl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.murat.android.opengl.common.data.TestData;
import com.murat.android.opengl.node.Numbers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup mGLFrame;
    GLView mGLView;
    Numbers mNumbers;
    Button mSmall, mBig, mUp, mDown, mRight, mLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        mGLFrame = findViewById(R.id.frame);
        mBig = findViewById(R.id.btn_big);
        mSmall = findViewById(R.id.btn_small);
        mUp = findViewById(R.id.btn_up);
        mDown = findViewById(R.id.btn_down);
        mLeft = findViewById(R.id.btn_left);
        mRight = findViewById(R.id.btn_right);
        mBig.setOnClickListener(this);
        mSmall.setOnClickListener(this);
        mUp.setOnClickListener(this);
        mDown.setOnClickListener(this);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        mGLView = new GLView(getApplicationContext());
        mGLFrame.addView(mGLView);

        float threshold = 0.1f;
//        mNumbers = new Numbers(this, R.drawable.number, TestData.numbers1, TestData.positions1, TestData.scales1, threshold);
//
//
        mNumbers = new Numbers(this, R.drawable.number, TestData.numbers, TestData.positions, TestData.scales, threshold);
        mGLView.add(mNumbers);
        mGLView.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up:
                up();
                break;
            case R.id.btn_down:
                down();
                break;
            case R.id.btn_left:
                left();
                break;
            case R.id.btn_right:
                right();
                break;
            case R.id.btn_big:
                big();
                break;
            case R.id.btn_small:
                small();
                break;
        }
    }

    private void up() {
        mNumbers.translate(0, 0.1f);
    }

    private void down() {
        mNumbers.translate(0, -0.1f);
    }

    private void left() {
        mNumbers.translate(-0.1f, 0);
    }

    private void right() {
        mNumbers.translate(0.1f, 0);
    }

    private void big() {
        mNumbers.scale(0.1f);
    }

    private void small() {
        mNumbers.scale(-0.1f);
    }

}
