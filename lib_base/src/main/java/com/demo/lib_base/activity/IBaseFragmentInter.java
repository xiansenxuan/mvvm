package com.demo.lib_base.activity;


import android.os.Bundle;
import android.view.View;

import androidx.databinding.ViewDataBinding;


/**
 * Created by xuan on 2017/5/19.
 */

public interface IBaseFragmentInter {

    void initViewModel();

    ViewDataBinding getDataBinding();

    /**
     * 显示
     */
    void showFragment();

    /**
     * 隐藏
     */
    void hideFragment();

    /**
     * 布局ID
     * @return
     */
    int getLayoutResId();

    /**
     * 初始化布局
     * @param savedInstanceState
     * @param view
     */
    void initView(Bundle savedInstanceState, View view);

    /**
     * 接收传递过来的bundle（ getArguments ）
     * @param args
     */
    void initArguments(Bundle args);

    /**
     * 各种监听
     */
    void setRxListener();

    void registerEventBus();

    void unregisterEventBus();

    /**
     * 刷新当前界面数据（主要用于无网络重新联网的时候会调用）
     */
    void onRefreshData();

    /**
     * 获取无网络布局
     * @return
     */
    View getNotNetWorkView();

    /**
     * 没有网络需要特殊处理的方法（非显示网络布局）
     */
    void networkDisconnectedProcessing();

    /**
     * //有网络了需要重新处理的方法（如果没有添加网络布局或者需要特殊处理才使用）
     */
    void networkConnectionProcessing();
}
