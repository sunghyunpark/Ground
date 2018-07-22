package base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import util.NetworkUtils;
import util.SessionManager;
import util.Util;

public abstract class BaseActivity extends AppCompatActivity{

    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());

    }

    protected boolean isLogin(){
        return sessionManager.isLoggedIn();
    }

    protected void showLoading(){
        hideLoading();
        mProgressDialog = Util.showLoadingDialog(this);
    }

    protected void hideLoading(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    protected void showMessage(String message){
        Util.showToast(getApplicationContext(), message);
    }

    protected boolean isNetworkConnected(){
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }
}
