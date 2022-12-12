package com.demo.lib_base.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.activity.BaseFragment;
import com.demo.lib_base.adapter.BaseRecyclerViewAdapter;
import com.demo.lib_base.inter.OnScanCallback;
import com.xuan.view.interfaces.OnLoadMoreListener;
import com.xuan.view.interfaces.OnRefreshListener;
import com.xuan.view.recyclerview.LRecyclerView;
import com.xuan.view.recyclerview.LRecyclerViewAdapter;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.utils
 * @since: 2022/11/11 15:30
 */
public class ViewUtils {

    public static LRecyclerViewAdapter addRecyclerViewAdapter(LRecyclerView recyclerView, BaseRecyclerViewAdapter adapter){
        LRecyclerViewAdapter lAdapter=new LRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(lAdapter);
        return lAdapter;
    }

    public static void addOnRefreshListener(BaseActivity activity,LRecyclerView recyclerView, BaseRecyclerViewAdapter adapter){
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.isSelectPosition=0;

                activity.onRefresh();

                recyclerView.refreshCompleteDelayed(2000);
            }
        });
    }

    public static void addOnLoadMoreListener(BaseActivity activity,LRecyclerView recyclerView){
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                activity.onLoadMore();

                recyclerView.refreshCompleteDelayed(2000);
            }
        });
    }

    public static void addOnRefreshListener(BaseFragment fragment, LRecyclerView recyclerView, BaseRecyclerViewAdapter adapter){
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.isSelectPosition=0;

                fragment.onRefresh();

                recyclerView.refreshCompleteDelayed(2000);
            }
        });
    }

    public static void addOnLoadMoreListener(BaseFragment fragment,LRecyclerView recyclerView){
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fragment.onLoadMore();

                recyclerView.refreshCompleteDelayed(2000);
            }
        });
    }

    public static void addOnScanListener(EditText et, OnScanCallback callback){
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(callback!=null){
                        callback.onScan(et.getText().toString().trim().replaceAll("\\s*|\r|\n|\t",""));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    // 扫描
    public static void addOnScanNumberListener(EditText editText, OnScanCallback callback) {
        editText.addTextChangedListener(new TextWatcher() {
            String beforeStr="";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    if(s.toString().trim().matches("^-?\\d*\\.?\\d*$")){
                        beforeStr=s.toString().trim();
                    }else{
                        editText.setText(beforeStr);
                    }
                }
            }
        });

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN) {
                if(callback!=null){
                    callback.onScan(editText.getText().toString().trim());
                    return true;
                }
            }
            return false;
        });
    }
    
    public static void requestFocus(EditText v) {
        if(v!=null){
            v.postDelayed(v::requestFocus, 260);
        }
    }
    
    public static void requestClearEditText(EditText editText) {
        if(editText!=null){
            requestFocus(editText);
            editText.getText().clear();
        }
    }

    public static void clearEditText(EditText ... list){
        for (EditText editText : list) {
            if(editText!=null){
                editText.getText().clear();
            }
        }
    }
    
}
