package com.example.isslocator.common;

public class Constants {

    public static String BASE_URL = "http://api.open-notify.org/";
    public static final String ISS_LOCATOR_ENDPOINT = "iss-pass.json";


    //RETROFIT
    public static final int OK_HTTP_CACHE_LIMIT = 2 * 1024 * 1024;
    public static final long CONNECT_TIMEOUT = 30; //seconds
    public static final long READ_TIMEOUT = 30;
}
