package com.demo.lib_base.utils;

import android.text.TextUtils;

import java.text.NumberFormat;

/**
 * com.hhd.workman.utils
 *
 * @author by xuan on 2018/8/20
 * @version [版本号, 2018/8/20]
 * @update by xuan on 2018/8/20
 * @descript
 */
public class NumberFormatUtils {
    public static String keepTwoDecimalPlaces(float number) {
        //初始化数值格式化工具 小数点2位数
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        return nf.format(number);
    }

    public static String keepTwoDecimalPlaces(double number) {
        //初始化数值格式化工具 小数点2位数
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(number);
    }

    public static String keepTwoDecimalPlaces(String str) {
        if(TextUtils.isEmpty(str)){
            return "";
        }
        String format = String.format("%.2f ", Double.valueOf(str));
        return format;
    }
}
