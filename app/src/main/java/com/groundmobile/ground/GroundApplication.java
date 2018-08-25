package com.groundmobile.ground;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

public class GroundApplication extends Application{
    public static final String GROUND_DEV_API = "http://222.122.202.150:1038/";

    public static int DISPLAY_HEIGHT;    //단말기 높이
    public static int DISPLAY_WIDTH;    //단말기 너비

    public static final String DEFAULT_TIME_FORMAT = "0000-00-00";
    public static final String MY_ARTICLE_TYPE = "myArticle";
    public static final String MY_COMMENT_TYPE = "myComment";
    public static final String MY_FAVORITE_TYPE = "myFavorite";

    //로컬 저장소
    public static final String STORAGE_DIRECTORY_NAME = "GROUND";
    public static final String IMG_NAME = "_Ground.png";

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        DISPLAY_HEIGHT = displayMetrics.heightPixels;
        DISPLAY_WIDTH = displayMetrics.widthPixels;
    }
}
