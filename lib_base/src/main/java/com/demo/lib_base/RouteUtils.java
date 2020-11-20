package com.demo.lib_base;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

public class RouteUtils {
    /**
     * 携带参数跳转到指定页面
     */
    public static void toPathWithBundle(String routerPath, Bundle bundle) {
        ARouter.getInstance().build(routerPath).with(bundle).navigation();
    }

    /**
     * main
     */
    public static void startMain() {
        ARouter.getInstance().build(RouteUrl.activity_main).navigation();
    }

    /**
     * home
     */
    public static void startHome(String str) {
        ARouter.getInstance().build(RouteUrl.activity_home).
                withString("str",str).
                navigation();
    }

    /**
     * msg
     */
    public static void startMsg() {
        ARouter.getInstance().build(RouteUrl.activity_msg).navigation();
    }

}
