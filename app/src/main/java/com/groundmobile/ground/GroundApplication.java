package com.groundmobile.ground;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.Calendar;

public class GroundApplication extends Application{
    public static final String GROUND_DEV_API = "http://222.122.202.150:1038/";
    public static int DISPLAY_HEIGHT;    //단말기 높이
    public static int DISPLAY_WIDTH;    //단말기 너비
    //오늘 날짜
    public static int TODAY_YEAR;
    public static int TODAY_MONTH;
    public static int TODAY_DAY;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        GroundApplication.context = getApplicationContext();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        DISPLAY_HEIGHT = displayMetrics.heightPixels;
        DISPLAY_WIDTH = displayMetrics.widthPixels;

        Calendar cal = Calendar.getInstance();
        TODAY_YEAR = cal.get(Calendar.YEAR);
        TODAY_MONTH = cal.get(Calendar.MONTH)+1;
        TODAY_DAY = cal.get(Calendar.DATE);
    }

    public static Context getAppContext() {
        return GroundApplication.context;
    }
}
