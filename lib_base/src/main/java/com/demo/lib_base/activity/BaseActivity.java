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

import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.constant.SystemDefaultConfig;
import com.demo.lib_base.dialog.CircleLoadingView;
import com.demo.lib_base.utils.SoftInputUtils;

/**
 * @author: wanglei
 * @Description: com.demo.lib_base.activity
 * @since: 2021/3/5 16:28
 */
public abstract class BaseActivity extends DataBindingActivity {

    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    public CircleLoadingView loadingView;

    //是否第一次进入布局
    private boolean isFirstEnterPage = true;
    //是否可见
    private boolean isResume = false;
    //是否是重新连接 才需要执行网络切换操作
    private boolean isReconnection = false;
    //是否第一次调用 onSupportVisible
    private boolean isOnSupportVisible = true;
    /**
     * 加载的页数
     */
    public int pageNo = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(SystemDefaultConfig.LIFE_TAG, "onCreate   " + this);

        initView(savedInstanceState);

        //初始化进度条
        loadingView = new CircleLoadingView(this);

        setRxListener();
    }

    @Override
    public abstract void initView(Bundle savedInstanceState);

    @Override
    public void setRxListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onStart   " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onResume   " + this);

        isResume = true;

        onSupportVisible();

        if (isOnSupportVisible) {
            onFirstSupportVisible();
        }
        isOnSupportVisible = false;
    }

    /**
     * 每次onResume都会调用
     */
    @Override
    public void onSupportVisible() {
        Log.d(SystemDefaultConfig.LIFE_TAG, "onSupportVisible:   " + this);
    }

    /**
     * 首次onResume
     */
    public void onFirstSupportVisible() {
        Log.d(SystemDefaultConfig.LIFE_TAG, "onFirstSupportVisible:   " + this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onPause   " + this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onStop   " + this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onDestroy   " + this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onRestart   " + this);
    }

    public <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this);
        }
        return mActivityProvider.get(modelClass);
    }

    public <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
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
            View v = getCurrentFocus();

            if (isShouldHideInput(v,ev)) {
                SoftInputUtils.hideSoftInputFromWindow(v);
            } else {

            }
        }
        return super.dispatchTouchEvent(ev);

        /*
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
        */
    }

    public boolean isShouldHideInput(View v,MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();

            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    public void cancelRequest() {

    }


    /**
     * 刷新操作
     */
    public void onRefresh() {
        pageNo = 1;
    }

    /**
     * 加载更多操作
     */
    public void onLoadMore() {
        pageNo++;
    }

    /**
     * 查询数据
     */
    public void queryData() {
    }

}
