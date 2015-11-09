package com.example.tenxingqq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 作者：xudiwei
 * <p/>
 * 日期： Administrator on 2015/9/23.
 * <p/>
 * 文本描述：
 */
public class ScrollerView extends FrameLayout {

    private Scroller scroller;

    public ScrollerView(Context context) {
        this(context, null);
    }

    public ScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView();
    }

    private void intView() {
        scroller = new Scroller(getContext());


    }

    public void run(){
        scroller.startScroll(0,0,100,100);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
        System.out.println("com");
        super.computeScroll();
    }
}
