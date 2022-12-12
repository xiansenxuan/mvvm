package com.galaxis.basic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.route.RouteUrl;
import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.utils.OnClickUtils;
import com.galaxis.basic.R;
import com.galaxis.basic.databinding.ActivityLoginBinding;
import com.galaxis.basic.viewmodel.BasicViewModel;

@Route(path = RouteUrl.LoginActivity)
public class LoginActivity extends BaseActivity {
    private BasicViewModel mState;
    private ActivityLoginBinding mBinding;

    ActivityResultLauncher<Intent> result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {

            }
        }
    });

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(BasicViewModel.class);
        mBinding= (ActivityLoginBinding) getDataBinding();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getSupportToolBar().setVisibility(View.GONE);

        mBinding.layoutBottom.tvLeft.setVisibility(View.GONE);
        mBinding.layoutBottom.tvMiddle.setText("登录");
        mBinding.layoutBottom.tvRight.setVisibility(View.GONE);
    }

    @Override
    public void setRxListener() {
        super.setRxListener();

        OnClickUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.tv_left){

                }else    if(v.getId()==R.id.tv_middle){
                    //跳转 选择仓库界面
                    RouteUtils.startStorage();

                }else    if(v.getId()==R.id.tv_right){
                }
            }
        },mBinding.layoutBottom.tvLeft,mBinding.layoutBottom.tvMiddle,mBinding.layoutBottom.tvRight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
    }
}
