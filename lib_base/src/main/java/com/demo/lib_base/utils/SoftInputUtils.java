package com.demo.lib_base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.lib_base.R;

import java.lang.reflect.Field;

/**
 * com.hhd.workman.utils
 *
 * @author by Administrator on 2019/3/13 0013
 * @version [版本号, 2019/3/13 0013]
 * @update by Administrator on 2019/3/13 0013
 * @descript 输入框和键盘管理类
 */
public class SoftInputUtils {
    /**
     * 设置输入框指针颜色样式
     * @param et
     */
    public static void setEditTextCursorDrawable(EditText et){
        try {
            @SuppressLint("SoonBlockedPrivateApi") Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
            setCursor.setAccessible(true);
            setCursor.set(et, R.drawable.cursor_search);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftInputFromWindow(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInputFromWindow(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        //imm.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideSoftInputFromWindow(Activity activity){
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if(imm.isActive()&&activity.getCurrentFocus()!=null){
            //拿到view的token 不为空
            if (activity.getCurrentFocus().getWindowToken()!=null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showSoftInputFromWindow(Activity activity){
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if(imm.isActive()&&activity.getCurrentFocus()!=null){
            //拿到view的token 不为空
            if (activity.getCurrentFocus().getWindowToken()!=null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
