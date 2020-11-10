package com.demo.module_home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.RouteUrl;
import com.demo.lib_base.RouteUtils;
import com.demo.module_home.R;
import com.demo.module_home.databinding.ActivityHomeBinding;

@Route(path = RouteUrl.activity_home)
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"点击home",Toast.LENGTH_SHORT).show();
                RouteUtils.startMain();

            }
        });


    }
}
