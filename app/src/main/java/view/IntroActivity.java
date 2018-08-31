package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.groundmobile.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.background_layout) ViewGroup background_vg;
    //private Bitmap resized, bitmap;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //resized.recycle();
        //bitmap.recycle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        //init();
    }

    private void init(){
        //setBackground();
    }

    /**
     * Intro 화면의 백그라운드 적용
     */
    private void setBackground(){
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.intro_bg_img1);
        //resized = Bitmap.createScaledBitmap(bitmap, GroundApplication.DISPLAY_WIDTH, GroundApplication.DISPLAY_HEIGHT, true);

        //Drawable d = new BitmapDrawable(getResources(), resized);
        //background_vg.setBackground(d);
    }

    /**
     * 회원가입 화면으로 이동.
     */
    @OnClick(R.id.register_btn) void registerBtn(){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    /**
     * 로그인 화면으로 이동.
     */
    @OnClick(R.id.login_btn) void loginBtn(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
