package view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.Util;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.background_layout) ViewGroup background_vg;
    private Bitmap resized, bitmap;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        resized.recycle();
        bitmap.recycle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        setBackground();
    }

    /**
     * Intro 화면의 백그라운드 적용
     */
    private void setBackground(){
        DisplayMetrics displayMetrics = Util.getDisplayMetrics(getApplicationContext());
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.intro_bg_img1);
        resized = Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, true);

        Drawable d = new BitmapDrawable(getResources(), resized);
        background_vg.setBackground(d);
    }

    /**
     * 회원가입 화면으로 이동.
     */
    @OnClick(R.id.register_btn) void goToRegister(){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    /**
     * 로그인 화면으로 이동.
     */
    @OnClick(R.id.login_btn) void goToLogin(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
