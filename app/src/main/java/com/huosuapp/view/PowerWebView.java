package com.huosuapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by admin on 2016/8/15.
 */
public class PowerWebView extends WebView {
    public PowerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PowerWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

}



