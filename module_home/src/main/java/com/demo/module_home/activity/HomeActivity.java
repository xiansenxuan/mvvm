package com.demo.module_home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.RouteUrl;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.module_home.R;
import com.demo.module_home.activity.viewmodel.HomeViewModel;
import com.demo.module_home.databinding.ActivityHomeBinding;

@Route(path = RouteUrl.HomeActivity)
public class HomeActivity extends BaseActivity {
    private HomeViewModel mState;
    private ActivityHomeBinding mBinding;

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
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(HomeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        String str=getIntent().getStringExtra("str");

        Toast.makeText(HomeActivity.this,"str = "+str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSupportVisible() {

    }

    @Override
    public void setRxListener() {

    }

    @Override
    public void registerEventBus() {

    }

    @Override
    public void unregisterEventBus() {

    }

    @Override
    public int newCreateOptionsMenu() {
        return 0;
    }

    @Override
    public void newOptionsItemSelected(MenuItem item) {

    }

    @Override
    public void onRefreshData() {

    }

    @Override
    public View getNotNetWorkView() {
        return null;
    }

    @Override
    public View getNotDataView() {
        return null;
    }

    @Override
    public void networkDisconnectedProcessing() {

    }

    @Override
    public void networkConnectionProcessing() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
        resultOther.unregister();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_home)).navigateUp();
    }

}
