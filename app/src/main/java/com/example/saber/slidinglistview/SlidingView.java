package com.example.saber.slidinglistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by saber on 2017/7/25.
 */

public class SlidingView extends ViewGroup {

    private static final String TAG = "SlidingView";

    //手指点击下去的坐标
    private int mDownX;
    private int mDownY;

    //手指移动时所处的位置
    private int mMoveX;
    private int mMoveY;

    //手指上次滑动的位置
    private int mLastX;
    private int mLastY;

    //布局Item
    private ViewGroup mListItem;
    //布局item的长度
    private int mItemLength;
    private int mLimit;

    //布局item中的内容
    private ViewGroup mContent;
    //布局item中侧滑时出现的menu
    private View mMenu;
    //屏幕的宽度
    private int mScreenWidth;
    //menu的宽度
    private int mMenuWidth;
    //是否展开
    public boolean isOpen;

    private Scroller scroller;
    //判定为拖动的最小移动像素数
    private int mTouchSlop;

    public SlidingView(Context context) {
        super(context,null);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        //获取屏幕宽度
        mScreenWidth = displayMetrics.widthPixels;

        //创建Scroller实例
        scroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();

    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mItemLength = 0;//需要重置，measure会执行多次
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //测量子元素
        int childCount = getChildCount();
        int childHeight = 0;
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility() == View.GONE){
                continue;
            }
            if(i == 0){
                measureChild(childView,widthMeasureSpec,heightMeasureSpec);
                childHeight = childView.getMeasuredHeight();
            }else {
                int heightSpec = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY);
                measureChild(childView,widthMeasureSpec,heightSpec);
            }
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            if(i > 0){
                mItemLength += childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            mLimit = (int) (mItemLength * 0.45);
            setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : getChildAt(0).getMeasuredWidth(),
                    heightMode == MeasureSpec.EXACTLY ? heightSize : getChildAt(0).getMeasuredHeight());
        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //为子View布局定位
        int childCount = getChildCount();
        int leftOffset = getPaddingLeft();
        int topOffset = getPaddingTop();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            leftOffset += lp.leftMargin;
            topOffset +=lp.topMargin;
            int measureWidth = childView.getMeasuredWidth();
            int measureHeight = childView.getMeasuredHeight();
            childView.layout(leftOffset,topOffset,leftOffset+measureWidth,topOffset+measureHeight);
            leftOffset += measureWidth + lp.rightMargin;
            topOffset = getPaddingTop();
        }

        //获取菜单的宽度
        mMenu = getChildAt(1);
        mMenuWidth = mMenu.getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean consume = false;
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()){

            case MotionEvent.ACTION_DOWN:
                //获取手指按下时的坐标
                mDownX = (int) e.getX();
                mDownY = (int) e.getY();
                Log.d(TAG, "mDownX:"+ mDownX);
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onInterceptTouchEvent:ACTION_MOVE");
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                if(Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop){
                    //当y方向滑动距离小于x方向时，拦截事件，交给自己处理
                    if(Math.abs(deltaX) > Math.abs(deltaY)){
                        Log.d(TAG, "onInterceptTouchEvent");
                        //不允许父元素拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        //此时拦截事件，直接调用onTouchEvent，传入onTouchEvent的ACTION_MOVE事件中，不再走onTouchEvent的ACTION_DOWN事件
                        consume = true;
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                break;
        }
        mLastX = x;
        mLastY = y;
        return consume;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) e.getX();
                mMoveY = (int) e.getY();
                Log.d(TAG, "mMoveX - mDownX:"+ (mMoveX - mDownX));

                scrollBy(-(mMoveX - mDownX),0);

                int scrollX = getScrollX();
                if (scrollX > mItemLength) {
                    scrollTo(mItemLength, 0);
                    isOpen = true;
                }
                if (scrollX < 0) {
                    scrollTo(0, 0);
                    isOpen = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                //滑动超过一定距离时，自动关闭或开启menu
                int upX = (int) getX();
                if(Math.abs(getScrollX())>= mMenuWidth && getScrollX() > 0 || Math.abs(getScrollX())< mMenuWidth && getScrollX() < 0){
                    open();
                }else if(Math.abs(getScrollX())>= mMenuWidth && getScrollX() < 0 || Math.abs(getScrollX())< mMenuWidth && getScrollX() > 0){
                    close();
                }
                break;
        }
        mDownX = mMoveX;
        mDownY = mMoveY;
        return true;
    }

    public void open(){
        Log.d(TAG,"OPEN");
        smoothScrollTo(mItemLength,0);
        isOpen = true;
    }

    public void close(){
        smoothScrollTo(0,0);
        isOpen = false;
    }

    public void smoothScrollTo(int destX,int destY){
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //5000ms内滑向destX
        scroller.startScroll(scrollX,0,delta,0,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }
}
