package com.andreasogeirik.master_frontend.layout.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.util.Constants;

public class CustomTextViewSmall extends TextView {
    public CustomTextViewSmall(Context context) {
        super(context);
        setTextSize(0, 0);
    }

    public CustomTextViewSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(0, 0);
    }

    public CustomTextViewSmall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextSize(0, 0);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setTextSize (int unit, float size){
        switch(Constants.USER_SET_SIZE){
            case Constants.SMALL:
                super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                break;
            case Constants.LARGE:
                super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
                break;
            case Constants.MEDIUM:
            default:
                super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
                break;
        }
    }
}