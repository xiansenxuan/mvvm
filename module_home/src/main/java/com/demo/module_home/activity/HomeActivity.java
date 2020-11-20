package com.demo.module_home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.RouteUrl;
import com.demo.module_home.R;
import com.demo.module_home.databinding.ActivityHomeBinding;

@Route(path = RouteUrl.activity_home)
public class HomeActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                int number = result.getData().getIntExtra("number", -1);

                Toast.makeText(HomeActivity.this, "number = " + number, Toast.LENGTH_SHORT).show();
            }
        }
    });

    ActivityResultLauncher<Intent> resultOther = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                String str = result.getData().getStringExtra("str");

                Toast.makeText(HomeActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }
    });

    int i=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String str=getIntent().getStringExtra("str");

        Toast.makeText(HomeActivity.this,"str = "+str,Toast.LENGTH_SHORT).show();


        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i%2==0){
                    result.launch(new Intent(HomeActivity.this,ResultActivity.class));
                }else{
                    resultOther.launch(new Intent(HomeActivity.this,ResultOtherActivity.class));
                }

                i++;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
        resultOther.unregister();
    }
}
