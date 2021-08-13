package com.example.minitwitter.common;

public class Constants {

    //Have this class will help us to avoid any misspells of the constants we need to use in our app

    public static final String API_MINITWITTER_BASE_URL = "https://www.minitwitter.com:3001/apiv1/";
    public static final String API_MINITWITTER_BASE_FILES_URL = "https://www.minitwitter.com/apiv1/uploads/photos/";

    //Start Activity for result
    public static final int SELECT_PHOTO_GALLERY = 1;

    //Preferences
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_PHOTOURL = "PREF_PHOTOURL";
    public static final String PREF_CREATED = "PREF_CREATED";
    public static final String PREF_ACTIVE = "PREF_ACTIVE";

    //Arguments
    public static final String TWEET_LIST_TYPE = "TWEET_LIST_TYPE";
    public static final int TWEET_LIST_ALL = 1;
    public static final int TWEET_LIST_FAVS = 2;
    public static final String ARG_TWEET_ID = "TWEET_ID";

}