package com.demo.lib_base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
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
            Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
            setCursor.setAccessible(true);
            setCursor.set(et, R.drawable.cursor_search);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftInputFromWindow(Context context, IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //切换软键盘的显示与隐藏
        imm.hideSoftInputFromWindow(windowToken, 0);
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
}
