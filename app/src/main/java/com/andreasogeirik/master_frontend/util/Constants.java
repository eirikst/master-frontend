package com.andreasogeirik.master_frontend.util;

/**
 * Created by Andreas on 26.01.2016.
 */
public class Constants {
    //Azure
    //public static final String BACKEND_URL = "http://sportydul.azurewebsites.net/";

    // Docker
    public static final String BACKEND_URL = "http://78.91.98.234:49183/";

    //Status codes for Tasks(http communication)
    public static final int OK = 1;
    public static final int RESOURCE_ACCESS_ERROR = -1;
    public static final int CLIENT_ERROR = -2;
    public static final int JSON_PARSE_ERROR = -3;
    public static final int UNAUTHORIZED = -4;
    public static final int SOME_ERROR = -5;

    //Number of posts returned when querying post
    public static int NUMBER_OF_POSTS_RETURNED = 5;

    //Number of events returned when querying event
    public static int NUMBER_OF_EVENTS_RETURNED = 5;

    //Number of users returned when searcing users
    public static int NUMBER_OF_USERS_RETURNED_SEARCH = 5;

    //Event difficulty
    public static final int EVENT_DIFFICULTY_EASY = 1;
    public static final int EVENT_DIFFICULTY_MEDIUM = 2;
    public static final int EVENT_DIFFICULTY_HARD = 3;

    //image
    public static final int EVENT_IMAGE_HEIGHT = 394;
    public static final int EVENT_IMAGE_WIDTH = 700;
    public static final int LIST_IMAGE_HEIGHT = 150;
    public static final int LIST_IMAGE_WIDTH = 150;
    public static final int USER_IMAGE_HEIGHT = 540;
    public static final int USER_IMAGE_WIDTH = 540;
}
