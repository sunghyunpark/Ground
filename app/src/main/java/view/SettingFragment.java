package view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import util.LoginManager;

public class SettingFragment extends Fragment {

    private View v;
    private LoginManager loginManager;

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
        v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);

        init();
        return v;
    }

    private void init(){
        loginManager = new LoginManager(getContext());
    }

    @OnClick(R.id.logout_btn) void logOut(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("로그아웃");
        alert.setMessage("정말 로그아웃 하시겠습니까?");
        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                loginManager.logOut(UserModel.getInstance().getUid());
            }
        });
        alert.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                    }
                });
        alert.show();
    }
}
