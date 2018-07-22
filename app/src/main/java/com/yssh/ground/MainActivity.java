package com.yssh.ground;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
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

    private final static String TAG = "MainActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private BackPressCloseHandler backPressCloseHandler;
    private FirebaseAuth mAuth;
    private int currentPage = R.id.tab_1;
    private LoginPresenter loginPresenter;

    @BindView(R.id.tab_1) ViewGroup tabBtn1;
    @BindView(R.id.tab_2) ViewGroup tabBtn2;
    @BindView(R.id.tab_3) ViewGroup tabBtn3;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        mAuth = FirebaseAuth.getInstance();    // Firebase 객체 생성

        init();

        //하단 탭 메뉴 초기화
        InitTabIcon(currentPage);
    }

    /**
     * mAuthListener 를 통해 현재 user가 로그인 상태인지 아닌지를 판별한다.
     * 만약 user가 로그인 상태라면 loginPresenter를 통해 로컬 DB인 realm에 user data를 업데이트한다.
     * 업데이트된 user data는 singleTon 객체에 저장된다.
     */
    private void init(){
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
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    loginPresenter.updateUserData(user.getUid());

                    Log.d("userData",
                            "uid : "+ UserModel.getInstance().getUid()+"\n"+
                            "loginType : "+ UserModel.getInstance().getLoginType()+"\n"+
                            "nickName : "+ UserModel.getInstance().getNickName());
                } /*else {
                    //Firebase에 로그인된 User가 없는 경우 Intro 화면으로 이동
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                    finish();
                }*/
            }};
    }

    /**
     * 하단 바텀 메뉴 초기화
     * @param tabId
     */
    private void InitTabIcon(int tabId){
        tab1_iv.setBackgroundResource(R.mipmap.home_img);
        tab1_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMoreGray));
        tab2_iv.setBackgroundResource(R.mipmap.board_img);
        tab2_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMoreGray));
        tab3_iv.setBackgroundResource(R.mipmap.setting_img);
        tab3_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMoreGray));

        switch (tabId){
            case R.id.tab_1:
                tab1_iv.setBackgroundResource(R.mipmap.home_selected_img);
                //tab1_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                break;

            case R.id.tab_2:
                tab2_iv.setBackgroundResource(R.mipmap.board_selected_img);
                //tab2_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                break;

            case R.id.tab_3:
                tab3_iv.setBackgroundResource(R.mipmap.setting_selected_img);
                //tab3_tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void loginClick(){

    }

    @Override
    public void goMainActivity(){

    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3}) void click(View v){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        InitTabIcon(v.getId());

        switch (v.getId()){
            case R.id.tab_1:
                if(currentPage == R.id.tab_1){
                    return;
                }else{
                    currentPage = R.id.tab_1;
                    fragment = new HomeFragment();
                    bundle.putString("KEY_MSG", "replace");
                    fragment.setArguments(bundle);
                }
                break;
            case R.id.tab_2:
                if(currentPage == R.id.tab_2){
                    return;
                }else{
                    currentPage = R.id.tab_2;
                    fragment = new BoardFragment();
                    bundle.putString("KEY_MSG", "replace");
                    fragment.setArguments(bundle);
                }
                break;
            case R.id.tab_3:
                if(currentPage == R.id.tab_3){
                    return;
                }else{
                    currentPage = R.id.tab_3;
                    fragment = new SettingFragment();
                    bundle.putString("KEY_MSG", "replace");
                    fragment.setArguments(bundle);
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
