package view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;
import com.yssh.ground.SessionManager;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import util.LoginManager;

public class SettingFragment extends Fragment {

    private LoginManager loginManager;
    private SessionManager sessionManager;

    @BindView(R.id.user_id_tv) TextView user_id_tv;
    @BindString(R.string.setting_fragment_login_default_txt) String loginDefaultStateStr;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(loginManager != null)
            loginManager.RealmDestroy();
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
        loginManager = new LoginManager(getContext());
        sessionManager = new SessionManager(getContext());

        setLoginState();
    }

    private void setLoginState(){
        if(sessionManager.isLoggedIn()){
            user_id_tv.setText(UserModel.getInstance().getEmail());
        }
    }

    @OnClick(R.id.login_btn) void login(){
        if(sessionManager.isLoggedIn()){
            //login
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("로그아웃");
            alert.setMessage("정말 로그아웃 하시겠습니까?");
            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    sessionManager.setLogin(false);
                    loginManager.logOut(UserModel.getInstance().getUid());
                    user_id_tv.setText(loginDefaultStateStr);
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
