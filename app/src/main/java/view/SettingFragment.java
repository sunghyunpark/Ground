package view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;

import base.BaseFragment;
import presenter.LoginPresenter;
import presenter.view.LoginView;
import util.SessionManager;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;

public class SettingFragment extends BaseFragment implements LoginView {

    private SessionManager sessionManager;
    private LoginPresenter loginPresenter;

    @BindView(R.id.user_id_tv) TextView user_id_tv;
    @BindView(R.id.current_app_version_tv) TextView app_version_tv;
    @BindString(R.string.setting_fragment_login_default_txt) String loginDefaultStateStr;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(loginPresenter != null)
            loginPresenter.RealmDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);

        init();
        return v;
    }

    private void init(){
        loginPresenter = new LoginPresenter(this, getContext());
        sessionManager = new SessionManager(getContext());

        setLoginState();
        setAppVersion();
    }

    //로그인 상태에 따른 init
    private void setLoginState(){
        if(sessionManager.isLoggedIn()){
            user_id_tv.setText(UserModel.getInstance().getEmail());
            Log.d("After Register", UserModel.getInstance().getEmail()+"????");
        }else{
            user_id_tv.setText(loginDefaultStateStr);
        }
    }

    private void setAppVersion(){
        //앱 버전
        String version;
        try {
            PackageInfo i = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = i.versionName;
            app_version_tv.setText("v"+version);
        } catch(PackageManager.NameNotFoundException e) { }
    }

    @Override
    public void loginClick(){

    }

    @Override
    public void goMainActivity(){

    }

    /**
     * 로그인 영역을 탭 시
     * - 로그인 상태
     * : 로그아웃 확인용 다이얼로그 노출
     * - 로그아웃 상태
     * : 인트로화면 노출
     */
    @OnClick(R.id.login_btn) void loginBtn(){
        if(sessionManager.isLoggedIn()){
            //login
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("로그아웃");
            alert.setMessage("정말 로그아웃 하시겠습니까?");
            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    sessionManager.setLogin(false);
                    loginPresenter.logOut(UserModel.getInstance().getUid());
                    setLoginState();
                }
            });
            alert.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.

                        }
                    });
            alert.show();
        }else{
            //not login
            //로그인 상태가 아니므로 인트로 화면을 띄워준다.
            startActivity(new Intent(getContext(), IntroActivity.class));
        }
    }
}
