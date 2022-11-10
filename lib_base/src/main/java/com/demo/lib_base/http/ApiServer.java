package com.demo.lib_base.http;


import com.demo.entity.home.UserEntity;
import com.demo.lib_base.entity.entity.SupportBaseEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xuan on 2017/5/10.
 */

public interface ApiServer {
    /*
        // POST请求实例

        单参数
        @FormUrlEncoded
        @POST(" news/attendanceRecordList")
        Observable<SupportBaseEntity<LiveDataEntity>> queryNewsData(@Field("key") String key);

        多参数
        @POST("news/attendanceRecordList")
        Observable<SupportBaseEntity<LiveDataEntity>> queryNewsData(@Body Entity entity);.

        public class Entity extends SupportBaseEntity{
            public String key;
            public String workerName;
            public int age;
            public int height;
        }
    */


    ////////////////////////////////////////////// 基础  /////////////////////////////////////////////////////

    @GET("news/attendanceRecordList")
    Observable<SupportBaseEntity<UserEntity>> queryUserData(@Query("key") String key);
}



