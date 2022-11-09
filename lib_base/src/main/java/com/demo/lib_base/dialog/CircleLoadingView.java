package com.demo.lib_base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.demo.lib_base.R;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.activity.BaseFragment;

/**
 * com.hhd.workman.widget.dotLoadingView
 *
 * @author by xuan on 2018/8/7
 * @version [版本号, 2018/8/7]
 * @update by xuan on 2018/8/7
 * @descript
 */
public class CircleLoadingView extends Dialog {

    private BaseFragment fragment;
    private Context context;
    private ProgressBar view;
    private boolean isBackCancel=true;

    public CircleLoadingView(Context context) {
        super(context, R.style.Widget_LoadingView);
        this.context = context;
        //设置dialog布局
        view = (ProgressBar) View.inflate(context, R.layout.loading_view, null);
        setContentView(view);

//        setCustomViewSize();
        initDialogSetting();
    }

    private void initDialogSetting() {
        setCanceledOnTouchOutside(false);
    }

    private void setCustomViewSize() {
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = dip2px(48);
        lp.width = dip2px(48);
        win.setAttributes(lp);
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if(isBackCancel){
            super.onBackPressed();
        }

        if(fragment!=null){
            fragment.cancelRequest();
        }else if(context instanceof BaseActivity){
            BaseActivity activity= (BaseActivity) context;
            if(activity!=null)
                activity.cancelRequest();
        }
    }

    @Override
    public void show() {
        if(context instanceof Activity){
            Activity activity= (Activity) context;
            if(!activity.isFinishing() && !isShowing()){
                super.show();
            }
        }else if(context instanceof Service){
            if(!isShowing()){
                super.show();
            }
        }else{
            if(!isShowing()){
                super.show();
            }
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void cancel() {
        if(isShowing()){
            super.cancel();
        }
    }


    /**
     * 清空dialog
     */
    public void cleanDrawable() {
        view.setIndeterminateDrawable(null);
        view.destroyDrawingCache();
    }

    /**
     * 如果是fragment含有请求 需要返回键取消请求 需要传递当前Fragment对象
     * @param fragment
     */
    public void allowBackCancelRequest(BaseFragment fragment) {
        this.fragment=fragment;
    }

    public void setType() {
        int overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        int alertWindow = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                overlay : alertWindow;
        getWindow().setType(type);
    }

    public void setIsBackCancel(boolean isBackCancel) {
        this.isBackCancel = isBackCancel;
    }
}
