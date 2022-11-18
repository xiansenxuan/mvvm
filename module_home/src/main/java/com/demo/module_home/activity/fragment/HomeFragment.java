package com.demo.module_home.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.lib_base.activity.BaseFragment;
import com.demo.lib_base.adapter.BaseRecyclerViewAdapter;
import com.demo.lib_base.adapter.BaseViewHolder;
import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.utils.ToastUtils;
import com.demo.lib_base.utils.ViewUtils;
import com.demo.module_home.R;
import com.demo.module_home.activity.data.UserData;
import com.demo.module_home.activity.viewmodel.HomeViewModel;
import com.demo.module_home.databinding.FragmentHomeBinding;
import com.xuan.view.ItemDecoration.DividerDecoration;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

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
    public void initViewModel() {
        mState=getFragmentScopeViewModel(HomeViewModel.class);
        mBinding= (FragmentHomeBinding) getDataBinding();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.setLayoutManager(manager);

        mAdapter = new MyRecyclerViewAdapter();
        ViewUtils.addRecyclerViewAdapter(mBinding.recyclerView, mAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.base_gray)
                .build();
        mBinding.recyclerView.addItemDecoration(divider);

        ViewUtils.addOnRefreshListener(this,mBinding.recyclerView, mAdapter);
        ViewUtils.addOnLoadMoreListener(this,mBinding.recyclerView);
    }

    @Override
    public void onFirstSupportVisible() {
        super.onFirstSupportVisible();

        ArrayList<String> list=mState.refreshData();

        if (pageNo == 1 || mAdapter.getItemCount() == 0) {
            mAdapter.clear();

            if ((list == null ||list.isEmpty())) {
                ToastUtils.showDefaultToast(getActivity(),"当前数据为空，请检查后输入");
                return;
            }

            //一条数据的时候 默认选中
            if (list.size() == 1) {
                mAdapter.isSelectPosition = 0;
            }
        }

        mAdapter.addItem(list);
        mAdapter.notifyDataSetChanged();
        //mBinding.recyclerView.setHaveNextPage(mAdapter.getItemCount() >= SystemDefaultConfig.pageSize && mAdapter.getItemCount() < list.total);
        mBinding.recyclerView.refreshComplete();

        mState.requestData(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if(o instanceof UserData){
                    UserData data = (UserData) o;
                    //成功回调
                }else {
                    //失败回调
                    mState.processingSeparationResult(o);
                }
            }
        },bindToLifecycle());
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
            String text= (String) getItem(position);

            holder.tv_content.setText(text);

            if (position == isSelectPosition) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(MyApplication.getInstance().getApplicationContext(), R.color.base_gray));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }

        class ViewHolder extends BaseViewHolder {
            TextView tv_content;

            public ViewHolder(View itemView) {
                super(itemView);

                tv_content=$(R.id.tv_content);
            }

        }

    }
}
