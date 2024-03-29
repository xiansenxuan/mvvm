package com.xuan.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.xuan.view.interfaces.IRefreshFooter;
import com.xuan.view.interfaces.IRefreshHeader;
import com.xuan.view.interfaces.OnLoadMoreListener;
import com.xuan.view.interfaces.OnRefreshListener;
import com.xuan.view.view.ArrowRefreshFooter;
import com.xuan.view.view.ArrowRefreshHeader;

/**
 *
 * @author lizhixian
 * @created 2016/8/29 11:21
 *
 */
public class LRecyclerViewFooter extends RecyclerView {
    private boolean mPullRefreshEnabled = true;
    private boolean mLoadMoreEnabled = true;
    private boolean mRefreshing = false;//是否正在下拉刷新
    private boolean mLoadingData = false;//是否正在加载数据
    private boolean mChageTabing = false;//是否正在加载数据
    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;
    private LScrollListener mLScrollListener;
    private OnBottomListener mOnBottomListener;
    private boolean isOnBottom = false;//默认底部
    private IRefreshHeader mRefreshHeader;
    private IRefreshFooter mRefreshFooter;
    private View mEmptyView;

    private final AdapterDataObserver mDataObserver = new DataObserver();
    private int mActivePointerId;
    private float mLastY = -1;
    private float sumOffSet;
    private static final float DRAG_RATE = 2.0f;
    private int mPageSize = 20; //一次网络请求默认数量

    private LRecyclerViewAdapter mWrapAdapter;
    private boolean mHaveNextPage = false;
    private boolean mIsVpDragger;
    private int mTouchSlop;
    private float startY;
    private float startX;
    private boolean isRegisterDataObserver;
    //scroll variables begin
    /**
     * 当前RecyclerView类型
     */
    protected LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    /**
     * 触发在上下滑动监听器的容差距离
     */
    private static final int HIDE_THRESHOLD = 20;

    /**
     * 滑动的距离
     */
    private int mDistance = 0;

    /**
     * 是否需要监听控制
     */
    private boolean mIsScrollDown = true;

    /**
     * Y轴移动的实际距离（最顶部为0）
     */
    private int mScrolledYDistance = 0;

    /**
     * X轴移动的实际距离（最左侧为0）
     */
    private int mScrolledXDistance = 0;
    //scroll variables end


    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public LRecyclerViewFooter(Context context) {
        this(context, null);
    }

