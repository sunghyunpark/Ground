package util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Util {

    /**
     * 디바이스 사이즈를 구할 때 사용한다.
     * @param context
     * @return DisplayMetrics
     * width = getDisplayMetrics.widthPixels;
     * height = getDisplayMetrics.heightPixels;
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        return context.getResources().getDisplayMetrics();
    }
}
