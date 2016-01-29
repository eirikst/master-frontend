package com.andreasogeirik.master_frontend.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Andreas on 29.01.2016.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private ListView listView;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public boolean canChildScrollUp(){
        if (listView == null){
            return true;
        }
        return listView.canScrollVertically(-1);
    }
}
