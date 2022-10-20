package com.demo.lib_base.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

/**
 * Created by xuan on 2017/5/19.
 */

public interface IBaseActivityInter {
    /**
     * @return null 不添加Toolbar
     */
    Toolbar getToolbar();

    /**
     * 在各种根元素初始化结束后的 其他参数化操作
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 界面用户可见状态
     */
    void onSupportVisible();

    /**
     * 设置各种监听
     */
    void setRxListener();

    /**
     * 注册EventBus
     */
    void registerEventBus();

    /**
     * 注销EventBus
     */
    void unregisterEventBus();

//    /**
//     * 需要自定义 则复写
//     * @param mSearchView
//     * @param mSearchAutoComplete
//     */
//    void setSearchView(SearchView mSearchView, SearchView.SearchAutoComplete mSearchAutoComplete);
//
//    /**
//     * 需要自定义监听 则复写
//     * @param mSearchView
//     * @param mSearchAutoComplete
//     */
//    void addSearchEvent(SearchView mSearchView, SearchView.SearchAutoComplete mSearchAutoComplete);
//
//    /**
//     * @return null 不添加SearchView
//     */
//    BaseActivity.SearchCallBackListener addSearchListener();

    /**
     * @return 添加一个新的菜单
     */
    int newCreateOptionsMenu();

    /**
     * 新菜单点击事件
     * @param item
     */
    void newOptionsItemSelected(MenuItem item);

    /**
     * 刷新当前界面数据（主要用于无网络重新联网的时候）
     */
    void onRefreshData();

    /**
     * 获取无网络布局
     * @return
     */
    View getNotNetWorkView();

    /**
     * 获取无数据布局
     * @return
     */
    View getNotDataView();

    /**
     * 没有网络需要特殊处理的方法（非显示无网络布局）
     */
    void networkDisconnectedProcessing();

    /**
     * //有网络了需要重新处理的方法（如果没有添加网络布局或者需要特殊处理才使用）
     */
    void networkConnectionProcessing();
}
