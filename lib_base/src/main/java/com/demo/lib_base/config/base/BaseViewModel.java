package com.demo.lib_base.config.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.demo.lib_base.http.ApiServer;
import com.demo.lib_base.http.RetrofitServer;
import com.demo.lib_base.http.ServicePropertyInter;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
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

    public void processingSeparationResult(Object o) {
        server.processingSeparationResult(o);
    }
}
