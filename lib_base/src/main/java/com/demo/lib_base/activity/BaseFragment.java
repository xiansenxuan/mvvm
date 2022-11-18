package com.demo.lib_base.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.constant.SystemDefaultConfig;

/**
 * @author: wanglei
 * @Description: com.demo.lib_base.activity
 * @since: 2021/3/5 16:34
 */
public abstract class BaseFragment extends DataBindingFragment{
    private ViewModelProvider mFragmentProvider;
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    //是否第一次进入布局
    private boolean isFirstEnterPage = true;
    //是否是重新连接 才需要执行网络切换操作
    private boolean isReconnection = false;
    /**
     * 是否第一次调用onResume
     */
    public boolean isOnSupportVisible = true;
    /**
     * 加载的页数
     */
    public int pageNo=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(SystemDefaultConfig.LIFE_TAG, "onCreate   " + this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(SystemDefaultConfig.LIFE_TAG, "onCreateView   " + this);

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(SystemDefaultConfig.LIFE_TAG, "onViewCreated: "+this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onStart   " + this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onResume   " + this);

        onSupportVisible();

        if (isOnSupportVisible) {
            onFirstSupportVisible();
        }
        isOnSupportVisible = false;
    }

    /**
     * 每次onResume都会调用
     */
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
    public void onPause() {
        super.onPause();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onPause   " + this);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onStop   " + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onDestroy   " + this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(SystemDefaultConfig.LIFE_TAG, "onDestroyView: "+this);
    }


    public <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        if (mFragmentProvider == null) {
            mFragmentProvider = new ViewModelProvider(this);
        }
        return mFragmentProvider.get(modelClass);
    }

    public <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(mActivity);
        }
        return mActivityProvider.get(modelClass);
    }

    public <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((MyApplication) mActivity.getApplicationContext());
        }
        return mApplicationProvider.get(modelClass);
    }

    public NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    public void cancelRequest() {

    }

    @Override
    public void networkDisconnectedProcessing() {

    }

    @Override
    public void networkConnectionProcessing() {

    }

    @Override
    public void  onRefreshData() {

    }

    @Override
    public View getNotNetWorkView() {
        return null;
    }

    @Override
    public void setRxListener() {

    }

    @Override
    public void initArguments(Bundle args) {

    }

    @Override
    public void registerEventBus() {

    }

    @Override
    public void unregisterEventBus() {

    }

    @Override
    public void showFragment(){
        Log.d(SystemDefaultConfig.LIFE_TAG, "showFragment:   "+this);

        if(getFragmentManager()!=null && getFragmentManager().beginTransaction()!=null){
            getFragmentManager().beginTransaction().show(this).commit();
        }else if(getChildFragmentManager()!=null && getChildFragmentManager().beginTransaction()!=null){
            getChildFragmentManager().beginTransaction().show(this).commit();
        }


    }

    @Override
    public void hideFragment(){
        Log.d(SystemDefaultConfig.LIFE_TAG, "hideFragment:   "+this);

        if(getFragmentManager()!=null && getFragmentManager().beginTransaction()!=null){
            getFragmentManager().beginTransaction().hide(this).commit();
        }else if(getChildFragmentManager()!=null && getChildFragmentManager().beginTransaction()!=null){
            getChildFragmentManager().beginTransaction().hide(this).commit();
        }


    }
    /**
     * 刷新操作
     */
    public void onRefresh(){
        pageNo=1;
    }

    /**
     * 加载更多操作
     */
    public void onLoadMore(){
        pageNo++;
    }

    /**
     * 查询数据
     */
    public void queryData(){}

    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
    }
}
