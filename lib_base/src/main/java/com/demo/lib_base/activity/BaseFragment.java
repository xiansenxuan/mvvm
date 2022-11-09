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

import com.demo.lib_base.Constant;
import com.demo.lib_base.app.MyApplication;

/**
 * @author: wanglei
 * @Description: com.demo.lib_base.activity
 * @since: 2021/3/5 16:34
 */
public abstract class BaseFragment extends DataBindingFragment {
    private ViewModelProvider mFragmentProvider;
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Constant.LIFE_TAG, "onCreate   " + this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(Constant.LIFE_TAG, "onCreateView   " + this);

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(Constant.LIFE_TAG, "onViewCreated: "+this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(Constant.LIFE_TAG, "onStart   " + this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(Constant.LIFE_TAG, "onResume   " + this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(Constant.LIFE_TAG, "onPause   " + this);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(Constant.LIFE_TAG, "onStop   " + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(Constant.LIFE_TAG, "onDestroy   " + this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(Constant.LIFE_TAG, "onDestroyView: "+this);
    }


    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        if (mFragmentProvider == null) {
            mFragmentProvider = new ViewModelProvider(this);
        }
        return mFragmentProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(mActivity);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((MyApplication) mActivity.getApplicationContext());
        }
        return mApplicationProvider.get(modelClass);
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    public void cancelRequest() {

    }
}
