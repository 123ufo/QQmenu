package com.example.tenxingqq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 作者：xudiwei
 * <p/>
 * 日期： Administrator on 2015/9/28.
 * <p/>
 * 文本描述：
 */
public class MyLinearLayout extends LinearLayout {
    private SlideLayout_1 slideLayout_1;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSlideLayout(SlideLayout_1 slideLayout_1) {
        this.slideLayout_1 = slideLayout_1;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (null == slideLayout_1 || slideLayout_1.getStates() == SlideLayout_1.STATE_CLOSE) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == slideLayout_1 || slideLayout_1.getStates() == SlideLayout_1.STATE_CLOSE) {
            return super.onTouchEvent(event);
        }else{
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP){
                slideLayout_1.close();
            }
            return true;
        }
    }
}
