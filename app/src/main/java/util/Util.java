package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yssh.ground.R;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import util.adapter.AreaBoardAdapter;

public class Util {

    public static final int SEND_RUNNING = 1000;
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

    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
    public static String formatTimeString(Date tempDate) {

        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String parseTime(String timeStr){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        s.setTimeZone(TimeZone.getDefault());

        return s.format(getDate(timeStr));
    }

    private static Date getDate(String dateStr) {
        SimpleDateFormat s;
        if (dateStr.endsWith("Z")) {
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'", Locale.getDefault());
            s.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault());
        }
        try {
            return s.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class BannerHandler extends Handler {
        private final WeakReference<Object> weakReference;
        private ViewPager viewPager;
        private int bannerCount;

        public BannerHandler(Object object, ViewPager viewPager, int bannerCount){
            this.weakReference = new WeakReference<Object>(object);
            this.viewPager = viewPager;
            this.bannerCount = bannerCount;
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SEND_RUNNING:
                    if(viewPager.getCurrentItem() % bannerCount == 0){
                        viewPager.setCurrentItem(1);

                    }else if(viewPager.getCurrentItem() % bannerCount == 1){
                        viewPager.setCurrentItem(2);

                    }else if(viewPager.getCurrentItem() % bannerCount == 2){
                        viewPager.setCurrentItem(0);

                    }
                    //viewPager.arrowScroll(View.FOCUS_RIGHT);
                    break;
                default:
                    break;
            }
        }
    }

}
