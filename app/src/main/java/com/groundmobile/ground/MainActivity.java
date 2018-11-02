package com.groundmobile.ground;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.LoginPresenter;
import presenter.view.LoginView;
import util.BackPressCloseHandler;
import view.BoardFragment;
import view.HomeFragment;
import view.SettingFragment;

/**
 * MainActivity
 * 어플리케이션을 실행하였을 때 실행된다.
 * MainActivity 위에 Fragment들이 존재한다.(HomeFragment(홈) / BoardFragment(게시판) / SettingFragment(설정))
 * FirebaseAuth 를 통해 현재 User가 로그인 상태인지 아닌지를 판별한다.
 */

public class MainActivity extends BaseActivity implements LoginView{
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BackPressCloseHandler backPressCloseHandler;
    private FirebaseAuth mAuth;
    private int currentPage = R.id.tab_1;    // 현재 탭을 알기 위함.
    private LoginPresenter loginPresenter;

    @BindView(R.id.tab1_img) ImageView tab1_iv;
    @BindView(R.id.tab2_img) ImageView tab2_iv;
    @BindView(R.id.tab3_img) ImageView tab3_iv;
    @BindView(R.id.tab1_txt) TextView tab1_tv;
    @BindView(R.id.tab2_txt) TextView tab2_tv;
    @BindView(R.id.tab3_txt) TextView tab3_tv;

    //Firebase 객체 제거
    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Firebase 리스너 적용
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // loginPresenter 로 부터 생성된 realm 객체 해제
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(loginPresenter != null)
            loginPresenter.RealmDestroy();
        loginPresenter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        //하단 탭 메뉴 초기화
        initTabIcon(currentPage);
    }

    /**
     * mAuthListener 를 통해 현재 user가 로그인 상태인지 아닌지를 판별한다.
     * 만약 user가 로그인 상태라면 loginPresenter를 통해 로컬 DB인 realm에 user data를 업데이트한다.
     * 업데이트된 user data는 singleTon 객체에 저장된다.
     */
    private void init(){
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Firebase 객체 생성
        mAuth = FirebaseAuth.getInstance();
        loginPresenter = new LoginPresenter(this, getApplicationContext());

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, new HomeFragment());
        fragmentTransaction.commit();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null || isLogin()) {
                    //Firebase에서 로그인된 User 가 있는 경우 Realm에 저장된 데이터를 싱글톤에 저장
                    loginPresenter.updateUserData(user.getUid());
                }
            }};
    }

    /**
     * 하단 바텀 메뉴 초기화
     * 탭 문구 색상 변경은 tab3_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)); 이런식으로 한다.
     * @param tabId
     */
    private void initTabIcon(int tabId){
        tab1_iv.setBackgroundResource(R.mipmap.home_img_white_not_selected);
        tab2_iv.setBackgroundResource(R.mipmap.board_img_white_not_selected);
        tab3_iv.setBackgroundResource(R.mipmap.setting_img_white_not_selected);

        switch (tabId){
            case R.id.tab_1:
                tab1_iv.setBackgroundResource(R.mipmap.home_img_white_selected);
                break;

            case R.id.tab_2:
                tab2_iv.setBackgroundResource(R.mipmap.board_img_white_selected);
                break;

            case R.id.tab_3:
                tab3_iv.setBackgroundResource(R.mipmap.setting_img_white_selected);
                break;
        }
    }

    @Override
    public void loginClick(){
        // 여기서는 사용되지 않는다.
    }

    @Override
    public void goMainActivity(){
        // 여기서는 사용되지 않는다.
    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3}) void tabClick(View v){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        initTabIcon(v.getId());

        switch (v.getId()){
            case R.id.tab_1:
                if(currentPage != R.id.tab_1){
                    currentPage = R.id.tab_1;
                    fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                }else{
                    return;
                }
                break;
            case R.id.tab_2:
                if(currentPage != R.id.tab_2){
                    currentPage = R.id.tab_2;
                    fragment = new BoardFragment();
                    fragment.setArguments(bundle);
                }else{
                    return;
                }
                break;
            case R.id.tab_3:
                if(currentPage != R.id.tab_3){
                    currentPage = R.id.tab_3;
                    fragment = new SettingFragment();
                    fragment.setArguments(bundle);
                }else{
                    return;
                }
                break;

        }
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
