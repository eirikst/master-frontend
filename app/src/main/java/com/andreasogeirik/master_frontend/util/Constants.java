package com.andreasogeirik.master_frontend.util;

/**
 * Created by Andreas on 26.01.2016.
 */
public class Constants {
    //Azure
//    public static final String BACKEND_URL = "http://sportydul.azurewebsites.net/";

    // NTNU HOST
//    public static final String BACKEND_URL = "http://129.241.102.163:8080/";

    //Home
//    public static final String BACKEND_URL = "http://10.0.0.121:8080/";
    //NTNU
    public static final String BACKEND_URL = "http://129.241.102.250:8080/";

    //Eirik NTNU
//    public static final String BACKEND_URL = "http://10.22.43.75:8080/";

    //Eirik hjemme
    //public static final String BACKEND_URL = "http://10.0.0.99:8080/";



    //Status codes for Tasks(http communication)
    public static final int OK = 1;
    public static final int RESOURCE_ACCESS_ERROR = -1;
    public static final int CLIENT_ERROR = -2;
    public static final int JSON_PARSE_ERROR = -3;
    public static final int UNAUTHORIZED = -4;
    public static final int SOME_ERROR = -5;

    //Number of posts returned when querying post
    public static int NUMBER_OF_POSTS_RETURNED = 10;

    //Number of events returned when querying event
    public static int NUMBER_OF_EVENTS_RETURNED = 5;

    //Number of users returned when searcing users
    public static int NUMBER_OF_USERS_RETURNED_SEARCH = 5;

    //Text sizes
    public static int USER_SET_SIZE = 2;
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;

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
