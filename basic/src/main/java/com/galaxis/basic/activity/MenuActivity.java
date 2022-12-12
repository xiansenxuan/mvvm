package com.galaxis.basic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.adapter.BaseRecyclerViewAdapter;
import com.demo.lib_base.adapter.BaseViewHolder;
import com.demo.lib_base.route.RouteUrl;
import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.utils.ToastUtils;
import com.demo.lib_base.utils.ViewUtils;
import com.demo.lib_base.widget.view.SupportToolBar;
import com.galaxis.basic.R;
import com.galaxis.basic.databinding.ActivityMenuBinding;
import com.galaxis.basic.viewmodel.BasicViewModel;

import java.util.ArrayList;

@Route(path = RouteUrl.MenuActivity)
public class MenuActivity extends BaseActivity {
    private BasicViewModel mState;
    private ActivityMenuBinding mBinding;
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
        mState=getActivityScopeViewModel(BasicViewModel.class);
        mBinding= (ActivityMenuBinding) getDataBinding();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_menu;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getSupportToolBar().addLeftBackView(this);
        getSupportToolBar().addMiddleTextView("姓名");
        getSupportToolBar().addRightTextView("更多", new SupportToolBar.OnRxClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showDefaultToast("敬请期待");
            }
        });

        setRecyclerView();

        addData();
    }

    private void addData() {
        ArrayList<String> list=new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(""+i);
        }

        mAdapter.clearNotify();
        mAdapter.addItemNotify(list);
        //mBinding.recyclerView.setHaveNextPage(mAdapter.getItemCount() >= SystemDefaultConfig.pageSize && mAdapter.getItemCount() < list.total);
        mBinding.recyclerView.refreshComplete();
    }

    @Override
    public void setRxListener() {
        super.setRxListener();
    }

    private void setRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this,4);
        mBinding.recyclerView.setLayoutManager(manager);

        mAdapter = new MyRecyclerViewAdapter();
        ViewUtils.addRecyclerViewAdapter(mBinding.recyclerView, mAdapter);

/*        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.base_gray)
                .build();
        mBinding.recyclerView.addItemDecoration(divider);*/

        ViewUtils.addOnRefreshListener(this,mBinding.recyclerView, mAdapter);
        //ViewUtils.addOnLoadMoreListener(this,mBinding.recyclerView);

        mAdapter.addOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RouteUtils.startTmsFlatActivity();
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        queryData();
    }

    @Override
    public void queryData() {
        super.queryData();

        addData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        result.unregister();
    }


    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyRecyclerViewAdapter.ViewHolder> {
        @Override
        public int getLayoutResId() {
            return R.layout.item_menu;
        }

        @Override
        public ViewHolder onCreateViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position) {
        }

        class ViewHolder extends BaseViewHolder {
            ImageView iv_menu;
            TextView tv_menu;

            public ViewHolder(View itemView) {
                super(itemView);

                iv_menu=$(R.id.iv_menu);
                tv_menu=$(R.id.tv_menu);
            }

        }

    }
}
