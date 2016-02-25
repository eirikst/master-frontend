package com.andreasogeirik.master_frontend.util;

/**
 * Created by Andreas on 26.01.2016.
 */
public class Constants {
    //Home
//    public static final String BACKEND_URL = "http://10.0.0.181:8080/";
    //NTNU
    //public static final String BACKEND_URL = "http://10.20.55.113:8080/";

    //Eirik NTNU
    //public static final String BACKEND_URL = "http://10.20.53.134:8080/";

    //Eirik hjemme
    public static final String BACKEND_URL = "http://10.0.0.46:8080/";



    //Status codes for Tasks(http communication)
    public static final int OK = 1;
    public static final int RESOURCE_ACCESS_ERROR = -1;
    public static final int CLIENT_ERROR = -2;
    public static final int JSON_PARSE_ERROR = -3;

    //Number of posts returned when querying post
    public static int NUMBER_OF_POSTS_RETURNED = 10;

    //Text sizes
    public static int USER_SET_SIZE = 2;
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;
}
