package com.demo.module_msg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.RouteUrl;
import com.demo.lib_base.RouteUtils;
import com.demo.module_msg.R;
import com.demo.module_msg.databinding.ActivityMsgBinding;

@Route(path = RouteUrl.activity_msg)
public class MsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        ActivityMsgBinding mBinding = ActivityMsgBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteUtils.startHome();
            }
        });
*/

        ActivityMsgBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_msg);

        binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MsgActivity.this,"点击Msg",Toast.LENGTH_SHORT).show();
                RouteUtils.startHome();
            }
        });

    }
}
