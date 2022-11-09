package com.demo.lib_base.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.lib_base.Constant;
import com.demo.lib_base.clash.CrashHandler;
import com.demo.lib_base.inter.SystemDefaultConfig;
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

import java.util.Calendar;

public class MyApplication extends Application  implements ViewModelStoreOwner, ComponentCallbacks2 {
    /**
     * 实例化
     */
    private static MyApplication instance;


    private ViewModelStore mAppViewModelStore;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        mAppViewModelStore = new ViewModelStore();

        initRouter();
        initLogger();
        initCrashException();
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
        if (Constant.isDebug) {
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
                dirPath = getExternalFilesDir(null) + Constant.FILE_SEP + "log" + Constant.FILE_SEP;
            } else {
                dirPath = getFilesDir() + Constant.FILE_SEP + "log" + Constant.FILE_SEP;
            }
        } else {
            dirPath = crashDirPath.endsWith(Constant.FILE_SEP) ? crashDirPath : crashDirPath + Constant.FILE_SEP;
        }
        return dirPath;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
