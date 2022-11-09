package com.github.jdsjlzx.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.jdsjlzx.R;
import com.github.jdsjlzx.interfaces.IRefreshFooter;
import com.github.jdsjlzx.interfaces.IRefreshHeader;
import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.WeakHandler;

import java.util.Date;


public class ArrowRefreshFooter extends LinearLayout implements IRefreshFooter {

    private LinearLayout mContainer;
    //private ImageView mArrowImageView;
    private SimpleViewSwitcher mProgressBar;
    private TextView mStatusTextView;
    private TextView mHeaderTimeView;

    private static final int ROTATE_ANIM_DURATION = 180;

    public int mMeasuredHeight;
    public int top;
    private int hintColor;
    private State mState = State.Normal;
    private String changeTabPrepareHint = "继续上拉切换到下一个tab";
    private String changeTabHint = "释放切换到下一个tab";
    private int style;
    private String loadingHint;
    private String noMoreHint;


    private int indicatorColor;
    private WeakHandler mHandler = new WeakHandler();

    public ArrowRefreshFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public ArrowRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_recyclerview_refresh_footer, null);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 10));
        setGravity(Gravity.TOP);

        //mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
        mStatusTextView = (TextView)findViewById(R.id.refresh_status_textview);

        //init the progress view
        mProgressBar = (SimpleViewSwitcher)findViewById(R.id.loading_progressbar);
        mProgressBar.setView(initIndicatorView(ProgressStyle.BallPulse));

        mHeaderTimeView = (TextView)findViewById(R.id.last_refresh_time);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
        top = getTop();
        hintColor = android.R.color.darker_gray;
        indicatorColor = ContextCompat.getColor(getContext(), R.color.colorGray);
        //style = ProgressStyle.BallPulse;
    }

    private View initIndicatorView(int style) {
        if (style == ProgressStyle.SysProgress) {
            return new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        } else {
            AVLoadingIndicatorView progressView = (AVLoadingIndicatorView) LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_view, null);
            progressView.setIndicatorId(style);
            progressView.setIndicatorColor(indicatorColor);
            return progressView;
        }
    }

    public void setChangeTabHint(String hint) {
        this.changeTabHint = hint;
    }
    public void setChangeTabPrepareHint(String hint) {
        this.changeTabPrepareHint = hint;
    }

    public void setHintTextColor(int color) {
        this.hintColor = color;
    }


    /**
     * 设置状态
     *
     * @param status
     */
    public void setState(State status) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {
            case Normal:
                setOnClickListener(null);
               //mContainer.setVisibility(GONE);
                setVisibleHeight(10);
                break;
            case Loading:
                setOnClickListener(null);
                setVisibleHeight(mMeasuredHeight);
                mContainer.setVisibility(VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.removeAllViews();
                mProgressBar.addView(initIndicatorView(style));
                mStatusTextView.setText(TextUtils.isEmpty(loadingHint) ? getResources().getString(R.string.list_footer_loading) : loadingHint);
                mStatusTextView.setTextColor(ContextCompat.getColor(getContext(), hintColor));

                break;
            case NoMore:
                setOnClickListener(null);
                mStatusTextView.setText(changeTabPrepareHint);
                mProgressBar.setVisibility(GONE);
               /* setVisibleHeight(mMeasuredHeight);
                mContainer.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mStatusTextView.setVisibility(VISIBLE);
                mStatusTextView.setText(TextUtils.isEmpty(noMoreHint) ? getResources().getString(R.string.list_footer_end) : noMoreHint);
                mStatusTextView.setTextColor(ContextCompat.getColor(getContext(), hintColor));*/
                break;
            case ChangeTabPrepare:
                setOnClickListener(null);
                mContainer.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mStatusTextView.setVisibility(VISIBLE);
                mStatusTextView.setText(changeTabPrepareHint);
                mStatusTextView.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            case ChangeTab:
                setOnClickListener(null);
                mContainer.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mStatusTextView.setVisibility(VISIBLE);
                mStatusTextView.setText(changeTabHint);
                mStatusTextView.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            case ChangeReset:
                setOnClickListener(null);
                mContainer.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }


    public enum State {
        Normal/**正常*/
        , NoMore/**加载到最底了*/
        , Loading/**加载中..*/
        , NetWorkError/**网络异常*/
        ,ChangeTabPrepare/** 准备下拉切换下一Tab*/
        ,ChangeTab /** 切换下一个tab */
        ,ChangeReset /** 还原 */
    }



    public State getState() {
        return mState;
    }

    @Override
    public void onLoading() {
        setState(State.Loading);
    }

    @Override
    public void onComplete() {
        setState(State.Normal);
    }

    @Override
    public void onNoMore() {
        setState(State.NoMore);
    }

    @Override
    public View getFootView() {
        return this;
    }


    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public void onPrepare() {
        setState(State.ChangeTabPrepare);
    }

    @Override
    public void onChanging() {
        setState(State.ChangeTab);
    }

    @Override
    public void onMove(float offSet, float sumOffSet) {
        int top = getTop();
        //Log.e("zhgj",offSet+"ddd"+sumOffSet);
        if (offSet < 0 ){
            setVisibleHeight((int) Math.abs(offSet) + getVisibleHeight());
        }else if (offSet > 0 && getVisibleHeight() > 0){
            layout(getLeft(), top, getRight(), getHeight()); //重新布局让header显示在顶端
            setVisibleHeight((int) (0-offSet) + getVisibleHeight());
        }
        //mStatusTextView.setText(changeTabPrepareHint);
        //Log.e("zhgj",getVisibleHeight()+"getVisibleHeight"+mState.toString()+mMeasuredHeight+"mMeasuredHeight");
       if (getVisibleHeight() <= mMeasuredHeight && (mState ==State.NoMore || mState==State.ChangeTab)) {
            onPrepare();
        }
        else if(getVisibleHeight() > mMeasuredHeight && mState!=State.NoMore){
            onChanging();
        }
        else if (getVisibleHeight() <= mMeasuredHeight){
            if(State.ChangeTab == mState )
            {
                onPrepare();
            }

        }
    }

    @Override
    public boolean onRelease() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {// not visible.
            isOnRefresh = false;
        }

        if(height> mMeasuredHeight &&  mState ==State.ChangeTab){
            //setState(State.ChangeTab);
            isOnRefresh = true;
           // smoothScrollTo(mMeasuredHeight);
        }
        // refreshing and header isn't shown fully. do nothing.
        /*if (mState == State.ChangeTab && height > mMeasuredHeight) {
            smoothScrollTo(mMeasuredHeight);
        }*/
        smoothScrollTo(0);
        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(top);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mStatusTextView.setText(changeTabPrepareHint);
        }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), 10);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("zhgj","getVisibleHeight"+animation.getAnimatedValue());
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

}