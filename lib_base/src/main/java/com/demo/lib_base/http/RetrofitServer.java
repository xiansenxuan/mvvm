package com.demo.lib_base.http;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.app.MyApplication;
import com.demo.lib_base.dialog.CircleLoadingView;
import com.demo.lib_base.constant.SystemDefaultConfig;
import com.demo.lib_base.utils.LoggerUtils;
import com.demo.lib_base.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
    private CircleLoadingView loadingView;

    protected boolean isShowProgress=true;
    protected boolean isHideProgress=true;
    protected boolean isKeepAlive=false;

    public static RetrofitServer server;
    
    public static RetrofitServer getInstance() {
        if(server==null){
            synchronized (RetrofitServer.class){
                server=new RetrofitServer();
            }
        }
        return server;
    }
    //////////////////////////////

    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public OkHttpClient.Builder builder;
    public Retrofit.Builder retrofitBuilder;

    public Retrofit.Builder stringRetrofitBuilder;

    public RetrofitServer() {
        if (builder == null) {
            synchronized (RetrofitServer.class) {
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
            synchronized (RetrofitServer.class) {
                retrofitBuilder = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client);
            }
        }

        if (stringRetrofitBuilder == null) {
            synchronized (RetrofitServer.class) {
                stringRetrofitBuilder = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client);
            }
        }

    }

    public <T> T createApi(Class<T> clazz, String baseUrl) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }

    public <T> T createApi(Class<T> clazz, String baseUrl, Retrofit.Builder retrofitBuilder) {
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
    public <T> void toSubscribe(Observable<T> observable, Consumer<T> consumer, Consumer<Throwable> throwable, LifecycleTransformer lifecycleTransformer) {
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
    public Observable toObservable(Observable observable, LifecycleTransformer lifecycleTransformer) {
        return observable.compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void cancelRequest() {
        Log.i(SystemDefaultConfig.RX_TAG, "cancelRequest");

        if(!isKeepAlive){
            synchronized (mCompositeDisposable) {
                if (mCompositeDisposable.size() > 0) {
                    mCompositeDisposable.clear();
                }
            }
        }
    }

    /**
     * 获取保活状态
     * @return
     */
    public boolean getKeepAlive(){
        return isKeepAlive;
    }

    /**
     * @param isKeepAlive 保活 后台或者onPause不会cancel
     *                    需要保活的时候 传递true 并且
     *                    请求中请勿传递 lifecycleTransformer
     *                    否则后果自负
     */
    public void keepAlive(boolean isKeepAlive) {
        this.isKeepAlive=isKeepAlive;
    }
///////////////////////////////////////////////////////////////////////////////////////////

    public void processingSeparationResult(Object obj){
        if(obj instanceof ErrorEntity){
            LoggerUtils.i(SystemDefaultConfig.RX_TAG, " processingSeparationResult exception error   " + obj.toString());
            processingErrorEntity((ErrorEntity) obj);
        }else{//既不是需要的数据模型也不是服务器错误模型或者错误模型 判断json解析失败
            showMsg("数据解析错误");
        }
    }

    public void processingErrorEntity(ErrorEntity entity) {
        //显示错误信息
        showMsg(entity.errorMsg==null?"系统错误":entity.errorMsg);
        //处理错误状态
        if(entity.errorCode== ServiceCodeInter.reset_login_code){
            //重新登录
            resetLogin();
        }
    }

    public void showMsg(String msg) {
        ToastUtils.showDefaultToast(MyApplication.getInstance(),msg);
    }

    public void resetLogin() {
        showMsg("请重新登录");

        RouteUtils.startLogin();
    }

    public void showProgress() {
        if(loadingView!=null){
            loadingView.dismiss();
            loadingView.cleanDrawable();
            loadingView=null;
        }
        loadingView=new CircleLoadingView(MyApplication.getInstance().topActivity);
        loadingView.show();
    }

    public void hideProgress() {
        if(loadingView!=null){
            loadingView.dismiss();
            loadingView.cleanDrawable();
            loadingView=null;
        }
    }

    /**
     * 单请求
     * @param consumer
     * @param observable
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public void processingNetwork(Consumer<Object> consumer, Observable observable){
        putSubscribe(
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Log.i(SystemDefaultConfig.RX_TAG,"doOnSubscribe   ");
                                if(isShowProgress)
                                    showProgress();

                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.i(SystemDefaultConfig.RX_TAG,"doFinally   ");
                                if(isHideProgress){
                                    hideProgress();
                                }

                            }
                        })
                        .flatMap(new SeparationEntityResultFunction())
                        .onErrorResumeNext(new SeparationThrowableResumeFunction())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer)
        );
    }

    /**
     * 单请求
     * @param consumer
     * @param singleIsShowProgress 单独控制 是否需要显示进度条 不受全局isShowProgress影响
     * @param observable
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public void processingNetwork(Consumer<Object> consumer, boolean singleIsShowProgress, Observable observable){
        putSubscribe(
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Log.i(SystemDefaultConfig.RX_TAG,"doOnSubscribe   ");

                                if(singleIsShowProgress)
                                    showProgress();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.i(SystemDefaultConfig.RX_TAG,"doFinally   ");

                                hideProgress();
                            }
                        })
                        .flatMap(new SeparationEntityResultFunction())
                        .onErrorResumeNext(new SeparationThrowableResumeFunction())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer)
        );
    }

    /**
     * 多请求合并查询 并行
     * @param consumer
     * @param observables
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public void processingNetwork(Consumer<Object> consumer, Observable... observables){
        if(observables!=null && observables.length>0){
            putSubscribe(
                    Observable.merge(new Iterable() {
                        @NonNull
                        @Override
                        public Iterator iterator() {
                            return Arrays.asList(observables).iterator();
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    Log.i(SystemDefaultConfig.RX_TAG,"doOnSubscribe   ");

                                    if(isShowProgress)
                                        showProgress();
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {
                                    Log.i(SystemDefaultConfig.RX_TAG,"doFinally   ");

                                    hideProgress();
                                }
                            })
                            .flatMap(new SeparationEntityResultFunction())
                            .onErrorResumeNext(new SeparationThrowableResumeFunction())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(consumer)
            );
        }

    }

    /**
     * 多请求合并查询 串行
     * @param consumer
     * @param observables
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    public void processingConcatNetwork(Consumer<Object> consumer, Observable... observables){
        if(observables!=null && observables.length>0){
            putSubscribe(
                    Observable.concat(new Iterable() {
                        @NonNull
                        @Override
                        public Iterator iterator() {
                            return Arrays.asList(observables).iterator();
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    Log.i(SystemDefaultConfig.RX_TAG,"doOnSubscribe   ");

                                    if(isShowProgress)
                                        showProgress();
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {
                                    Log.i(SystemDefaultConfig.RX_TAG,"doFinally   ");

                                    hideProgress();
                                }
                            })
                            .flatMap(new SeparationEntityResultFunction())
                            .onErrorResumeNext(new SeparationThrowableResumeFunction())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(consumer)
            );
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
//    public Request request;
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
