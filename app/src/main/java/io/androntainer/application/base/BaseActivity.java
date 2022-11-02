package io.androntainer.application.base;

import android.view.View;

import com.kongzue.baseframework.util.JumpParameter;


abstract public class BaseActivity extends com.kongzue.baseframework.BaseActivity {

    @Override
    public void initViews() {

    }

    @Override
    public View resetContentView() {
        contentView();
        return super.resetContentView();
    }

    @Override
    public void initDatas(JumpParameter parameter) {

    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public abstract void init();
    public abstract void contentView();
}