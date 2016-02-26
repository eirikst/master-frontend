package com.andreasogeirik.master_frontend.layout.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.MyEventsFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    final int ATTENDING = 0;
    final int COMING_UP = 1;
    final int MY = 2;
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Påmeldt", "Kommende", "Mine" };
    private Context context;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        //TODO:set fragment based on position
        switch(position) {
            case ATTENDING:
                return AttendingEventsFragment.newInstance();
            case COMING_UP:
                return AttendingEventsFragment.newInstance();//TODO:change
            case MY:
                return MyEventsFragment.newInstance();
            default:
                return AttendingEventsFragment.newInstance();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}