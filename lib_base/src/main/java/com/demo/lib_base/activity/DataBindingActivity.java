package com.demo.lib_base.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.demo.lib_base.R;
import com.demo.lib_base.widget.view.SupportToolBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.activity
 * @since: 2021/11/11 18:41
 */
public  abstract class DataBindingActivity extends RxAppCompatActivity  implements IBaseActivityInter {
    protected ViewDataBinding mBinding;
    protected SupportToolBar mSupportToolBar;

    public DataBindingActivity() {

    }

    @Override
    public ViewDataBinding getDataBinding() {
        return mBinding;
    }

    @Override
    public SupportToolBar getSupportToolBar() {
        return mSupportToolBar;
    }

    protected abstract void initViewModel();

    protected abstract int getLayoutResId();
/*
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
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        mBinding.setLifecycleOwner(this);

        // 从最上层窗口 添加一个LinearLayout 竖直方向，
        // 然后添加 一个 supportToolbar和当前activity布局
        ViewGroup viewGroup=findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        
        LinearLayout contentLayout =new  LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setBackgroundResource(android.R.color.white);
        viewGroup.addView(contentLayout);

        mSupportToolBar= (SupportToolBar) LayoutInflater.from(this).inflate(R.layout.support_layout, null, true);
        contentLayout.addView(mSupportToolBar);

        ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(-1,-1);
        contentLayout.addView(mBinding.getRoot(),layoutParams);

        initViewModel();
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
