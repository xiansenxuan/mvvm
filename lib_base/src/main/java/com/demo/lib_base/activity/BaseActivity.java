package com.demo.lib_base.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.lib_base.Constant;
import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.utils.SoftInputUtils;
import com.demo.lib_base.utils.StatusBarUtil;

/**
 * @author: wanglei
 * @Description: com.demo.lib_base.activity
 * @since: 2021/3/5 16:28
 */
public abstract class BaseActivity extends DataBindingActivity implements IBaseActivityInter{

    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Constant.LIFE_TAG, "onCreate   " + this);

        initView(savedInstanceState);
    }

    @Override
    public abstract void initView(Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(Constant.LIFE_TAG, "onStart   " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(Constant.LIFE_TAG, "onResume   " + this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(Constant.LIFE_TAG, "onPause   " + this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(Constant.LIFE_TAG, "onStop   " + this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(Constant.LIFE_TAG, "onDestroy   " + this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(Constant.LIFE_TAG, "onRestart   " + this);
    }

    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((MyApplication) this.getApplicationContext());
        }
        return mApplicationProvider.get(modelClass);
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void registerEventBus() {

    }

    @Override
    public void unregisterEventBus() {

    }


    @Override
    public void networkDisconnectedProcessing() {

    }

    @Override
    public void networkConnectionProcessing() {
    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public View getNotNetWorkView() {
        return null;
    }

    @Override
    public View getNotDataView() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        newOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int newCreateOptionsMenu() {
        return 0;
    }

    @Override
    public void newOptionsItemSelected(MenuItem item) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideInput(ev)) {
                SoftInputUtils.hideSoftInputFromWindow(this);
            }
        }
        return dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(MotionEvent event) {
        View v = getCurrentFocus();

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight() + StatusBarUtil.getStatusBarHeight(this);
            int right = left + v.getWidth();

            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}