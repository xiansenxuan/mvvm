package com.demo.lib_base.app;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.lib_base.clash.CrashHandler;
import com.demo.lib_base.constant.SystemDefaultConfig;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.naming.FileNameGenerator;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MyApplication extends Application  implements ViewModelStoreOwner, ComponentCallbacks2 {
    /**
     * 实例化
     */
    private static MyApplication instance;

    //activity引用
    private List<Activity> activityList;
    //前台显示activity数量
    private int foregroundActivityNumber=0;
    public FragmentActivity topActivity;
    //app是否在后台
    private boolean isRunInBackground=true;
    private RunInBackgroundListener listener;

    public interface RunInBackgroundListener{
        void onFront();
        void onBackstage();
    }

    public void addRunInBackgroundListener(RunInBackgroundListener listener){
        this.listener=listener;
    }

    private ViewModelStore mAppViewModelStore;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        mAppViewModelStore = new ViewModelStore();

        initRouter();
        initLogger();
        initCrashException();
        registerActivityLife();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mAppViewModelStore;
    }

    private void initRouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (SystemDefaultConfig.isDebug) {
            //打印日志
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！
            //线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }

    public boolean isDebug(){
        return true;
    }

    public static String getFileName() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(DateTime.now().getMillis());
        int minute=instance.get(Calendar.MINUTE);
        int points=minute/10*10;
        return DateTime.now().toString("yyyy_MM_dd HH")+"_"+points;
    }

    public static boolean isSDCardEnableByEnvironment() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private String getLogPath(String crashDirPath) {
        String dirPath;
        if (TextUtils.isEmpty((crashDirPath))){
            if (isSDCardEnableByEnvironment()
                    &&getExternalFilesDir(null) != null) {
                dirPath = getExternalFilesDir(null) + SystemDefaultConfig.FILE_SEP + "log" + SystemDefaultConfig.FILE_SEP;
            } else {
                dirPath = getFilesDir() + SystemDefaultConfig.FILE_SEP + "log" + SystemDefaultConfig.FILE_SEP;
            }
        } else {
            dirPath = crashDirPath.endsWith(SystemDefaultConfig.FILE_SEP) ? crashDirPath : crashDirPath + SystemDefaultConfig.FILE_SEP;
        }
        return dirPath;
    }

    private void initCrashException() {
        CrashHandler.getInstance().init(this);
    }

    private void initLogger() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(isDebug() ? LogLevel.ALL             // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                        : LogLevel.NONE)
                .tag(SystemDefaultConfig.LIFE_TAG)                                         // 指定 TAG，默认为 "X-LOG"
                .build();

        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
                .Builder(getLogPath(""))                             // 指定保存日志文件的路径
                .fileNameGenerator(new FileNameGenerator() {
                    @Override
                    public boolean isFileNameChangeable() {
                        return true;
                    }

                    @Override
                    public String generateFileName(int logLevel, long timestamp) {
                        return getFileName()+".txt";
                    }
                })        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(new NeverBackupStrategy())             // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(new FileLastModifiedCleanStrategy(7L*24*60*60*1000))
                .flattener(new ClassicFlattener())                     // Default: DefaultFlattener
                .build();

        XLog.init(                                                 // 初始化 XLog
                config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                new AndroidPrinter(),
                filePrinter);

    }


    private void registerActivityLife() {
        activityList= Collections.synchronizedList(new ArrayList<Activity>());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                activityList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivityNumber+=1;

                if(activity instanceof FragmentActivity){
                    topActivity= (FragmentActivity) activity;
                }/*else if(activity instanceof MainActivity){
                    mainActivity= (MainActivity) activity;
                }else{
                    //第三方activity 不保存在栈顶
                    //thirdActivity=activity;
                }*/


                if(isRunInBackground){
                    //进入前台
                    if(listener!=null) {
                        listener.onFront();
                    }

                    isRunInBackground=false;
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                foregroundActivityNumber-=1;

                if(foregroundActivityNumber==0){
                    //进入后台
                    if(listener!=null) {
                        listener.onBackstage();
                    }

                    isRunInBackground=true;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });
    }

    public int getForegroundActivityNumber(){
        return foregroundActivityNumber;
    }

    private void initDir() {
        new File(SystemDefaultConfig.cameraPath).mkdirs();
        new File(SystemDefaultConfig.cachePath).mkdirs();
        new File(SystemDefaultConfig.downloadPath).mkdirs();
        new File(SystemDefaultConfig.logPath).mkdirs();
        new File(SystemDefaultConfig.cameraPath).mkdirs();
        new File(SystemDefaultConfig.videoPath).mkdirs();
        new File(SystemDefaultConfig.audioPath).mkdirs();
        new File(SystemDefaultConfig.compressImagePath).mkdirs();
    }

/*
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }*/
}
