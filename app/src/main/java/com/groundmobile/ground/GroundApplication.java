package com.groundmobile.ground;

import android.app.Application;
import android.util.DisplayMetrics;

import java.util.Calendar;

public class GroundApplication extends Application{
    public static final String GROUND_DEV_API = "http://222.122.202.150:1038/";

    public static int DISPLAY_HEIGHT;    //단말기 높이
    public static int DISPLAY_WIDTH;    //단말기 너비

    public static final String DEFAULT_TIME_FORMAT = "0000-00-00";
    //MY
    public static final String MY_ARTICLE_TYPE = "myArticle";
    public static final String MY_COMMENT_TYPE = "myComment";
    public static final String MY_FAVORITE_TYPE = "myFavorite";

    //로컬 저장소
    public static final String STORAGE_DIRECTORY_NAME = "GROUND";
    public static final String IMG_NAME = "_Ground.png";

    //오늘 날짜
    public static int TODAY_YEAR;
    public static int TODAY_MONTH;
    public static int TODAY_DAY;

    //KEY
    public static final String EXTRA_USER_ID = "uid";
    public static final String EXTRA_BOARD_TYPE = "boardType";
    public static final String EXTRA_AREA_NAME = "area";
    public static final String EXTRA_AREA_NO = "areaNo";
    public static final String EXTRA_ARTICLE_NO = "articleNo";
    public static final String EXTRA_EXIST_ARTICLE_MODEL = "hasArticleModel";
    public static final String EXTRA_ARTICLE_MODEL = "articleModel";
    public static final String EXTRA_MY_TYPE = "myType";
    public static final String EXTRA_BOARD_PHOTO_URL = "photoUrl";

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        DISPLAY_HEIGHT = displayMetrics.heightPixels;
        DISPLAY_WIDTH = displayMetrics.widthPixels;

        Calendar cal = Calendar.getInstance();
        TODAY_YEAR = cal.get(Calendar.YEAR);
        TODAY_MONTH = cal.get(Calendar.MONTH)+1;
        TODAY_DAY = cal.get(Calendar.DATE);
    }
}
