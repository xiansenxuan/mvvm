package com.demo.module_home.activity.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.demo.lib_base.activity.BaseFragment;
import com.demo.lib_base.utils.ToastUtils;
import com.demo.module_home.R;
import com.demo.module_home.activity.viewmodel.LoginViewModel;
import com.demo.module_home.databinding.FragmentLoginBinding;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.fragment
 * @since: 2021/3/2 14:45
 */
public class LoginFragment extends BaseFragment{
    private LoginViewModel mState;
    private FragmentLoginBinding mBinding;

    @Override
    public void initViewModel() {
        mState=getFragmentScopeViewModel(LoginViewModel.class);
        mBinding= (FragmentLoginBinding) getDataBinding();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState.login();
            }
        });


        mState.isLogin.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    //关闭登录 进入首页
                    ToastUtils.showDefaultToast(getActivity(),mState.userName.get()+" / "+mState.password.get());
                    Navigation.findNavController(getActivity(), R.id.nav_home).navigate(R.id.action_loginFragment_to_homeFragment);
                }else{

                }
            }
        });
    }
}
