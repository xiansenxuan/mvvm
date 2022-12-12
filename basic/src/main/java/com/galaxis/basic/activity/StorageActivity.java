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
import com.demo.lib_base.adapter.BaseListPopupWindow;
import com.demo.lib_base.entity.entity.PopListEntity;
import com.demo.lib_base.route.RouteUrl;
import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.utils.OnClickUtils;
import com.galaxis.basic.R;
import com.galaxis.basic.databinding.ActivityStorageBinding;
import com.galaxis.basic.viewmodel.BasicViewModel;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

@Route(path = RouteUrl.StorageActivity)
public class StorageActivity extends BaseActivity {
    private BasicViewModel mState;
    private ActivityStorageBinding mBinding;

    ActivityResultLauncher<Intent> result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {

            }
        }
    });

    //仓库
    private int mStorage;
    private BaseListPopupWindow.MyAdapter mPopAdapter;

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(BasicViewModel.class);
        mBinding= (ActivityStorageBinding) getDataBinding();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_storage;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getSupportToolBar().setVisibility(View.GONE);

        mBinding.layoutBottom.tvLeft.setVisibility(View.GONE);
        mBinding.layoutBottom.tvMiddle.setText("确认");
        mBinding.layoutBottom.tvRight.setVisibility(View.GONE);

        initPop();
    }

    @Override
    public void setRxListener() {
        super.setRxListener();

        OnClickUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.tv_left){

                }else    if(v.getId()==R.id.tv_middle){
                    //跳转 选择菜单
                    RouteUtils.startMenu();

                }else    if(v.getId()==R.id.tv_right){

                }
            }
        },mBinding.layoutBottom.tvLeft,mBinding.layoutBottom.tvMiddle,mBinding.layoutBottom.tvRight);

    }


    private void  initPop(){
        BaseListPopupWindow popupWindow = new BaseListPopupWindow(this);
        popupWindow.setAnchorView(mBinding.tvRole,new BaseListPopupWindow.OnDataCallBack() {
            @Override
            public void queryData() {
                queryPopData();

            }
        });
        mPopAdapter = popupWindow.addAdapter(new BaseListPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mBinding.tvRole.setText(mPopAdapter.getItem(position).value);

                mStorage= mPopAdapter.getItem(position).key;

                popupWindow.dismiss();
            }
        });
    }

    private void queryPopData() {
        mState.queryStorage(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                ArrayList<Integer> temp=new ArrayList<>();

                if(o instanceof Integer){
                    temp.add((Integer) o);

                    ArrayList<PopListEntity> list = new ArrayList<>();
                    for (Integer datum : temp) {
                        PopListEntity entity = new PopListEntity();
                        entity.key = datum;
                        entity.value = String.valueOf(datum);
                        list.add(entity);
                    }
                    mPopAdapter.addItemNotify(list);

                }else{
                    mState.processingSeparationResult(o);
                }
            }
        },bindToLifecycle());
    }

    @Override
    public void onFirstSupportVisible() {
        super.onFirstSupportVisible();

        queryPopData();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        queryData();
    }

    @Override
    public void queryData() {
        super.queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
    }
}
