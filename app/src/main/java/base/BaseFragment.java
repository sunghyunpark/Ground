package base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import util.NetworkUtils;
import util.SessionManager;
import util.Util;

public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isLogin(){
        return sessionManager.isLoggedIn();
    }

    protected void showLoading(){
        hideLoading();
        mProgressDialog = Util.showLoadingDialog(getActivity());
    }

    protected void hideLoading(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    protected void showMessage(String message){
        Util.showToast(getContext(), message);
    }

    protected boolean isNetworkConnected(){
        return NetworkUtils.isNetworkConnected(getActivity());
    }
}
