package com.andreasogeirik.master_frontend.application.main.interfaces;


import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface MainPageView {
    void initGUI();
    void navigateToEntrance();
    void setNotificationCount(int count);
    void displayMessage(String message);
    void showNotificationCenter(Set<Object> notifications);
    void showProgress();
    void hideProgress();
}