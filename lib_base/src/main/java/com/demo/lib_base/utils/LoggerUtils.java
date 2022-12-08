package com.demo.lib_base.utils;

import com.demo.lib_base.constant.SystemDefaultConfig;
import com.elvishew.xlog.XLog;

import javax.annotation.Nullable;

/**
 * @author: xuan
 * @Description: com.galaxis.utils.util
 * @since: 2021/11/6 16:12
 */
public class LoggerUtils {
    public static void i(Object... args){
        XLog.tag(SystemDefaultConfig.LOG_TAG).i(args);
    }

    public static void e(Object... args){
        XLog.tag(SystemDefaultConfig.LOG_TAG).e(args);
    }

    public static void i(String tag, @Nullable Object... args){
        XLog.tag(tag).i(args);
    }

    public static void e(String tag, Object... args){
        XLog.tag(tag).e(args);
    }


}
