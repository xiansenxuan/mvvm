package com.demo.lib_base.http;


import com.demo.lib_base.entity.entity.SupportBaseEntity;

import io.reactivex.functions.Function;

public class SeparationObjectResultFunction implements Function<SupportBaseEntity,Object> {
    // 0 失败
    // 1 成功
    // 2 重复
    // 11 重新登录


    @Override
    public Object apply(SupportBaseEntity tBaseEntity) throws Exception {
        if(tBaseEntity.success && tBaseEntity.code==1 ){
            if(tBaseEntity.data!=null){
                return tBaseEntity.data;
            }else{
                return new Object();
            }
        }else{
            throw  new SeparationResultException(tBaseEntity.success,tBaseEntity.code,tBaseEntity.msg);
        }
    }
}
