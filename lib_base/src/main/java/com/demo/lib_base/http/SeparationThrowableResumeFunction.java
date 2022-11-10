package com.demo.lib_base.http;


import android.annotation.SuppressLint;
import android.util.Log;

import com.demo.lib_base.constant.SystemDefaultConfig;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SeparationThrowableResumeFunction implements Function<Throwable, Observable<Object>> {


    @SuppressLint("CheckResult")
    @Override
    public Observable<Object> apply(Throwable throwable) throws Exception {
        Log.i(SystemDefaultConfig.RX_TAG,"onErrorResumeNext SeparationThrowableResumeFunction");

        throwable.printStackTrace();

        if(throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException
                || throwable instanceof SocketException ){ // 可处理异常
            return Observable.just(new ErrorEntity(
                    ServiceCodeInter.time_out_code,
                    ServiceMessageInter.timeOutMessage));

        }else if(throwable instanceof ConnectException || throwable instanceof UnknownHostException ||
                throwable instanceof IOException){

            return Observable.just(new ErrorEntity(
                    ServiceCodeInter.disconnecting_code,
                    ServiceMessageInter.disconnectingMessage));

        }else if(throwable instanceof JsonSyntaxException){
            return Observable.just(new ErrorEntity(
                    ServiceCodeInter.json_syntax_code,
                    ServiceMessageInter.dataCannotProcessMessage));

        }else if(throwable instanceof retrofit2.adapter.rxjava2.HttpException){
            return Observable.just(new ErrorEntity(
                    ServiceCodeInter.lost_page_code,
                    ServiceMessageInter.lostPageMessage));

        }else if(throwable instanceof UnknownServiceException){
            return Observable.just(new ErrorEntity(
                    ServiceCodeInter.unknown_service_code,
                    ServiceMessageInter.unknownServiceMessage));

        }else if(throwable instanceof ServiceException){
            //自定义异常
            ServiceException serviceException= (ServiceException) throwable;
            return Observable.just(new ErrorEntity(serviceException.code,
                    serviceException.message));

        }else{ // 其他无法处理异常
            return Observable.error(throwable);
        }
    }

}
