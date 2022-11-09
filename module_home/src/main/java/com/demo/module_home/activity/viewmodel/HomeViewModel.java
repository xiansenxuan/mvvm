package com.demo.module_home.activity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.demo.lib_base.config.base.BaseViewModel;

import java.util.ArrayList;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.viewmodel
 * @since: 2021/3/2 14:09
 */
public class HomeViewModel extends BaseViewModel {

    MutableLiveData<String> list=new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    private void refreshData(){
        ArrayList<String> list =new ArrayList<>();



    }

}
