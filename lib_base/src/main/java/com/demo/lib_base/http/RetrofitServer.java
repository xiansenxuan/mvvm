package com.demo.lib_base.http;

import android.annotation.SuppressLint;
import android.util.Log;

import com.demo.lib_base.inter.SystemDefaultConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * com.hhd.workman.rx.http
 *
 * @author by xuan on 2018/8/7
 * @version [版本号, 2018/8/7]
 * @update by xuan on 2018/8/7
 * @descript
 */
public class RetrofitServer {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private OkHttpClient.Builder builder;
    private Retrofit.Builder retrofitBuilder;

    private Retrofit.Builder stringRetrofitBuilder;

    public RetrofitServer() {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
            builder.connectTimeout(ServicePropertyInter.default_timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(ServicePropertyInter.read_timeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(ServicePropertyInter.write_timeout, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
//                    .addInterceptor(new CacheRequestInterceptor())
//                     .addNetworkInterceptor(new CacheResponseInterceptor())
                    .addInterceptor(new HeadInterceptor());

            if (SystemDefaultConfig.isDebug) {
                builder.addInterceptor(new ServiceHttpLoggingInterceptor()
                        .setLevel(ServiceHttpLoggingInterceptor.Level.BODY)
                        .setMaxBufferSize(30 * 1024 * 1024));
            }
        }

//        int cacheSize = 200 * 1024 * 1024;
//        File cacheDirectory = new File(ConstantConfig.cachePath);
//        Cache cache = new Cache(cacheDirectory, cacheSize);

//        OkHttpClient client = builder.cache(cache).build();
//
        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                    .client(client);
        }

        if (stringRetrofitBuilder == null) {
            stringRetrofitBuilder = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client);
        }

    }

    private <T> T createApi(Class<T> clazz, String baseUrl) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }

    private <T> T createApi(Class<T> clazz, String baseUrl, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }

    /**
     * @param observable
     * @param consumer
     * @param throwable
     * @param lifecycleTransformer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    private <T> void toSubscribe(Observable<T> observable, Consumer<T> consumer, Consumer<Throwable> throwable, LifecycleTransformer lifecycleTransformer) {
        Disposable subscribe = observable.compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, throwable);

        mCompositeDisposable.add(subscribe);
    }

    public void putSubscribe(Disposable subscribe) {
        if (subscribe != null) {
            mCompositeDisposable.add(subscribe);
        }
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    private Observable toObservable(Observable observable, LifecycleTransformer lifecycleTransformer) {
        return observable.compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void cancelRequest() {
        Log.i(SystemDefaultConfig.RX_TAG, "cancelRequest");

        synchronized (mCompositeDisposable) {
            if (mCompositeDisposable.size() > 0) {
                mCompositeDisposable.clear();
            }
        }

    }


///////////////////////////////////////////////////////////////////////////////////////////


public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authorised = originalRequest.newBuilder()
                .addHeader(ServicePropertyInter.login_token, "")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .build();


        return chain.proceed(authorised);
    }
}


}
//class CacheResponseInterceptor implements Interceptor {
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        // 有网的情况下 30s 之内访问同一接口，会直接读缓存，不会重复访问
//        Request request = chain.request();
//        Response response = chain.proceed(request);
//
//        int maxAge = 60;

//    public Response intercept(Chain chain) throws IOException {
//        request //        return response.newBuilder()
//                .removeHeader("Pragma")
//                .removeHeader("Cache-Control")
//                // max-age 是一种特例，既包含缓存策略又包含缓存过期时间
//                .addHeader("Cache-Control", "max-age=" + maxAge)
//                .build();
//    }
//}
//
//class CacheRequestInterceptor implements Interceptor {
//    private Request request;
//
//    @SuppressLint("CheckResult")
//    @Override= chain.request();
//
//        ReactiveNetwork.observeInternetConnectivity()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override public void accept(Boolean isConnectedToInternet) {
//                        if(!isConnectedToInternet){
//                            // 直接读缓存
//                            request = request.newBuilder()
//                                    .cacheControl(CacheControl.FORCE_CACHE)
//                                    .build();
//                        }
//                    }
//                });
//
//        Response response = chain.proceed(request);
//        return response;
//    }
//}
