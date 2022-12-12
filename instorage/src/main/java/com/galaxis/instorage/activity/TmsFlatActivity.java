package com.galaxis.instorage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.entity.instorage.InStorageEntity;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.adapter.BaseRecyclerViewAdapter;
import com.demo.lib_base.adapter.BaseViewHolder;
import com.demo.lib_base.inter.OnScanCallback;
import com.demo.lib_base.route.RouteUrl;
import com.demo.lib_base.utils.OnClickUtils;
import com.demo.lib_base.utils.RulesUtils;
import com.demo.lib_base.utils.StringActionUtils;
import com.demo.lib_base.utils.ToastUtils;
import com.demo.lib_base.utils.ViewUtils;
import com.demo.lib_base.widget.view.SupportToolBar;
import com.galaxis.instorage.R;
import com.galaxis.instorage.databinding.ActivityTmsFlatBinding;
import com.galaxis.instorage.viewmodel.InStorageViewModel;
import com.xuan.view.ItemDecoration.DividerDecoration;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

@Route(path = RouteUrl.TmsFlatActivity)
public class TmsFlatActivity extends BaseActivity {
    private InStorageViewModel mState;
    private ActivityTmsFlatBinding mBinding;
    private MyRecyclerViewAdapter mAdapter;

    ActivityResultLauncher<Intent> result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {

            }
        }
    });

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(InStorageViewModel.class);
        mBinding= (ActivityTmsFlatBinding) getDataBinding();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tms_flat;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getSupportToolBar().addLeftBackView(this);
        getSupportToolBar().addMiddleTextView("TMS入平库");
        getSupportToolBar().addRightTextView("更多", new SupportToolBar.OnRxClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showDefaultToast("敬请期待");
            }
        });

        mBinding.layoutBottom.tvLeft.setText("确定");
        mBinding.layoutBottom.tvMiddle.setText("删除");
        mBinding.layoutBottom.tvRight.setText("下一步");

        setRecyclerView();

        ViewUtils.addOnScanListener(mBinding.etCode, new OnScanCallback() {
            @Override
            public void onScan(String text) {

                ViewUtils.requestFocus(mBinding.etPn);
            }
        });

        ViewUtils.addOnScanListener(mBinding.etPn, new OnScanCallback() {
            @Override
            public void onScan(String text) {
                if(isRack(text)){
                    return;
                }

                ViewUtils.requestFocus(mBinding.etLot);
            }
        });

        ViewUtils.addOnScanListener(mBinding.etLot, new OnScanCallback() {
            @Override
            public void onScan(String text) {
                if(isRack(text)){
                    return;
                }

                ViewUtils.requestFocus(mBinding.etCount);
            }
        });

        ViewUtils.addOnScanListener(mBinding.etCount, new OnScanCallback() {
            @Override
            public void onScan(String text) {
                if(isRack(text)){
                    return;
                }

                if(!TextUtils.isEmpty(mBinding.etCode.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mBinding.etPn.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mBinding.etLot.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mBinding.etCount.getText().toString().trim()) ){
                    //刷新数组
                    addData();
                    ToastUtils.showDefaultToast(TmsFlatActivity.this,"添加成功");
                }else{
                    ToastUtils.showDefaultToast("有未填项目，请检查后重试");
                }
            }
        });
    }

    private boolean isRack(String text){
        if(RulesUtils.isRack(text)){
            mBinding.etCode.setText(text);
            mBinding.etPn.setText("");
            mBinding.etLot.setText("");
            mBinding.etCode.setText("");

            mBinding.etPn.requestFocus();

            return true;
        }

        return false;
    }

    private void addData() {
            InStorageEntity data=new InStorageEntity();
            data.code=mBinding.etCode.getText().toString().trim();
            data.pn=mBinding.etPn.getText().toString().trim();
            data.lotId=mBinding.etLot.getText().toString().trim();
            data.count=mBinding.etCount.getText().toString().trim();
            data.number=mAdapter.getItemCount()+1;
            data.isCheck=false;

            mAdapter.addItemNotify(data);
            mAdapter.notifyDataSetChanged();
            //mBinding.recyclerView.setHaveNextPage(mAdapter.getItemCount() >= SystemDefaultConfig.pageSize && mAdapter.getItemCount() < list.total);
            mBinding.recyclerView.refreshComplete();
    }

    @Override
    public void setRxListener() {
        super.setRxListener();

        OnClickUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.tv_left){
                    if(!TextUtils.isEmpty(mBinding.etCode.getText().toString().trim()) &&
                            !TextUtils.isEmpty(mBinding.etPn.getText().toString().trim()) &&
                            !TextUtils.isEmpty(mBinding.etLot.getText().toString().trim()) &&
                            !TextUtils.isEmpty(mBinding.etCount.getText().toString().trim()) ){
                        //刷新数组
                        addData();
                        ToastUtils.showDefaultToast(TmsFlatActivity.this,"添加成功");
                    }else{
                        ToastUtils.showDefaultToast("有未填项目，请检查后重试");
                    }

                }else    if(v.getId()==R.id.tv_middle){
                    if(!mAdapter.isEmpty()){
                        ArrayList<InStorageEntity> list=new ArrayList<>();

                        for (int i = 0; i < mAdapter.mItemList.size(); i++) {
                            InStorageEntity data= (InStorageEntity) mAdapter.mItemList.get(i);
                            if(!data.isCheck){
                                list.add(data);
                            }
                        }

                        mAdapter.clearNotify();
                        mAdapter.addItemNotify(list);
                        mAdapter.notifyDataSetChanged();
                        //mBinding.recyclerView.setHaveNextPage(mAdapter.getItemCount() >= SystemDefaultConfig.pageSize && mAdapter.getItemCount() < list.total);
                        mBinding.recyclerView.refreshComplete();
                    }

                }else    if(v.getId()==R.id.tv_right){
                    ToastUtils.showDefaultToast("敬请期待");
                }
            }
        },mBinding.layoutBottom.tvLeft,mBinding.layoutBottom.tvMiddle,mBinding.layoutBottom.tvRight);
    }

    private void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(manager);

        mAdapter = new MyRecyclerViewAdapter();
        ViewUtils.addRecyclerViewAdapter(mBinding.recyclerView, mAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.base_gray)
                .build();
        mBinding.recyclerView.addItemDecoration(divider);

        ViewUtils.addOnRefreshListener(this,mBinding.recyclerView, mAdapter);
        ViewUtils.addOnLoadMoreListener(this,mBinding.recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
    }


    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyRecyclerViewAdapter.ViewHolder> {
        @Override
        public int getLayoutResId() {
            return R.layout.item_tms_flat;
        }

        @Override
        public ViewHolder onCreateViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position) {
            InStorageEntity data= (InStorageEntity) getItem(position);

            holder.cb_select.setChecked(data.isCheck);
            holder.tv_number.setText(String.valueOf(data.number));
            holder.tv_code.setText(TextUtils.isEmpty(data.code)?"":data.code);
            holder.tv_pn.setText(TextUtils.isEmpty(data.pn)?"":data.pn);

            holder.tv_lot.setText(StringActionUtils.lineChange("LotID: "+data.lotId+" / "+"数量:  "+data.count));

            holder.cb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.isCheck=!data.isCheck;
                    holder.cb_select.setChecked(data.isCheck);
                }
            });

        }

        class ViewHolder extends BaseViewHolder {
            CheckBox cb_select;
            TextView tv_number;
            TextView tv_code;
            TextView tv_pn;
            TextView tv_lot;

            public ViewHolder(View itemView) {
                super(itemView);

                cb_select=$(R.id.cb_select);
                tv_number=$(R.id.tv_number);
                tv_code=$(R.id.tv_code);
                tv_pn=$(R.id.tv_pn);
                tv_lot=$(R.id.tv_lot);
            }

        }

    }
}
