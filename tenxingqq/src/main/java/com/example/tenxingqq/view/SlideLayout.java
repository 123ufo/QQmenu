package com.example.tenxingqq.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 作者：xudiwei
 * <p/>
 * 日期： Administrator on 2015/9/17.
 * <p/>
 * 文本描述：侧滑菜单
 */
public class SlideLayout extends FrameLayout {

    private MyCallBack myCallBack;
    private ViewDragHelper mViewDragHelper;
    private View menuContainer;
    private View mainContainer;
    private int menuContainerMeasuredWidth;
    private int menuContainerMeasuredHeight;
    private int mainContainerMeasuredWidth;
    private int mainRange;
    private int mainContainerMeasuredHeight;
    private OnDragStateChangedListener listener;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        myCallBack = new MyCallBack();
        mViewDragHelper = ViewDragHelper.create(this, myCallBack);

        Scroller scroller = new Scroller(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            //把当前的ViewGroup的触模事件交给ViewDragHelper处理
            mViewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        menuContainerMeasuredWidth = menuContainer.getMeasuredWidth();
        menuContainerMeasuredHeight = menuContainer.getMeasuredHeight();

        mainContainerMeasuredWidth = mainContainer.getMeasuredWidth();
        mainContainerMeasuredHeight = mainContainer.getMeasuredHeight();
        //主界面可滑动的范围
        mainRange = (int) (mainContainerMeasuredWidth * 0.6);
    }

    @Override
    protected void onFinishInflate() {
        if (this.getChildCount() != 2) {
            throw new IllegalStateException("CN:子控件必须要两个，EN: must two child ViewGroun");
        }
        if (!(this.getChildAt(0) instanceof ViewGroup && this.getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("CN:子控件必须是ViewGroup的子类，EN:child view must instanceof ViewGroup");
        }

        menuContainer = getChildAt(0);
        mainContainer = getChildAt(1);

    }

    /**
     * 回调方法
     */
    class MyCallBack extends ViewDragHelper.Callback {

        /**
         * 第一个被调用
         * 用于决定被捕获的view 返回true表示事件被捕获，返回false表示不被捕获，面其它的方法都不会被回调
         *
         * @param child     当前事件把捕获的view
         * @param pointerId 当前view上有几个手指按下
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
//            System.out.println("trycaptureview:" + pointerId);
            return true;
        }

        /**
         * 第二个被调用
         * 当 {@link #tryCaptureView(View child, int pointerId)} 返回true时就会回调这个方法
         *
         * @param capturedChild   当前被事件被获取的view
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
        }

        /**
         * 第三个被调用
         * 当View的事件。的Drag状态改变时调用
         * STATE_IDLE      空闲状态
         * STATE_DRAGGING   拖动状态
         * STATE_SETTLING    自动状态（也就是当手指松开时View还在滑动）
         *
         * @param state
         */
        @Override
        public void onViewDragStateChanged(int state) {
//            System.out.println("onviewdragstatechanged");
            super.onViewDragStateChanged(state);
        }

        /**
         * 第四个被调用
         * 用于决定当前事件被捕获的view拖动的范围 当回调此方法时view还没真正的移动。而onViewPositionChanged()
         * 被调用时才是真正的移动
         * child.getLeft() + dx = left;
         *
         * @param child
         * @param left
         * @param dx
         * @return 返回可拖动的范围，如果返回left表示。不受限制.如果返回0表示不允许拖动
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mainContainer) {
                return fixRange(left);
            }
            return left;
        }

        /**
         * 第五个被调用
         * 当view移动 后被调用
         *
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            System.out.println("onviewpositionchanged");
            if (changedView == menuContainer) {
                menuContainer.layout(0, 0, menuContainerMeasuredWidth, menuContainerMeasuredHeight);
                int oldMainLeft = mainContainer.getLeft() + dx;
                oldMainLeft = fixRange(oldMainLeft);
                mainContainer.layout(oldMainLeft, 0, mainContainerMeasuredWidth + oldMainLeft, mainContainerMeasuredHeight);

            }
            processAnimation(mainContainer.getLeft());
            //如果是要compatible2.3版本的话就要调用这个方法
//            invalidate();
        }

        /**
         * 第六个被调用
         * 当手指松开时调用
         *
         * @param releasedChild
         * @param xvel          水平方向的速度。向右是+向左是-
         * @param yvel          垂直方向的速度。向下是+向上是-
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel > 1.0f) {
                open();
            } else if (xvel == 0 && mainContainer.getLeft() > mainRange/2){
                open();
            } else{
                close();
            }
        }
    }

    /**
     * 让一个值在固定的mainRange范围内
     *
     * @param left
     * @return
     */
    private int fixRange(int left) {
        if (left < 0) {
            return 0;
        } else if (left > mainRange) {
            return mainRange;
        }
        return left;
    }

    /**
     * 动画执行
     * @param start
     * @param end
     */
    private void doTranslateAnimator(int start, int end) {
        final ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                processAnimation(value);

            }
        });
        va.start();
    }

    /**
     * 动画处理
     *
     * @param range
     */
    private void processAnimation(int range) {
        //main
        float radio = 1 - (range * 0.2f / mainRange);
        mainContainer.layout(range, 0, mainContainerMeasuredWidth + range, mainContainerMeasuredHeight);
        mainContainer.setScaleY(radio);
        mainContainer.setScaleX(radio);

        //menu
        //0.8-1.0
        float scale = (range * 0.2f / mainRange) + 0.8f;
        menuContainer.setScaleX(scale);
        menuContainer.setScaleY(scale);

        float translate = (range * 0.4f / mainRange) + 0.6f;
        int left = (int) (menuContainerMeasuredWidth * translate) - menuContainerMeasuredWidth;
        int right = left + menuContainerMeasuredWidth;
        menuContainer.layout(left, 0, right, menuContainerMeasuredHeight);

        float alpha = (range * 0.9f / mainRange) + 0.1f;
        menuContainer.setAlpha(alpha);
    }

    /**
     * 开
     */
    private void open() {
        doTranslateAnimator(mainContainer.getLeft(), mainRange);
    }

    /**
     *关
     */
    private void close() {
        doTranslateAnimator(mainContainer.getLeft(), 0);
    }

    public interface OnDragStateChangedListener{
        void open();
        void close();
        void draging(int position);
    }

    public void setOnDragStateChangedListener(OnDragStateChangedListener listener){
        this.listener = listener;

    }
}
