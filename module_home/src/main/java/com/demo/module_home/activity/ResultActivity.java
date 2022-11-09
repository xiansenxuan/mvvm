package com.demo.module_home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.demo.lib_base.activity.BaseActivity;
import com.demo.module_home.R;
import com.demo.module_home.activity.viewmodel.ResultViewModel;
import com.demo.module_home.databinding.ActivityResultBinding;

public class ResultActivity extends BaseActivity {

    private ResultViewModel mResultViewModel;
    private ActivityResultBinding mBinding;

    @Override
    protected void initViewModel() {
        mResultViewModel=getActivityScopeViewModel(ResultViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_result;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent();
                data.putExtra("number",mResultViewModel.number);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        mResultViewModel =  new ViewModelProvider(this).get(ResultViewModel.class);

        mResultViewModel.number=888;
    }

    @Override
    public void setRxListener() {

    }
}
