package com.galaxis.basic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.demo.lib_base.config.base.BaseViewModel;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.viewmodel
 * @since: 2021/3/2 14:09
 */
public class BasicViewModel extends BaseViewModel {
    public MutableLiveData<String> list=new MutableLiveData<>();

    public BasicViewModel(@NonNull Application application) {
        super(application);
    }

    public void requestData(Consumer consumer,LifecycleTransformer lifecycleTransformer){

    }

    public void queryStorage(Consumer consumer,LifecycleTransformer lifecycleTransformer) {
        queryCustom(Observable.just(1,2,3),consumer,lifecycleTransformer);
    }
}
