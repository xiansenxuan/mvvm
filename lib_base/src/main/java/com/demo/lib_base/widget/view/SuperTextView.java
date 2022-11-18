package com.demo.lib_base.widget.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.demo.lib_base.utils.DisplayUtil;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.widget.text
 * @since: 2022/11/10 17:50
 */
public class SuperTextView extends AppCompatTextView {

    public SuperTextView(Context context) {
        this(context, null);
    }

    public SuperTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //setBackground(null); 默认有下划线背景（???）
        setMinHeight(DisplayUtil.dip2px(context, 26));
    }
}
