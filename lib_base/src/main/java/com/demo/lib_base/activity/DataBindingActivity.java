package com.demo.lib_base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.activity
 * @since: 2021/11/11 18:41
 */
public  abstract class DataBindingActivity extends AppCompatActivity {
    protected ViewDataBinding mBinding;

    public DataBindingActivity() {
    }

    protected abstract void initViewModel();

    protected abstract int getLayoutResId();

    public <V extends ViewDataBinding> V getDataBinding(Class<V> modelClass){
        if(modelClass.isInstance(mBinding)){
            try {
                mBinding = modelClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " +modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
        throw new RuntimeException("Cannot create an instance of " + modelClass);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();

        mBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        mBinding.setLifecycleOwner(this);
    }

    public boolean isDebug() {
        return this.getApplicationContext().getApplicationInfo() != null && (this.getApplicationContext().getApplicationInfo().flags & 2) != 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mBinding.unbind();
        this.mBinding = null;
    }
}