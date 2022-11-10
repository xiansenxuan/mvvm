package com.demo.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.route.RouteUrl;
import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.databinding.ActivityMainBinding;

@Route(path = RouteUrl.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, com.demo.lib_base.R.layout.activity_main);

        binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteUtils.startHome("ARouter参数");
            }
        });


    }

}
