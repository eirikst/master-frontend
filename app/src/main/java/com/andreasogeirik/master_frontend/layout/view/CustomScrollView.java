package com.andreasogeirik.master_frontend.layout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Andreas on 06.04.2016.
 */
public class CustomScrollView extends android.widget.ScrollView {

    private boolean scrollable = true;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (scrollable){
                    return super.onTouchEvent(ev);
                }

                return scrollable;
            default:
                return super.onTouchEvent(ev);
        }
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollable = enabled;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!scrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }
}
