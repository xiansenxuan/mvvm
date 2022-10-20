package com.demo.lib_base.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.demo.lib_base.inter.SystemDefaultConfig;
import com.jakewharton.rxbinding3.view.RxView;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * com.hhd.workman.utils
 *
 * @author by Administrator on 2019/3/13 0013
 * @version [版本号, 2019/3/13 0013]
 * @update by Administrator on 2019/3/13 0013
 * @descript
 */
public class OnClickUtils {
    public static final String clickInterval="clickInterval";

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public static void setOnClickListener(View.OnClickListener listener, View... views){
        if(views!=null && views.length>0){
            for (int i = 0; i < views.length; i++) {
                int index = i;
                RxView.clicks(views[i])
                        .throttleFirst(SystemDefaultConfig.quick_click_time, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Log.i(clickInterval,"clickInterval "+ DateTime.now().getMillis());

                                if (listener != null) {
                                    listener.onClick(views[index]);
                                }
                            }
                        });
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public static void setOnClickListener(View.OnClickListener listener, View view){
        RxView.clicks(view)
                .throttleFirst(SystemDefaultConfig.quick_click_time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i(clickInterval,"clickInterval "+ DateTime.now().getMillis());

                        if (listener != null) {
                            listener.onClick(view);
                        }
                    }
                });
    }


}