    public LRecyclerViewFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerViewFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext().getApplicationContext()).getScaledTouchSlop();
        if (mPullRefreshEnabled) {
            setRefreshHeader(new ArrowRefreshHeader(getContext().getApplicationContext()));
        }

        if (mLoadMoreEnabled) {
            setLoadMoreFooter(new ArrowRefreshFooter(getContext().getApplicationContext()));
        }
    }


    /**
     * 设置自定义的footerview
     */
    public void setLoadMoreFooter(IRefreshFooter iRefreshFooter) {
        this.mRefreshFooter = iRefreshFooter;
       // mFootView = loadMoreFooter.getFootView();
       // mFootView.setVisibility(GONE);
        mRefreshFooter.getFootView().setVisibility(GONE);
        //wxm:mFootView inflate的时候没有以RecyclerView为parent，所以要设置LayoutParams
        /*ViewGroup.LayoutParams layoutParams = mFootView.getLayoutParams();
        if (layoutParams != null) {
            mFootView.setLayoutParams(new LayoutParams(layoutParams));
        } else {
            mFootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }*/
    }

    @Override
    public void setAdapter(Adapter adapter) {
        LRecyclerViewFooter.this.setVisibility(View.GONE);

        if (mWrapAdapter != null && mDataObserver != null && isRegisterDataObserver) {
            mWrapAdapter.getInnerAdapter().unregisterAdapterDataObserver(mDataObserver);
        }

        mWrapAdapter = (LRecyclerViewAdapter) adapter;
        super.setAdapter(mWrapAdapter);

        mWrapAdapter.getInnerAdapter().registerAdapterDataObserver(mDataObserver);
        //指定必须手动添加数据去刷新 初始化不刷新
//        mDataObserver.onChanged();
        isRegisterDataObserver = true;

        mWrapAdapter.setRefreshHeader(mRefreshHeader);

        //fix bug: https://github.com/jdsjlzx/LRecyclerView/issues/115
        if (mLoadMoreEnabled && mWrapAdapter.getFooterViewsCount()==0) {
            mWrapAdapter.addFooterView(mRefreshFooter.getFootView());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mWrapAdapter != null && mDataObserver != null && isRegisterDataObserver) {
            mWrapAdapter.getInnerAdapter().unregisterAdapterDataObserver(mDataObserver);
            isRegisterDataObserver = false;
        }

    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter instanceof LRecyclerViewAdapter) {
                LRecyclerViewAdapter lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
                if (lRecyclerViewAdapter.getInnerAdapter() != null) {
                    int count = lRecyclerViewAdapter.getInnerAdapter().getItemCount();
                    if (count == 0) {
                        if(mEmptyView != null)
                            mEmptyView.setVisibility(View.VISIBLE);
                        LRecyclerViewFooter.this.setVisibility(View.GONE);
                    } else {
                        if(mEmptyView != null)
                            mEmptyView.setVisibility(View.GONE);
                        LRecyclerViewFooter.this.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (adapter != null && mEmptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        if(mEmptyView != null)
                            mEmptyView.setVisibility(View.VISIBLE);
                        LRecyclerViewFooter.this.setVisibility(View.GONE);
                    } else {
                        if(mEmptyView != null)
                            mEmptyView.setVisibility(View.GONE);
                        LRecyclerViewFooter.this.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
                if(mWrapAdapter.getInnerAdapter().getItemCount() < mPageSize ) {
                    mRefreshFooter.getFootView().setVisibility(GONE);
                }
            }

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart + mWrapAdapter.getHeaderViewsCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart + mWrapAdapter.getHeaderViewsCount() + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart + mWrapAdapter.getHeaderViewsCount() + 1, itemCount);
            if(mWrapAdapter.getInnerAdapter().getItemCount() < mPageSize ) {
                mRefreshFooter.getFootView().setVisibility(GONE);
            }

        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int headerViewsCountCount = mWrapAdapter.getHeaderViewsCount();
            mWrapAdapter.notifyItemRangeChanged(fromPosition + headerViewsCountCount + 1, toPosition + headerViewsCountCount + 1 + itemCount);
        }

    }

    /**
     * 解决嵌套RecyclerView滑动冲突问题
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getY();
            mActivePointerId = ev.getPointerId(0);
            sumOffSet = 0;

        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                sumOffSet = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                mLastY = (int) ev.getY(index);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    pointerIndex = 0;
                    mActivePointerId = ev.getPointerId(pointerIndex);
                }
                final int moveY = (int) ev.getY(pointerIndex);
                final float deltaY = (moveY - mLastY) / DRAG_RATE;
                mLastY = moveY;
                sumOffSet += deltaY;
                if (isOnTop() && mPullRefreshEnabled && !mRefreshing && (appbarState == AppBarStateChangeListener.State.EXPANDED)) {
                    if(mRefreshHeader.getType() == IRefreshHeader.TYPE_HEADER_NORMAL) {
                        mRefreshHeader.onMove(deltaY, sumOffSet);
                    }else if (mRefreshHeader.getType() == IRefreshHeader.TYPE_HEADER_MATERIAL) {
                         if(deltaY > 0 && !canScrollVertically(-1) || deltaY < 0 && !canScrollVertically(1)) { //判断无法下拉和无法上拉（item过少的情况）
                          overScrollBy(0, (int) -deltaY, 0, 0, 0, 0, 0, (int) sumOffSet, true);
                        }
                    }
                }
                if(isOnBottom() && mLoadMoreEnabled && !mHaveNextPage && isOnBottom && isChangeTab)
                {
                    mRefreshFooter.getFootView().setVisibility(VISIBLE);
                    mRefreshFooter.onMove(deltaY*DRAG_RATE, sumOffSet);
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1; // reset
                mActivePointerId = -1;
                if (isOnTop() && mPullRefreshEnabled && !mRefreshing/*&& appbarState == AppBarStateChangeListener.State.EXPANDED*/) {
                    if (mRefreshHeader != null && mRefreshHeader.onRelease()) {
                        if (mRefreshListener != null) {
                            mRefreshing = true;
                            //mRefreshFooter.getFootView().setVisibility(GONE);
                            mRefreshListener.onRefresh();
                        }
                    }
                }
                if (isOnBottom() && !mHaveNextPage && !mChageTabing && isChangeTab) {
                    if (mRefreshFooter != null && mRefreshFooter.onRelease()) {
                        if (mOnBottomListener != null) {
                            mChageTabing = true;
                            mRefreshFooter.getFootView().setVisibility(GONE);
                            //执行切换
                            mOnBottomListener.onBottom();
                            //状态初始化
                            mChageTabing = false;
                        }
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX,
        int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY != 0 && isTouchEvent) {
            mRefreshHeader.onMove(deltaY, sumOffSet);
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public boolean isOnTop() {
        return  mPullRefreshEnabled && (mRefreshHeader.getHeaderView().getParent() != null);
    }

    public boolean isOnBottom() {
        return  mLoadMoreEnabled && (mRefreshFooter.getFootView().getParent() != null);
    }

    /**
     * set view when no content item
     *
     * @param emptyView visiable view when items is empty
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        //默认隐藏
        if(mEmptyView!=null){
            mEmptyView.setVisibility(GONE);
        }
//        mDataObserver.onChanged();
    }

    /**
     *
     * @param pageSize 一页加载的数量
     */
    public void refreshComplete(int pageSize) {
        this.mPageSize = pageSize;
        if (mRefreshing) {
            mHaveNextPage = true;
            mRefreshing = false;
            mRefreshHeader.refreshComplete();

            if(mWrapAdapter.getInnerAdapter().getItemCount() < pageSize) {
                mRefreshFooter.getFootView().setVisibility(GONE);
            }
        } else if (mLoadingData) {
            mLoadingData = false;
            mRefreshFooter.onComplete();
        }

    }

    public void refreshComplete() {

        if (mRefreshing) {
            /*mHaveNextPage = true;*/
            mRefreshing = false;
            mChageTabing = false;
            mRefreshHeader.refreshComplete();
            mRefreshFooter.getFootView().setVisibility(VISIBLE);
           /* if(mWrapAdapter.getInnerAdapter().getItemCount() < mPageSize) {
                mFootView.setVisibility(GONE);
            }*/
        } else if (mLoadingData) {
            mLoadingData = false;
            mRefreshFooter.onComplete();
            mRefreshFooter.getFootView().setVisibility(VISIBLE);
        }

    }

    /**
     *
     * @param pageSize 一页加载的数量
     * @param delayed  延迟刷新
     */
    public void refreshCompleteDelayed(final int pageSize, int delayed) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                LRecyclerViewFooter.this.mPageSize = pageSize;

                if (mRefreshing) {
                    mHaveNextPage = true;
                    mRefreshing = false;
                    mRefreshHeader.refreshComplete();

                    if(mWrapAdapter.getInnerAdapter().getItemCount() < pageSize) {
                        mRefreshFooter.getFootView().setVisibility(GONE);
                    }
                } else if (mLoadingData) {
                    mLoadingData = false;
                    mRefreshFooter.onComplete();
                }
            }
        },delayed <= 0 ? 1 : delayed);
    }

    /**
     *
     * @param delayed  延迟刷新
     */
    public void refreshCompleteDelayed(int delayed) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRefreshing) {
                    mHaveNextPage = true;
                    mRefreshing = false;
                    mRefreshHeader.refreshComplete();

                    if(mWrapAdapter.getInnerAdapter().getItemCount() < mPageSize) {
                        mRefreshFooter.getFootView().setVisibility(GONE);
                    }
                } else if (mLoadingData) {
                    mLoadingData = false;
                    mRefreshFooter.onComplete();
                }
            }
        },delayed <= 0 ? 1 : delayed);
    }

    /**
     * 设置是否还有下一页数据
     */
    public void setHaveNextPage(boolean haveNextPage){
        mLoadingData = false;
        mHaveNextPage = haveNextPage;
        if(!mHaveNextPage) {
            mRefreshFooter.onNoMore();
            mRefreshFooter.getFootView().setVisibility(GONE);
        } else {
            mRefreshFooter.onComplete();
           // mRefreshFooter.getFootView().setVisibility(GONE);
        }
    }

    /**
     * 设置自定义的RefreshHeader
     * 注意：setRefreshHeader方法必须在setAdapter方法之前调用才能生效
     */
    public void setRefreshHeader(IRefreshHeader refreshHeader) {
        if(isRegisterDataObserver){
            throw new RuntimeException("setRefreshHeader must been invoked before setting the adapter.");
        }
        this.mRefreshHeader = refreshHeader;
    }



    public void setPullRefreshEnabled(boolean enabled) {
        mPullRefreshEnabled = enabled;
    }

    /**
     * 到底加载是否可用
     */
    public void setLoadMoreEnabled(boolean enabled) {
        if(mWrapAdapter == null){
            throw new NullPointerException("LRecyclerViewAdapter cannot be null, please make sure the variable mWrapAdapter have been initialized.");
        }
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            if (null != mWrapAdapter) {
                mWrapAdapter.removeFooterView();
            } else {
                mRefreshFooter.onComplete();
            }
        }
    }



    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }



    public void setLScrollListener(LScrollListener listener) {
        mLScrollListener = listener;
    }

    public interface LScrollListener {

        void onScrollUp();//scroll down to up

        void onScrollDown();//scroll from up to down

        void onScrolled(int distanceX, int distanceY);// moving state,you can get the move distance

        void onScrollStateChanged(int state);
    }

    public interface OnBottomListener {
        void onBottom();
    }

    public void setOnBottomListener(OnBottomListener listener) {
        mOnBottomListener = listener;
    }


    public void refresh() {
        if (mRefreshing) {// if RefreshHeader is Refreshing, return
            return;
        }
        if (mPullRefreshEnabled && mRefreshListener != null) {
            mRefreshHeader.onRefreshing();
            int offSet = mRefreshHeader.getHeaderView().getMeasuredHeight();
            mRefreshHeader.onMove(offSet,offSet);
            mRefreshing = true;

            mRefreshFooter.getFootView().setVisibility(GONE);
            mRefreshListener.onRefresh();
        }
    }

    public void forceToRefresh() {
        if (mLoadingData) {
            return;
        }
        refresh();
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        int firstVisibleItemPosition = 0;
        LayoutManager layoutManager = getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(lastPositions);
                firstVisibleItemPosition = findMax(lastPositions);
                break;
        }

        // 根据类型来计算出第一个可见的item的位置，由此判断是否触发到底部的监听器
        // 计算并判断当前是向上滑动还是向下滑动
        calculateScrollUpOrDown(firstVisibleItemPosition, dy);
        // 移动距离超过一定的范围，我们监听就没有啥实际的意义了
        mScrolledXDistance += dx;
        mScrolledYDistance += dy;
        mScrolledXDistance = (mScrolledXDistance < 0) ? 0 : mScrolledXDistance;
        mScrolledYDistance = (mScrolledYDistance < 0) ? 0 : mScrolledYDistance;
        if (mIsScrollDown && (dy == 0)) {
            mScrolledYDistance = 0;
        }
        //Be careful in here
        if (null != mLScrollListener) {
            mLScrollListener.onScrolled(mScrolledXDistance, mScrolledYDistance);
        }

        if (mLoadMoreListener != null && mLoadMoreEnabled) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if (visibleItemCount > 0
                    && lastVisibleItemPosition >= totalItemCount - 1
                    && totalItemCount > visibleItemCount
                    && mHaveNextPage
                    && !mRefreshing) {

                onLoadMore();

            }

        }
        if (isOnTop() && dy > 0 && mRefreshHeader.getType() == IRefreshHeader.TYPE_HEADER_MATERIAL && !mRefreshing && (appbarState
            == AppBarStateChangeListener.State.EXPANDED)) {
            mRefreshHeader.onMove(dy, mScrolledYDistance);
        }
    }

    public void onLoadMore(){
        mRefreshFooter.getFootView().setVisibility(View.VISIBLE);
        if (!mLoadingData) {
            mLoadingData = true;
            mRefreshFooter.onLoading();
            mLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        currentScrollState = state;

        if (mLScrollListener != null) {
            mLScrollListener.onScrollStateChanged(state);
        }

        LayoutManager layoutManager = getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0  && (lastVisibleItemPosition) >= totalItemCount - 2)) {
            isOnBottom = true;
        }
        else
        {
            isOnBottom = false;
        }
    }


    /**
     * 计算当前是向上滑动还是向下滑动
     */
    private void calculateScrollUpOrDown(int firstVisibleItemPosition, int dy) {
        if (null != mLScrollListener) {
            if (firstVisibleItemPosition == 0) {
                if (!mIsScrollDown) {
                    mIsScrollDown = true;
                    mLScrollListener.onScrollDown();
                }
            } else {
                if (mDistance > HIDE_THRESHOLD && mIsScrollDown) {
                    mIsScrollDown = false;
                    mLScrollListener.onScrollUp();
                    mDistance = 0;
                } else if (mDistance < -HIDE_THRESHOLD && !mIsScrollDown) {
                    mIsScrollDown = true;
                    mLScrollListener.onScrollDown();
                    mDistance = 0;
                }
            }
        }

        if ((mIsScrollDown && dy > 0) || (!mIsScrollDown && dy < 0)) {
            mDistance += dy;
        }
    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决LRecyclerView与CollapsingToolbarLayout滑动冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if(null != p && p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if(child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout)child;
                    break;
                }
            }
            if(appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    public void setChangeTabHintText(String changeTabPrepareHint,String changeTabHint)
    {
        if (mRefreshFooter!= null && mRefreshFooter.getFootView()!=null)
            ((ArrowRefreshFooter)mRefreshFooter.getFootView()).setChangeTabHint(changeTabHint);
            ((ArrowRefreshFooter)mRefreshFooter.getFootView()).setChangeTabPrepareHint(changeTabPrepareHint);
    }

    //默认不支持
    private boolean isChangeTab =false;
    public void setIsChangeTab(boolean isChangeTab)
    {
        this.isChangeTab = isChangeTab;
    }
}
