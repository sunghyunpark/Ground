package view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.io.File;
import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.JMediaScanner;
import util.ShareArticleTask;

/**
 * Formation 화면
 *
 */
public class FormationActivity extends BaseActivity implements View.OnTouchListener{

    private static final int REQUEST_PERMISSIONS = 10;
    private static final int DEFAULT_CIRCLE_CNT = 5;

    private ArrayList<ImageView> imageViews;
    private ImageView imageView;
    private float oldXvalue;
    private float oldYvalue;
    private Bitmap resized, bitmap;
    private Drawable d;

    @BindView(R.id.background_layout) ViewGroup background_layout;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(imageViews != null){
            imageViews = null;
        }
        if(imageView != null){
            imageView = null;
        }
        resized.recycle();
        bitmap.recycle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        ButterKnife.bind(this);
        init();
    }

    private void init(){
        imageViews = new ArrayList<>();
        for(int i = 0; i < DEFAULT_CIRCLE_CNT; i++){
            imageViews.add(getImageView());
            background_layout.addView(imageView);
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.formation_background_img);
        resized = Bitmap.createScaledBitmap(bitmap, GroundApplication.DISPLAY_WIDTH, GroundApplication.DISPLAY_HEIGHT, true);

        d = new BitmapDrawable(getResources(), resized);
        background_layout.setBackground(d);
    }

    private void addCircle(){
        if(imageViews.size() < 11){
            imageViews.add(getImageView());
            background_layout.addView(imageView);
        }else{
            showMessage("최대 11개 까지만 생성이 가능합니다.");
        }
    }

    private void removeCircle(){
        if(imageViews.size() < 6){
            showMessage("최소 5개이상이어야 합니다.");
        }else{
            background_layout.removeView(imageViews.get(imageViews.size()-1));
            imageViews.remove(imageViews.size()-1);
            background_layout.invalidate();
        }
    }

    private ImageView getImageView(){
        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                GroundApplication.DISPLAY_HEIGHT/18,
                GroundApplication.DISPLAY_HEIGHT/18));
        imageView.setBackgroundResource(R.drawable.formation_circle_shape);
        imageView.setOnTouchListener(this);
        return imageView;
    }

    private void saveClick(){
        showLoading();
        ShareArticleTask shareArticleTask = new ShareArticleTask(new ShareArticleTask.callbackListener() {
            @Override
            public void openChooserCallback(String mediaPath, String timeStamp) {
                //ShareArticleTask 의 interface
                hideLoading();
                showMessage("전술판을 캡쳐했습니다.");
                openShareChooser(mediaPath, timeStamp);
            }
        });
        shareArticleTask.execute(takeScreenshot(background_layout));
    }

    /**
     * 츄져 노출
     * @param mediaPath -> 이미지 경로 (ShareArticleTask)
     * @param timeStamp -> 이미지 생성 시간 (ShareArticleTask)
     */
    private void openShareChooser(String mediaPath, String timeStamp){
        String type = "image/*";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.groundmobile.ground.fileProvider", media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

        JMediaScanner scanner = new JMediaScanner(getApplicationContext());
        scanner.startScan(Environment.getExternalStorageDirectory()+ "/" + GroundApplication.STORAGE_DIRECTORY_NAME + "/"+timeStamp+GroundApplication.IMG_NAME);

    }

    private Bitmap takeScreenshot(View rootView) {
        rootView.setDrawingCacheEnabled(true);
        Bitmap bit = rootView.getDrawingCache();
        Rect statusBar = new Rect();
        //this.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBar);
        return Bitmap.createBitmap(bit, 0, statusBar.top, bit.getWidth(), bit.getHeight() - statusBar.top, null, true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            //  Log.i("Tag1", "Action Down X" + event.getX() + "," + event.getY());
            Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
            //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }
        }
        return true;
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

    @OnClick(R.id.add_btn) void addCircleBtn(){
        addCircle();
    }

    @OnClick(R.id.minus_btn) void removeCircleBtn(){
        removeCircle();
    }

    @OnClick(R.id.save_btn) void saveBtn(){
        if (ContextCompat.checkSelfPermission(FormationActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(FormationActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (FormationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(FormationActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(FormationActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            saveClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                //권한이 있는 경우
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveClick();
                }
                //권한이 없는 경우
                else {
                    showMessage("퍼미션을 허용해야 이용할 수 있습니다.");
                }
                break;
        }
    }
}
