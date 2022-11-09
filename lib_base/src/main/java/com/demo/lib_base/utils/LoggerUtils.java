package com.demo.lib_base.utils;

import com.elvishew.xlog.XLog;

/**
 * @author: xuan
 * @Description: com.galaxis.utils.util
 * @since: 2021/11/6 16:12
 */
public class LoggerUtils {
    public static void i(Object... args){
        XLog.i(args);
    }
    public static void e(Object... args){
        XLog.e(args);
    }
    public static void i(String tag, Object... args){
        XLog.tag(tag).i(args);
    }
    public static void e(String tag, Object... args){
        XLog.tag(tag).e(args);
    }
}
