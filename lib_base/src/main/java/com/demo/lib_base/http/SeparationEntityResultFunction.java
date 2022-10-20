package com.demo.lib_base.http;


import android.annotation.SuppressLint;

import com.demo.lib_base.entity.entity.BaseEntity;
import com.demo.lib_base.entity.entity.SupportBaseEntity;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SeparationEntityResultFunction implements Function<SupportBaseEntity, Observable<Object>> {
    @SuppressLint("CheckResult")
    @Override
    public Observable<Object> apply(SupportBaseEntity tBaseEntity) throws Exception {
        //赋值
        if(tBaseEntity.data instanceof BaseEntity){
            BaseEntity baseEntity= (BaseEntity) tBaseEntity.data;
            baseEntity.code=tBaseEntity.code;
            baseEntity.msg=tBaseEntity.msg;
            baseEntity.success=tBaseEntity.success;
        }

        if(tBaseEntity!=null && tBaseEntity.success ){
            if(tBaseEntity.data==null) { //数据源为空
                return Observable.error(
                        new ServiceException(
                                ServiceMessageInter.dataNullMessage,
                                ServiceCodeInter.data_null));
            } else {
                if (tBaseEntity.code == ServiceCodeInter.success) { //正常
                    return Observable.just(tBaseEntity.data);

                } else { //有数据但是处理不了
                    return Observable.error(new ServiceException(
                            ServiceMessageInter.dataCannotProcessMessage,
                            ServiceCodeInter.data_cannot_process));
                }
            }

        } else if(tBaseEntity!=null && !tBaseEntity.success ){
            if(tBaseEntity.code!=ServiceCodeInter.success){ // 服务器错误
                return Observable.error(new ServiceException(tBaseEntity.msg,
                        tBaseEntity.code));

            }else{
                return Observable.error(new ServiceException(
                        ServiceMessageInter.unknownErrorMessage,
                        ServiceCodeInter.unknown_error_code
                ));
            }
        } else{ //其他无法处理的错误 没有返回任何数据或正确的数据或其他情况
            return Observable.error(new ServiceException(
                    ServiceMessageInter.unknownErrorMessage,
                    ServiceCodeInter.unknown_error_code
            ));

        }
    }
}
