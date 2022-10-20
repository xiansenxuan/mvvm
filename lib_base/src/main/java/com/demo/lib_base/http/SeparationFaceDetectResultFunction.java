package com.demo.lib_base.http;


import com.demo.lib_base.RouteUtils;
import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.entity.entity.SupportBaseEntity;
import com.demo.lib_base.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SeparationFaceDetectResultFunction implements Function<SupportBaseEntity, Observable<Object>> {
    // 0 失败
    // 1 成功
    // 2 重复
    // 11 重新登录

    @Override
    public Observable<Object> apply(SupportBaseEntity tBaseEntity) throws Exception {
        if(tBaseEntity!=null && tBaseEntity.success && tBaseEntity.code==1 && tBaseEntity.data!=null){
            return Observable.just(tBaseEntity.data);
        }
        if(tBaseEntity!=null && tBaseEntity.code==11){
            RouteUtils.startLogin();
            ToastUtils.showDefaultToast(MyApplication.getInstance().getApplicationContext(),"请重新登录");

        }
        if(tBaseEntity!=null && tBaseEntity.code==0){
            ToastUtils.showDefaultToast(MyApplication.getInstance().getApplicationContext(),tBaseEntity.msg);
        }
        return Observable.empty();
    }
}
