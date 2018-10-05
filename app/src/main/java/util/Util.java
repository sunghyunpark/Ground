package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    public static final int SEND_RUNNING = 1000;

    public static void showToast(Context context, String message){
        //토스트를 중앙에 띄워준다.
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
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

    public static String ellipseStr(String str){
        String resultStr;

        if(str.length() > 7){
            resultStr = str.substring(0,6)+"...";
        }else{
            return str;
        }
        return resultStr;
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
        try {
            return s.format(getDate(timeStr));
        }catch (NullPointerException e){
            return GroundApplication.DEFAULT_TIME_FORMAT;
        }
    }

    public static Date getDate(String dateStr) {
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
