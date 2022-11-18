package com.demo.lib_base.utils;

import android.text.TextUtils;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.utils
 * @since: 2022/11/18 15:15
 */
public class RulesUtils {
    public static boolean isRack(String text){
        if(TextUtils.isEmpty(text)){
            return false;
        }
        if(text.contains("RACK")){
            return true;
        }
        return  false;
    }
}
