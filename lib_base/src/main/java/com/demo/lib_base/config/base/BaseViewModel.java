package com.demo.lib_base.config.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.demo.lib_base.constant.SystemDefaultConfig;
import com.demo.lib_base.http.ApiServer;
import com.demo.lib_base.http.RetrofitServer;
import com.demo.lib_base.http.SeparationThrowableResumeFunction;
import com.demo.lib_base.http.ServicePropertyInter;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author: wanglei
 * @Description: com.demo.lib_base.config.base
 * @since: 2020/11/27 16:33
 */
public abstract class BaseViewModel extends AndroidViewModel {
    protected RetrofitServer server=RetrofitServer.getInstance();
    protected ApiServer apiServer=server.createApi(ApiServer.class, ServicePropertyInter.BASE_URL);

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * @param observable apiServer.xxx
     * @param consumer
     * @param lifecycleTransformer
     */
    public void query(Observable observable, Consumer consumer, LifecycleTransformer lifecycleTransformer){
        server.processingNetwork(consumer
                , server.toObservable(observable,lifecycleTransformer)
        );
    }

    /**
     * @param observable apiServer.xxx
     * @param consumer
     * @param lifecycleTransformer
     */
    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    public void queryCustom(Observable observable, Consumer consumer, LifecycleTransformer lifecycleTransformer){
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i(SystemDefaultConfig.RX_TAG,"doOnSubscribe   ");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(SystemDefaultConfig.RX_TAG,"doFinally   ");
                    }
                })
                .onErrorResumeNext(new SeparationThrowableResumeFunction())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    public void processingSeparationResult(Object o) {
        server.processingSeparationResult(o);
    }
}
