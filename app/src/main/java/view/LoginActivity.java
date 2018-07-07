package view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yssh.ground.MainActivity;
import com.yssh.ground.R;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.LoginPresenter;
import presenter.view.LoginView;
import util.SessionManager;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.email_edit_box) EditText email_et;
    @BindView(R.id.password_edit_box) EditText pw_et;
    @BindString(R.string.error_not_exist_input_txt) String notExistErrorStr;
    @BindString(R.string.register_error_input_email_txt) String inputEmailErrorStr;
    @BindString(R.string.register_error_input_pw_txt) String inputPwErrorStr;

    private FirebaseAuth mAuth;
    private LoginPresenter loginPresenter;
    private SessionManager sessionManager;

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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        sessionManager = new SessionManager(getApplicationContext());
        loginPresenter = new LoginPresenter(this, getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * login to Firebase
     * @param email
     * @param password
     */
    private void signIn(final String email, final String password){
        showLoading();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginPresenter.getUserDataForLogin(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getEmail());
                        } else {
                            hideLoading();
                            showMessage("Sign In Fail From Firebase");
                        }
                    } } );
    }

    @Override
    public void goMainActivity(){
        hideLoading();
        sessionManager.setLogin(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void loginClick(){
        String emailStr = email_et.getText().toString().trim();
        String pwStr = pw_et.getText().toString().trim();

        if(emailStr.equals("") || pwStr.equals("")){
            showMessage(notExistErrorStr);
        }else if(!emailStr.contains("@") || !emailStr.contains(".com")){
            showMessage(inputEmailErrorStr);
        }else if(pwStr.length()<6){
            showMessage(inputPwErrorStr);
        }else{
            //loadingDialog.show();
            signIn(emailStr, pwStr);
        }
    }

    @OnClick(R.id.login_btn) void loginBtn(){
        loginClick();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
