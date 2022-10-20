package com.demo.module_home.activity.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.demo.lib_base.activity.BaseFragment;
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
    protected void initViewModel() {
        mState= new ViewModelProvider(this).get(LoginViewModel.class);
        mBinding=getDataBinding(FragmentLoginBinding.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
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
                    //关闭登录
                    Navigation.findNavController(getActivity(), R.id.nav_home).navigate(R.id.action_loginFragment_to_homeFragment);
                }else{

                }
            }
        });
    }

}
