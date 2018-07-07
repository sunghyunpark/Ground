package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends BaseActivity implements LoginView {

    @BindView(R.id.email_edit_box) EditText email_et;
    @BindView(R.id.password_edit_box) EditText pw_et;
    @BindView(R.id.name_edit_box) EditText name_et;
    @BindString(R.string.error_not_exist_input_txt) String notExistErrorStr;
    @BindString(R.string.register_error_input_email_txt) String inputEmailErrorStr;
    @BindString(R.string.register_error_input_pw_txt) String inputPwErrorStr;

    private final static String TAG = "RegisterActivity";
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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        sessionManager = new SessionManager(getApplicationContext());
        loginPresenter = new LoginPresenter(this, getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * register to Firebase
     * @param email
     * @param password
     */
    private void createAccount(String email, String password, final String nickName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG, mAuth.getCurrentUser().getUid());
                            loginPresenter.postUserDataForRegister(mAuth.getCurrentUser().getUid(), "email", nickName);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "이미 동일한 계정이 존재합니다.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void loginClick(){
        String emailStr = email_et.getText().toString().trim();
        String pwStr = pw_et.getText().toString().trim();
        String nameStr = name_et.getText().toString().trim();

        if(emailStr.equals("") || pwStr.equals("")){
            Toast.makeText(getApplicationContext(), notExistErrorStr, Toast.LENGTH_SHORT).show();
        }else if(!emailStr.contains("@") || !emailStr.contains(".com")){
            Toast.makeText(getApplicationContext(), inputEmailErrorStr,Toast.LENGTH_SHORT).show();
        }else if(pwStr.length()<6){
            Toast.makeText(getApplicationContext(), inputPwErrorStr, Toast.LENGTH_SHORT).show();
        }else{
            createAccount(emailStr, pwStr, nameStr);
        }
    }

    @Override
    public void goMainActivity(){
        sessionManager.setLogin(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 가입하기 버튼 클릭 시 간단하게 검증 후 firebase 로 전송함과 동시에 ground server 로 보낸다.
     */
    @OnClick(R.id.register_btn) void registerBtn(){
        loginClick();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
