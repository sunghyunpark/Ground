package view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Formation 화면
 *
 */
public class FormationActivity extends BaseActivity implements View.OnTouchListener{

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

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.formation_background_img1);
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

    private ImageView getImageView(){
        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                GroundApplication.DISPLAY_HEIGHT/16,
                GroundApplication.DISPLAY_HEIGHT/16));
        imageView.setBackgroundResource(R.drawable.formation_circle_shape);
        imageView.setOnTouchListener(this);
        return imageView;
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

    @OnClick(R.id.save_btn) void saveBtn(){

    }
}
