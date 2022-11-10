package com.demo.lib_base.http;

import android.annotation.SuppressLint;
import android.util.Log;

import com.demo.lib_base.constant.SystemDefaultConfig;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SeparationBaiduFaceDetectResultFunction implements Function<Object, Observable<Object>> {
    @SuppressLint("CheckResult")
    @Override
    public Observable<Object> apply(Object obj) throws Exception {
        Log.i(SystemDefaultConfig.RX_TAG,"flatMap SeparationEntityResultFunction");

        if(obj instanceof ErrorEntity){
            //表示在上一层查询出现非正常状况 需要特殊处理
            return Observable.just(obj);
        }

        return Observable.just(obj);
    }
}
