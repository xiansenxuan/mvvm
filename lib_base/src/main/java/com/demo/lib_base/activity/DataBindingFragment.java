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
import androidx.fragment.app.Fragment;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.activity
 * @since: 2021/11/12 14:23
 */
public abstract class DataBindingFragment extends Fragment {
    protected AppCompatActivity mActivity;
    protected ViewDataBinding mBinding;

    public DataBindingFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity)context;
    }

    protected abstract void initViewModel();

    protected abstract int getLayoutResId();

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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initViewModel();
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }


    public boolean isDebug() {
        return this.mActivity.getApplicationContext().getApplicationInfo() != null && (this.mActivity.getApplicationContext().getApplicationInfo().flags & 2) != 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mBinding.unbind();
        this.mBinding = null;
    }

}
