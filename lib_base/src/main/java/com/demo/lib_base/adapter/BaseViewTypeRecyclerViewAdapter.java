package com.demo.lib_base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: wanglei
 * @Description: 支持viewType的适配器基类
 * @since: 2020/12/2 15:38
 */
public abstract class BaseViewTypeRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends BaseRecyclerViewAdapter<VH> {

    public abstract int getBaseItemViewType(int position);

    @Override
    public int getItemViewType(int position) {
        return getBaseItemViewType(position);
    }

    public abstract int getLayoutResId(int viewType);

    public abstract VH onCreateViewHolder(View view,int viewType);

    @Override
    public VH onBaseCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false);
        return onCreateViewHolder(view,viewType);
    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public VH onCreateViewHolder(View view) {
        return null;
    }
}
