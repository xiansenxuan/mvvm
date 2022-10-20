package com.demo.lib_base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by xuan on 2017/8/19.
 *
 * This step is optional, but if you want you can configure some Toasty parameters. Place this anywhere in your app:

     Toasty.Config.getInstance()
     .setErrorColor(@ColorInt int errorColor) // optional
     .setInfoColor(@ColorInt int infoColor) // optional
     .setSuccessColor(@ColorInt int successColor) // optional
     .setWarningColor(@ColorInt int warningColor) // optional
     .setTextColor(@ColorInt int textColor) // optional
     .tintIcon(boolean tintIcon) // optional (apply textColor also to the icon)
     .setToastTypeface(@NonNull Typeface typeface) // optional
     .setTextSize(int sizeInSp) // optional
     .apply(); // required
     You can reset the configuration by using reset() method:

     Toasty.Config.reset();


     Each method always returns a Toast object, so you can customize the Toast much more. DON'T FORGET THE show() METHOD!

     To display an error Toast:

     Toasty.error(yourContext, "This is an error toast.", Toast.LENGTH_SHORT, true).show();
     To display a success Toast:

     Toasty.success(yourContext, "Success!", Toast.LENGTH_SHORT, true).show();
     To display an info Toast:

     Toasty.info(yourContext, "Here is some info for you.", Toast.LENGTH_SHORT, true).show();
     To display a warning Toast:

     Toasty.warning(yourContext, "Beware of the dog.", Toast.LENGTH_SHORT, true).show();
     To display the usual Toast:

     Toasty.normal(yourContext, "Normal toast w/o icon").show();
     To display the usual Toast with icon:

     Toasty.normal(yourContext, "Normal toast w/ icon", yourIconDrawable).show();
     You can also create your custom Toasts with the custom() method:

     Toasty.custom(yourContext, "I'm a custom Toast", yourIconDrawable, tintColor, duration, withIcon,
     shouldTint).show();

 */

public class ToastUtils {

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Activity context, String msg){
        Toasty.Config.getInstance().setTextSize(13).apply();

            if (Looper.myLooper() != Looper.getMainLooper()) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.normal(context,msg, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toasty.normal(context,msg,Toast.LENGTH_SHORT).show();
            }
    }

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Activity context, int resId){
        Toasty.Config.getInstance().setTextSize(13).apply();

        if (Looper.myLooper() != Looper.getMainLooper()) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.normal(context,context.getString(resId), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toasty.normal(context,context.getString(resId),Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Activity context, int resId,int errorCode){
        Toasty.Config.getInstance().setTextSize(13).apply();

        if (Looper.myLooper() != Looper.getMainLooper()) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.normal(context,context.getString(resId), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toasty.normal(context,context.getString(resId)+":"+String.valueOf(errorCode),Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Activity context, String msg, int length){
        Toasty.Config.getInstance().setTextSize(15).apply();

            if (Looper.myLooper() != Looper.getMainLooper()) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.normal(context,msg, length).show();
                    }
                });
            } else {
                Toasty.normal(context,msg,length).show();
            }
    }

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Context context, String msg){
        Toasty.Config.getInstance().setTextSize(15).apply();

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toasty.normal(context,msg,Toast.LENGTH_SHORT).show();
            }
    }

    @SuppressLint("CheckResult")
    public static void showDefaultToast(Context context, String msg, int length){
        Toasty.Config.getInstance().setTextSize(15).apply();

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toasty.normal(context, msg, length).show();
            }
    }

}
