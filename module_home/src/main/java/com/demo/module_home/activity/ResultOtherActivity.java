package com.demo.module_home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.demo.module_home.R;
import com.demo.module_home.databinding.ActivityResultOtherBinding;

public class ResultOtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResultOtherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_result_other);

        binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent();
                data.putExtra("str","第二个result");
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }
}
