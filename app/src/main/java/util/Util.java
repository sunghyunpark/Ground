package util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    public static void showToast(Context context, String message){
        //토스트를 중앙에 띄워준다.
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
