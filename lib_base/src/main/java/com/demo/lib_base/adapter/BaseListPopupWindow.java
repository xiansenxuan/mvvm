package com.demo.lib_base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.lib_base.inter.SystemDefaultConfig;
import com.galaxis.framework.R;
import com.galaxis.framework.bean.PopListEntity;
import com.galaxis.utils.constant.SystemConstants;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * @author: xuan
 * @Description: com.galaxis.framework.base.adapter
 * @since: 2021/11/2 11:05
 */
public class BaseListPopupWindow extends ListPopupWindow {
    private ArrayList<PopListEntity> mItemList = new ArrayList<>();
    private LayoutInflater mInflater;
    private MyAdapter adapter;
    
    public BaseListPopupWindow(@NonNull Context context) {
        this(context,null);
    }

    public BaseListPopupWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseListPopupWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BaseListPopupWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mInflater= LayoutInflater.from(context);
    }

    public MyAdapter addAdapter(OnItemClickListener listener) {
        adapter=new MyAdapter();
        setAdapter(adapter);

        if(listener!=null){
            adapter.addOnItemClickListener(listener);
        }

        return adapter;
    }

    @SuppressLint("CheckResult")
    @Override
    public void setAnchorView(@Nullable View anchor) {
        super.setAnchorView(anchor);
    }

    @SuppressLint("CheckResult")
    public void setAnchorView(@Nullable View anchor, OnDataCallBack callBack) {
        this.setAnchorView(anchor);

        this.callBack=callBack;

        RxView.clicks(anchor)
                .throttleFirst(SystemConstants.throttleFirstTime, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        if(!mItemList.isEmpty()){
                            show();
                        }else{
                            if(callBack!=null){
                                isQuery=true;
                                callBack.queryData();
                            }
                        }

                    }
                });
    }

    @SuppressLint("CheckResult")
    public void setAnchorView(@Nullable View anchor, @Nullable View  clickView,OnDataCallBack callBack) {
        this.setAnchorView(anchor);

        this.callBack=callBack;

        RxView.clicks(clickView)
                .throttleFirst(SystemDefaultConfig.quick_click_time, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        if(!mItemList.isEmpty()){
                            show();
                        }else{
                            if(callBack!=null){
                                isQuery=true;
                                callBack.queryData();
                            }
                        }

                    }
                });
    }

    @Override
    public void show() {
        if(!isShowing()){
            super.show();
        }else{
            dismiss();
        }
    }

    private boolean isQuery;

    private OnDataCallBack callBack;

    public interface OnDataCallBack{
        void queryData();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyAdapter extends BaseAdapter{

        protected OnItemClickListener onItemClickListener;

        public void addOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public int getCount() {
            if (mItemList == null || mItemList.size() == 0) {
                return 0;
            } else {
                return mItemList.size();
            }
        }

        @Override
        public PopListEntity getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mItemList.get(position).hashCode();
        }

        @SuppressLint("CheckResult")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.pop_list_item, null);
                viewHolder.tv = (TextView) convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.tv.setText(getItem(position).value);

            RxView.clicks(viewHolder.tv)
                    .throttleFirst(SystemDefaultConfig.quick_click_time, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) throws Exception {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(viewHolder.tv, position);
                            }
                        }
                    });

            return convertView;
        }

        public class ViewHolder {
            public TextView tv;
        }

        /**
         * 插入一组数据 刷新
         * @param itemList
         * @param <T>
         */
        public <T> void addItemNotify(List<PopListEntity> itemList) {
            if (itemList!=null) {
                mItemList.addAll(itemList);
                notifyDataSetChanged();
            }

            if(isQuery){
                show();
            }
            isQuery=false;
        }

        public <T> ArrayList getItemList(){
            return mItemList;
        }

        /**
         * 清空 刷新
         */
        public void clearNotify() {
            mItemList.clear();
            notifyDataSetChanged();
        }
    }
}
