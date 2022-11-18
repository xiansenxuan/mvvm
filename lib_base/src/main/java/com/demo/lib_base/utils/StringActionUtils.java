package com.demo.lib_base.utils;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.utils
 * @since: 2022/11/17 17:01
 */
public class StringActionUtils {
    public static String lineChange(String str){
        String[] batchs=str.split("");
        String newStr="";
        char c='\u200B';
        for (int i = 0; i < batchs.length; i++) {
            newStr+=batchs[i]+c;
        }
        return newStr;
    }
}
