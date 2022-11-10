package com.demo.lib_base.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.demo.lib_base.R;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.widget.edit
 * @since: 2022/11/10 17:39
 */
public class SuperEditText extends AppCompatEditText implements TextWatcher{

    private static final String TAG = SuperEditText.class.getSimpleName();

    private Drawable mDrawable;
    private int mIconWidth;
    private boolean mDisableKeyboard;
    private boolean mDisableBackgroundFrame;

    public SuperEditText(Context context) {
        this(context, null);
    }

    public SuperEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperEditText);
        if (typedArray != null) {
            typedArray.recycle();
        }
        setIcon();
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
        if (!mDisableBackgroundFrame) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_edittext_select));
        }
        setGravity(Gravity.CENTER_VERTICAL);
    }


    public void setIcon() {
        mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_white_48dp);

        // 设置图标的位置以及大小,getIntrinsicWidth()获取显示出来的大小而不是原图片的带小
        mIconWidth = (int)getResources().getDimension(R.dimen.dp_22);
        mDrawable.setBounds(0, 0, mIconWidth, mIconWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int pixelSize = getResources().getDimensionPixelSize(R.dimen.dp_4);
        setPadding(pixelSize, pixelSize, pixelSize, pixelSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isEnabled()) {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Just ignore the [Enter] key
            return true;
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 - 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 -
     * 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mDisableKeyboard) {
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 点击右边键盘图标
            if (event.getX() > (getWidth() - mIconWidth)) {
                if (getCompoundDrawables()[2] != null) {
                    return true;
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setShowSoftInputOnFocus(false);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，设置清除图标的显示与隐藏
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (mDisableKeyboard) {
            return;
        }
        if (focused) {
            setClearIconVisible(true);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
    }

    /**
     * 晃动动画
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        // 防止不能多选
        if (selStart == selEnd) {
            // 判空，防止出现空指针
            if (getText() == null) {
                setSelection(0);
            } else {
                // 保证光标始终在最后面
                setSelection(getText().length());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
