package com.demo.lib_base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.activity
 * @since: 2021/11/12 14:23
 */
public abstract class DataBindingFragment extends RxFragment implements IBaseFragmentInter{
    protected AppCompatActivity mActivity;
    protected ViewDataBinding mBinding;

    public DataBindingFragment() {
    }

    @Override
    public ViewDataBinding getDataBinding() {
        return mBinding;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity)context;
    }

/*
    public <V extends ViewDataBinding> V getDataBinding(Class<V> modelClass){
        if(modelClass.isInstance(mBinding)){
            try {
                mBinding = modelClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " +modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
        throw new RuntimeException("Cannot create an instance of " + modelClass);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        mBinding.setLifecycleOwner(this);

        initViewModel();
        initView(savedInstanceState,mBinding.getRoot());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        initArguments(bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mBinding.unbind();
        this.mBinding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
