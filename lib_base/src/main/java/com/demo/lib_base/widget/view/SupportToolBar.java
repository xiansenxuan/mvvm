package com.demo.lib_base.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.demo.lib_base.R;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.activity.BaseFragment;
import com.jakewharton.rxbinding3.view.RxView;

import java.lang.reflect.Field;

import io.reactivex.functions.Consumer;

/**
 * com.hhd.workman.widget.linearylayout
 *
 * @author by xuan on 2018/11/9
 * @version [版本号, 2018/11/9]
 * @update by xuan on 2018/11/9
 * @descript
 */
public class SupportToolBar extends LinearLayout {
    private int MATCH_PARENT= LayoutParams.MATCH_PARENT;
    private int WRAP_CONTENT= LayoutParams.WRAP_CONTENT;

    public LinearLayout contentLayout;
    public LinearLayout leftLayout;
    public LinearLayout middleLayout;
    public LinearLayout rightLayout;

    private TextView textView;
    private TextView rightTextView;
    private View lineView;

    private int textColor= R.color.base_white;
    private int toolbarBg=R.color.base_blue;
    private int defaultBackResId=R.mipmap.back_white;

    int actionBarSize;

    public SupportToolBar(Context context) {
        this(context,null);
    }

    public SupportToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SupportToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        setBackgroundResource(toolbarBg);

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,tv,true)){
            actionBarSize=TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }

        initView();
    }

    /**
     * 默认不调用此方法 ，背景色白色，字体黑色，返回键黑色
     * 调用此方法后，设置背景透明，字体白色，返回键白色
     */
    public void changeToolbarStyleTransparent(){
        textColor=R.color.base_white;
        toolbarBg=R.color.transparency;
        defaultBackResId=R.mipmap.back_white;
        hideLineView();

        setBackgroundResource(toolbarBg);
    }

    public void hideLineView(){
        lineView.setVisibility(GONE);
    }

    public void showLineView(){
        lineView.setVisibility(VISIBLE);
    }

    private void initView() {
        contentLayout=new LinearLayout(getContext());
        contentLayout.setOrientation(HORIZONTAL);
        LayoutParams contentParams=new LayoutParams(MATCH_PARENT,WRAP_CONTENT);
        contentLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        //contentLayout.setMinimumHeight(actionBarSize); layout布局指定了高度
        contentLayout.setLayoutParams(contentParams);

        leftLayout=new LinearLayout(getContext());
        leftLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams leftParams=new LayoutParams(0,WRAP_CONTENT);
        leftParams.weight=1;
        leftParams.gravity=Gravity.CENTER_VERTICAL;
        leftLayout.setLayoutParams(leftParams);

        middleLayout=new LinearLayout(getContext());
        middleLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams middleParams=new LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        middleParams.gravity=Gravity.CENTER;
        middleLayout.setLayoutParams(middleParams);

        rightLayout=new LinearLayout(getContext());
        rightLayout.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        LayoutParams rightParams=new LayoutParams(0,WRAP_CONTENT);
        rightParams.weight=1;
        rightParams.gravity=Gravity.CENTER_VERTICAL;
        rightLayout.setLayoutParams(rightParams);

        lineView=new View(getContext());
        LayoutParams lineParams=new LayoutParams(MATCH_PARENT,3);
        lineView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.line_color));
        lineView.setLayoutParams(lineParams);

        contentLayout.addView(leftLayout);
        contentLayout.addView(middleLayout);
        contentLayout.addView(rightLayout);

        addView(contentLayout);
        //addView(lineView);
    }

    private ImageView getImageButton(){
        return (ImageView) inflate(getContext(), R.layout.support_toolbar_image_button,null);
    }

    private TextView getTextView(){
        TextView view=(TextView) inflate(getContext(), R.layout.support_toolbar_text_view,null);
        view.setTextColor(ContextCompat.getColor(getContext(),textColor));
        return view;
    }

    public ImageView addLeftBackView(BaseActivity activity){
        ImageView imageView = getImageButton();
        imageView.setImageResource(defaultBackResId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftBackView(BaseActivity activity,OnRxClickListener inter){
        ImageView imageView = getImageButton();
        imageView.setImageResource(defaultBackResId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftBackView(BaseFragment fragment){
        ImageView imageView = getImageButton();
        imageView.setImageResource(defaultBackResId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onBackPressed();
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftBackView(BaseFragment fragment,OnRxClickListener inter){
        ImageView imageView = getImageButton();
        imageView.setImageResource(defaultBackResId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftBackView(OnRxClickListener inter){
        ImageView imageView = getImageButton();
        imageView.setImageResource(defaultBackResId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftCloseView(OnRxClickListener inter){
        ImageView imageView = getImageButton();
        imageView.setImageResource(R.mipmap.close_white);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null){
                    inter.onClick(v);
                }
            }
        });

        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftCloseView(BaseActivity activity){
        ImageView imageView = getImageButton();
        imageView.setImageResource(R.mipmap.close_white);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftCloseView(BaseFragment fragment){
        ImageView imageView = getImageButton();
        imageView.setImageResource(R.mipmap.close_white);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onBackPressed();
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftImageView(int resId){
        ImageView imageView = getImageButton();
        imageView.setImageResource(resId);
        leftLayout.addView(imageView);

        return imageView;
    }

    public ImageView addLeftImageView(int resId,OnRxClickListener inter){
        ImageView imageView = getImageButton();
        imageView.setImageResource(resId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        leftLayout.addView(imageView);

        return imageView;
    }

    public TextView addLeftTextView(String text){
        TextView textView = getTextView();
        textView.setText(text);
        leftLayout.addView(textView);

        return textView;
    }

    public TextView addLeftTextView(String text,OnRxClickListener inter){
        TextView textView = getTextView();
        textView.setText(text);

        onRxListener(textView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        leftLayout.addView(textView);

        return textView;
    }

    public TextView addMiddleTextView(String text) {
        middleLayout.removeView(textView);
        if (textView == null){
            textView = getTextView();
        }
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        textView.setText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setForeground(null);
        }
        // removeView(textView);
        middleLayout.addView(textView);

        return textView;
    }

    public TextView addMiddleTextView(int textResId) {
        TextView textView = getTextView();
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        textView.setText(textResId);

        middleLayout.addView(textView);

        return textView;
    }

    public TextView addMiddleTextView(String text,OnRxClickListener inter) {
        TextView textView = getTextView();
        textView.setText(text);
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        onRxListener(textView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        middleLayout.addView(textView);

        return textView;
    }
    public TextView addRightTextView(String text,int textColor,OnRxClickListener inter) {
        rightLayout.removeView(rightTextView);
        rightTextView = getTextView();
        rightTextView.setText(text);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        rightTextView.setTextColor(getResources().getColor(textColor));
        if ("".equals(text)){
            rightLayout.setVisibility(INVISIBLE);
        }else {
            rightLayout.addView(rightTextView);
            rightLayout.setVisibility(VISIBLE);
        }

        onRxListener(rightTextView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });



        return rightTextView;
    }

    public TextView addRightTextView(String text,OnRxClickListener inter) {
        rightLayout.removeView(rightTextView);
        rightTextView = getTextView();
        rightTextView.setText(text);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        if ("".equals(text)){
            rightLayout.setVisibility(INVISIBLE);
        }else {
            rightLayout.addView(rightTextView);
            rightLayout.setVisibility(VISIBLE);
        }

        onRxListener(rightTextView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });



        return rightTextView;
    }
    public ImageView addRightImageView(int resId,OnRxClickListener inter) {
        ImageView imageView = getImageButton();
        imageView.setImageResource(resId);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                if(inter!=null)
                    inter.onClick(v);
            }
        });
        rightLayout.addView(imageView);

        return imageView;
    }


    public ImageView addRightMenuView(int menuResId,OnMenuItemClickListener inter) {
        ImageView imageView = getImageButton();
        //使用系统图标 图标位置F:\adt-bundle-windows\sdk\platforms\android-21\data\res\drawable-xxhdpi
//        int resId= Resources.getSystem().getIdentifier("ic_menu_sort_by_size","drawable","android");
        imageView.setImageResource(R.mipmap.toolbar_menu_icon);

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(imageView,menuResId,inter);
            }
        });

        rightLayout.addView(imageView);

        return imageView;
    }

    public ImageView addRightMenuView(int menuResId,int iconResId,OnMenuItemClickListener inter) {
        ImageView imageView = getImageButton();
        //使用系统图标 图标位置F:\adt-bundle-windows\sdk\platforms\android-21\data\res\drawable-xxhdpi
//        int resId= Resources.getSystem().getIdentifier("ic_menu_sort_by_size","drawable","android");
        imageView.setImageResource(iconResId);

        LayoutParams params;
        if(imageView.getLayoutParams()==null){
            params=new LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        }else{
            params= (LayoutParams) imageView.getLayoutParams();
        }

        onRxListener(imageView, new OnRxClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(imageView,menuResId,inter);
            }
        });

        rightLayout.addView(imageView);

        return imageView;
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view, int menuResId, OnMenuItemClickListener inter) {

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(menuResId, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);
        //反射显示图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
            helper.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                if(inter!=null){
                    inter.onMenuItemClick(item);
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
                popupMenu.dismiss();
            }
        });
        popupMenu.show();
    }

    @SuppressLint("CheckResult")
    private void onRxListener(View view, OnRxClickListener inter){
        RxView.clicks(view)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if(inter!=null)
                            inter.onClick(view);

                    }
                });
    }

    public interface OnMenuItemClickListener{
        void onMenuItemClick(MenuItem item);
    }

    public interface OnRxClickListener {
        void onClick(View v);
    }
}
