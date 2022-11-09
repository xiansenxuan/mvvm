package com.demo.module_home.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.lib_base.activity.BaseFragment;
import com.demo.lib_base.adapter.BaseRecyclerViewAdapter;
import com.demo.lib_base.adapter.BaseViewHolder;
import com.demo.lib_base.app.MyApplication;
import com.demo.module_home.R;
import com.demo.module_home.activity.viewmodel.HomeViewModel;
import com.demo.module_home.databinding.FragmentHomeBinding;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.fragment
 * @since: 2021/3/2 16:52
 */
public class HomeFragment extends BaseFragment{
    private HomeViewModel mState;
    private FragmentHomeBinding mBinding;
    private MyRecyclerViewAdapter mAdapter;

    @Override
    protected void initViewModel() {
        mState=getActivityScopeViewModel(HomeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(manager);

        mAdapter = new MyRecyclerViewAdapter();
        addRecyclerViewAdapter(mRecyclerView, mAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.base_gray)
                .build();
        mBinding.mRecyclerView.addItemDecoration(divider);

        addOnRefreshListener(mBinding.mRecyclerView, mAdapter);
        addOnLoadMoreListener(mBinding.mRecyclerView);
    }

    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyRecyclerViewAdapter.ViewHolder> {
        @Override
        public int getLayoutResId() {
            return R.layout.item_home_rv;
        }

        @Override
        public ViewHolder onCreateViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position) {
            if (position == isSelectPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(MyApplication.getInstance().getApplicationContext(), R.color.base_gray));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }

        class ViewHolder extends BaseViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}
