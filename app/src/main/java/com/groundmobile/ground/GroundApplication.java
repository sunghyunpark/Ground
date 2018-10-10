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
    public static final String EXTRA_MATCH_BOARD_TYPE = "boardType";    // match 게시판(match/hire/recruit)에만 사용된다. 분기처리용.
    public static final String EXTRA_AREA_NAME = "area";
    public static final String EXTRA_AREA_NO = "areaNo";
    public static final String EXTRA_ARTICLE_NO = "articleNo";
    public static final String EXTRA_EXIST_ARTICLE_MODEL = "hasArticleModel";
    public static final String EXTRA_ARTICLE_MODEL = "articleModel";
    public static final String EXTRA_MY_TYPE = "myType";
    public static final String EXTRA_BOARD_PHOTO_URL = "photoUrl";
    public static final String EXTRA_ARTICLE_TYPE = "articleType";

    //ARTICLE TYPE 댓글 관련하여 match(match/hire/recruit) / free 를 구분할 때 사용
    public static final String BOARD_TYPE_MATCH = "match";    // match(match / hire / recruit)
    public static final String BOARD_TYPE_FREE = "free";
    // 아래 3개의 변수는 match 게시판 내에서 분기처리가 필요한 경우 사용된다.
    public static final String MATCH_OF_BOARD_TYPE_MATCH = "match";
    public static final String HIRE_OF_BOARD_TYPE_MATCH = "hire";
    public static final String RECRUIT_OF_BOARD_TYPE_MATCH = "recruit";

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
