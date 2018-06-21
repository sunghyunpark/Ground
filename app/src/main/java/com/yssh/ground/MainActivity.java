package com.yssh.ground;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import util.LoginManager;
import view.IntroActivity;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private LoginManager loginManager;

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
    // LoginManager 로 부터 생성된 realm 객체 해제
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(loginManager != null)
            loginManager.RealmDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();    // Firebase 객체 생성

        init();
    }

    private void init(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Firebase에서 로그인된 User 가 있는 경우 Realm에 저장된 데이터를 싱글톤에 저장
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    loginManager = new LoginManager(getApplicationContext());
                    loginManager.updateUserData(user.getUid());
                } else {
                    //Firebase에 로그인된 User가 없는 경우 Intro 화면으로 이동
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                    finish();
                }
            }};
    }

    @OnClick(R.id.go_intro_btn) void goTest(){
        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intent);
    }
}
