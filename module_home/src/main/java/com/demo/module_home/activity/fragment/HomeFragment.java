package com.demo.module_home.activity.fragment;

import android.os.Bundle;

import com.demo.lib_base.activity.BaseFragment;
import com.demo.module_home.R;
import com.demo.module_home.activity.viewmodel.HomeViewModel;
import com.demo.module_home.databinding.FragmentHomeBinding;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.fragment
 * @since: 2021/3/2 16:52
 */
public class HomeFragment extends BaseFragment{
    private HomeViewModel mState;

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(HomeViewModel.class);
        mBinding=getDataBinding(FragmentHomeBinding.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }
}
