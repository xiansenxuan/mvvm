package com.demo.lib_base.route;

import android.content.Intent;
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
        ARouter.getInstance().build(RouteUrl.activity_main)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                .withAction(Intent.ACTION_MAIN)
                .navigation();
    }

    /**
     * home
     */
    public static void startHome(String str) {
        ARouter.getInstance().build(RouteUrl.HomeActivity)
                .withString("str",str)
                .navigation();
    }

    /**
     * msg
     */
    public static void startMsg() {
        ARouter.getInstance().build(RouteUrl.activity_msg).navigation();
    }

    /**
     * tms
     */
    public static void startTmsFlatActivity() {
        ARouter.getInstance().build(RouteUrl.TmsFlatActivity).navigation();
    }

    public static void startLogin() {
        ARouter.getInstance().build(RouteUrl.LoginActivity).navigation();
    }

    public static void startStorage() {
        ARouter.getInstance().build(RouteUrl.StorageActivity).navigation();
    }

    public static void startMenu() {
        ARouter.getInstance().build(RouteUrl.MenuActivity).navigation();
    }
}
