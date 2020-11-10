package com.demo.lib_base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class MyApplication extends Application {
    /**
     * 上下文
     */
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initRouter(this);

    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initRouter(MyApplication mApplication) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (Constant.isDebug) {
            //打印日志
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！
            //线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(mApplication);

    }
}
