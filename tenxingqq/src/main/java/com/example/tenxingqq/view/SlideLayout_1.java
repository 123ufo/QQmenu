package com.example.tenxingqq.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.apache.http.util.ExceptionUtils;

/**
 * 作者：xudiwei
 * <p/>
 * 日期： Administrator on 2015/9/24.
 * <p/>
 * 文本描述：
 */
public class SlideLayout_1 extends FrameLayout {

    private View menuContainer;
    private View mainContainer;
    private DragCallBack dragCallBack;
    private ViewDragHelper viewDragHelper;
    private int menuContainerMeasuredWidth;
    private int menuContainerMeasuredHeight;
    private int mainContainerMeasuredWidth;
    private int mainContainerMeasuredHeight;
    private int range;
    private OnDragStateChangedListener listener;

    public static int STATE_OPEN = 1;  //开状态
    public static int STATE_CLOSE = 2; //状状态
    public static int STATE_DRAGGING = 3; //拖动状态

    private int states = STATE_CLOSE;

    public SlideLayout_1(Context context) {
        this(context, null);
    }

    public SlideLayout_1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        dragCallBack = new DragCallBack();
        viewDragHelper = ViewDragHelper.create(this, dragCallBack);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalArgumentException("CN:子控件必须是两个 EN:must two child view in this layout");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取菜单view
        menuContainer = getChildAt(0);
        //获取主界面view
        mainContainer = getChildAt(1);
        //菜单的宽高
        menuContainerMeasuredWidth = menuContainer.getMeasuredWidth();
        menuContainerMeasuredHeight = menuContainer.getMeasuredHeight();
        //主界面的宽高
        mainContainerMeasuredWidth = mainContainer.getMeasuredWidth();
        mainContainerMeasuredHeight = mainContainer.getMeasuredHeight();
        //滑动范围
        range = (int) (mainContainerMeasuredWidth * 0.6f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return viewDragHelper.shouldInterceptTouchEvent(ev);

//        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        System.out.println("onlayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            viewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private class DragCallBack extends ViewDragHelper.Callback {

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
            System.out.println("tryCaptureview");
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
            super.onViewCaptured(capturedChild, activePointerId);
            System.out.println("onviewcaptured");
        }


        /**
         * 获取水平拖动的一个 range这里如果返回是0的话也touch事件是发生在当子控件上的话则不会响应拖动事件。
         * 这个方法在拖动松开时调用
         * 或当touch事件发现在子控件时调用
         * @param child
         * @return  默认是返回0，重写返回一个大于0的整数
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
//            return super.getViewHorizontalDragRange(child);
            System.out.println("getviewhorizontadragrange");
            return range;
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
            super.onViewDragStateChanged(state);
            System.out.println("onviewdragstatechanged");
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
//            return super.clampViewPositionHorizontal(child, left, dx);
            if (child == mainContainer) {
                return fixLeft(left);
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
//            System.out.println("::" + left);
            if (changedView == menuContainer) {
                menuContainer.layout(0, 0, menuContainerMeasuredWidth, menuContainerMeasuredHeight);
                int templeft = mainContainer.getLeft() + left;
                int mainLeft = fixLeft(templeft);
                mainContainer.layout(mainLeft, 0, mainContainerMeasuredWidth + mainLeft, mainContainerMeasuredHeight);
            }

            dispatchEvent(mainContainer.getLeft());

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
            } else if (xvel == 0f && mainContainer.getLeft() > range/2) {
                open();
            }else{
                close();
            }
        }
    }

    /**
     * 开
     */
    public void open() {
        viewDragHelper.smoothSlideViewTo(mainContainer, range, 0);
        invalidate();
    }

    /**
     * 关
     */
    public void close() {
        viewDragHelper.smoothSlideViewTo(mainContainer, 0, 0);
        invalidate();
    }

    /**
     * 修复left的值在0－range的范围内
     *
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if (left <= 0) {
            return 0;
        } else if (left >= range) {
            return range;
        }
        return left;
    }

    private void dispatchEvent(int left) {

        //1.0-0.8
        float mainScale = 1 - left * 0.2f / range;
        mainContainer.setScaleX(mainScale);
        mainContainer.setScaleY(mainScale);
        //0.8f-1.0
        float leftScale = left * 0.2f / range + 0.8f;
        menuContainer.setScaleX(leftScale);
        menuContainer.setScaleY(leftScale);
        //-range-0
        int menuTranslate = left - range;
        menuContainer.setTranslationX(menuTranslate);
        //0.8f-0.2f
        float menuAlpha = left * 0.8f / range + 0.2f;
        menuContainer.setAlpha(menuAlpha);
        //背景亮度
        float bgAlpha = 1 - left * 1.0f / range;
        getBackground().setColorFilter(evaluatorColor(bgAlpha), PorterDuff.Mode.SRC_OVER);

        //回调
        if (null != listener) {
            if (left <= 0 && states != STATE_CLOSE) {
                listener.close();
                states = STATE_CLOSE;
            } else if (left >= range && states != STATE_OPEN) {
                listener.open();
                states = STATE_OPEN;
            } else if (left > 0 && left < range) {
                listener.dragging(left);
                states = STATE_DRAGGING;
            }
        }

    }

    /**
     * 背景颜色估值计算
     *
     * @param radio
     * @return
     */
    private int evaluatorColor(float radio) {
        //black
        return Color.argb((int) (0xff * radio), 0x00, 0x00, 0x00);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void setOnDragStateChangeListener(OnDragStateChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 回调接口
     */
    public interface OnDragStateChangedListener {
        void open();

        void close();

        void dragging(int offset);
    }

    public int getStates(){
        return states;
    }

}
