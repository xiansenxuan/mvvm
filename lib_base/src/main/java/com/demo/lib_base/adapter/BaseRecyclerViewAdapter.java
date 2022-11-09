package com.demo.lib_base.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: wanglei
 * @Description: 适配器基类
 * @since: 2020/12/2 15:38
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    public ArrayList<Object> mItemList = new ArrayList<>();

    protected OnItemSelectListener onItemSelectListener;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    /**
     * 点击选中的那一条
     */
    public int isSelectPosition;

    public BaseRecyclerViewAdapter() {
        //添加一个默认选中的监听器
        addOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void onSelect(View view, int position) {
                isSelectPosition=position;

                notifyDataSetChanged();
            }
        });
    }

    public boolean isEmpty(){
        return this.mItemList.isEmpty();
    }


    public Object getSelectItem(){
        if(mItemList!=null && mItemList.size()>isSelectPosition){
            return mItemList.get(isSelectPosition);
        }else{
            return null;
        }
    }


    public Object getItem(int position){
        if(mItemList!=null && mItemList.size()>position){
            return mItemList.get(position);
        }else{
            return null;
        }
    }

    /**
     * 插入一组数据
     * @param itemList
     * @param <T>
     */
    public <T> void addItem(List<T> itemList) {
        if (itemList!=null) {
            this.mItemList.addAll(itemList);
        }
    }

    /**
     * 插入一组数据 刷新
     * @param itemList
     * @param <T>
     */
    public <T> void addItemNotify(List<T> itemList) {
        if (itemList!=null) {
            this.mItemList.addAll(itemList);
            notifyDataSetChanged();
        }
    }

    /**
     * 单独插入一条
     * @param t
     * @param <T>
     */
    public <T> void addItem(T t) {
        if (t!=null) {
            this.mItemList.add(t);
        }
    }

    /**
     * 单独插入一条 刷新
     * @param t
     * @param <T>
     */
    public <T> void addItemNotify(T t,int position) {
        if (t!=null && this.mItemList.add(t)) {
            notifyItemChanged(position);
        }
    }

    public <T> ArrayList getItemList(){
        return this.mItemList;
    }

    /**
     *
     notifyItemRemoved(position);//刷新被删除的地方
     notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据

     * @param position
     */
    public void remove(int position) {
        this.mItemList.remove(position);
        //通知演示插入动画
        notifyItemRemoved(position);

        // 如果移除的是最后一个，忽略
        if(position != mItemList.size()){
            notifyItemRangeChanged(position,this.mItemList.size()-position);
        }
    }

    /**
     *
     notifyItemRemoved(position);//刷新被删除的地方
     notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据

     * @param position
     */
    public void remove(int position,int toPosition) {

        for (int i = toPosition-1; i >= position; i--) {
            this.mItemList.remove(i);
        }
        //通知演示插入动画
        notifyItemMoved(position,toPosition);

        // 如果移除的是最后一个，忽略
        if(position != mItemList.size()){
            notifyItemRangeChanged(position,this.mItemList.size()-position);
        }


    }

    /**
     * 清空
     */
    public void clear() {
        isSelectPosition=0;
        mItemList.clear();
        notifyDataSetChanged();
    }

    /**
     * 清空 刷新
     */
    public void clearNotify() {
        isSelectPosition=0;
        mItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBaseBindViewHolder(holder,position);
    }

    public abstract void onBindItemViewHolder(VH holder, int position);

    @SuppressLint("CheckResult")
    public void onBaseBindViewHolder(final VH holder, final int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemSelectListener!=null) {
                    onItemSelectListener.onSelect(holder.itemView, position);
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });

        if(onItemLongClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }

        onBindItemViewHolder(holder,position);
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onBaseCreateViewHolder(parent,viewType);
    }

    public abstract int getLayoutResId();

    public abstract VH onCreateViewHolder(View view);

    public VH onBaseCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
        return onCreateViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (mItemList == null || mItemList.size() == 0) {
            return 0;
        } else {
            return mItemList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).hashCode();
    }

    public void addOnItemSelectListener(OnItemSelectListener onItemSelectListener){
        this.onItemSelectListener=onItemSelectListener;
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public void addOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener=onItemLongClickListener;
    }


    public interface OnItemSelectListener {
        void onSelect(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}

