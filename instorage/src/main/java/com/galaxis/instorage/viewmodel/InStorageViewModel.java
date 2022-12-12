package com.galaxis.instorage.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.demo.lib_base.config.base.BaseViewModel;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.viewmodel 入库公用model
 * @since: 2021/3/2 14:09
 *
 */
public class InStorageViewModel extends BaseViewModel {
    public MutableLiveData<String> list=new MutableLiveData<>();

    public InStorageViewModel(@NonNull Application application) {
        super(application);
    }

    public void requestData(Consumer consumer,LifecycleTransformer lifecycleTransformer){
        query(apiServer.queryUserData("1"),consumer,lifecycleTransformer);
    }

    public ArrayList<String> refreshData(){
        ArrayList<String> list =new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add("第"+i+"行号");
        }

        return list;
    }

}
