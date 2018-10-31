package view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groundmobile.ground.R;

import base.BaseFragment;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.LoginPresenter;
import presenter.view.LoginView;
import presenter.view.SettingView;
import util.NetworkUtils;
import util.SessionManager;
import util.Util;
import view.dialog.EditProfileDialog;

public class SettingFragment extends BaseFragment implements LoginView, SettingView {

    private SessionManager sessionManager;
    private LoginPresenter loginPresenter;

    @BindView(R.id.user_id_tv) TextView user_id_tv;
    @BindView(R.id.current_app_version_tv) TextView app_version_tv;
    @BindView(R.id.user_nick_name_tv) TextView user_nickName_tv;
    @BindString(R.string.setting_fragment_login_default_txt) String loginDefaultStateStr;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(loginPresenter != null)
            loginPresenter.RealmDestroy();
        sessionManager = null;
        loginPresenter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);

        initUI();
        return v;
    }

    private void init(){
        loginPresenter = new LoginPresenter(this, getContext());
        sessionManager = new SessionManager(getContext());
    }

    private void initUI(){

        // 로그인 상태에 따른 로그인 버튼 초기화
        setLoginState();

        // 앱 버전 초기화
        setAppVersion();

        // 사용자 닉네임 초기화
        setUserNickName();
    }

    // 로그인 상태에 따른 init
    private void setLoginState(){
        if(sessionManager.isLoggedIn()){
            user_id_tv.setText(UserModel.getInstance().getEmail());
        }else{
            user_id_tv.setText(loginDefaultStateStr);
        }
    }

    // 사용자 닉네임 초기화
    private void setUserNickName(){
        if(sessionManager.isLoggedIn()){
            user_nickName_tv.setText(UserModel.getInstance().getNickName());
        }else{
            user_nickName_tv.setText("");
        }
    }

    // 앱 버전 초기화
    private void setAppVersion(){
        try {
            PackageInfo i = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            app_version_tv.setText("v"+i.versionName);
        } catch(PackageManager.NameNotFoundException e) { }
    }

    /**
     * 로그인 영역을 탭 시
     * - 로그인 상태
     * : 로그아웃 확인용 다이얼로그 노출
     * - 로그아웃 상태
     * : 인트로화면 노출
     */
    @Override
    public void login(){
        if(!NetworkUtils.isNetworkConnected(getContext())){
            Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
        }else if(!sessionManager.isLoggedIn()){
            //로그인 상태가 아니므로 인트로 화면을 띄워준다.
            startActivity(new Intent(getContext(), IntroActivity.class));
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("로그아웃");
            alert.setMessage("정말 로그아웃 하시겠습니까?");
            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    sessionManager.setLogin(false);
                    loginPresenter.logOut(UserModel.getInstance().getUid());
                    setLoginState();
                    setUserNickName();
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

    @Override
    public void goProfile(){
        // 프로필 다이얼로그에서 설정한 닉네임을 설정한다.
        if(isLogin()){
            EditProfileDialog editProfileDialog = new EditProfileDialog(getContext(), new EditProfileDialog.EditProfileDialogListener() {
                @Override
                public void editUserNameEvent() {
                    user_nickName_tv.setText(UserModel.getInstance().getNickName());
                }
            });

            editProfileDialog.show();
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    /**
     * 카카오톡 챗봇 친구추가 웹 페이지로 이동.
     */
    @Override
    public void goKakaoChatbot(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pf.kakao.com/_xkYIIj"));
        startActivity(intent);
    }

    /**
     * 리뷰 작성하기
     * 플레이 스토어로 이동된다.
     */
    @Override
    public void goReview(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getContext().getPackageName()));
        startActivity(intent);
    }

    @Override
    public void goOpenSource(){
        Intent intent = new Intent(getContext(), OpenSourceActivity.class);
        startActivity(intent);
    }

    /**
     * 버그 신고
     * 그라운드 네이버 오피스 폼 웹페이지로 이동된다.
     */
    @Override
    public void goReportBug(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://naver.me/IGvFHAfa"));
        startActivity(intent);
    }

    /**
     * 추천하기
     * 문자 앱으로 이동되며 디폴트로 문자 내용에 플레이스토어 주소가 입력되어있다.
     */
    @Override
    public void goRecommend(){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        //String subject = "문자의 제목";
        String text = "https://play.google.com/store/apps/details?id="+getContext().getPackageName();
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // Title of intent
        Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
        startActivity(chooser);
    }

    @Override
    public void goAppPush(){
        Intent intent = new Intent(getContext(), AppPushActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginClick(){

    }

    @Override
    public void goMainActivity(){

    }

    @OnClick(R.id.login_btn) void loginBtn(){
        login();
    }

    @OnClick(R.id.profile_btn) void profileBtn(){
        goProfile();
    }

    @OnClick(R.id.kakao_chatbot_btn) void kakaoChatBotBtn(){
        goKakaoChatbot();
    }

    @OnClick(R.id.review_btn) void reviewBtn(){
        goReview();
    }

    @OnClick(R.id.app_license_btn) void openSourceBtn(){
        goOpenSource();
    }

    @OnClick(R.id.report_issue_btn) void reportIssueBtn(){
        goReportBug();
    }

    @OnClick(R.id.recommend_btn) void recommendBtn(){
        goRecommend();
    }

    @OnClick(R.id.app_push_btn) void appPushBtn(){
        goAppPush();
    }
}
