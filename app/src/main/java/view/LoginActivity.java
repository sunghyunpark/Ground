package view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yssh.ground.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.LoginManager;
import util.Util;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_box) EditText email_et;
    @BindView(R.id.password_edit_box) EditText pw_et;
    @BindString(R.string.error_not_exist_input_txt) String notExistErrorStr;
    @BindString(R.string.register_error_input_email_txt) String inputEmailErrorStr;
    @BindString(R.string.register_error_input_pw_txt) String inputPwErrorStr;

    private LoginManager loginManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        loginManager = new LoginManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * login to Firebase
     * @param email
     * @param password
     */
    private void signIn(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginManager.getUserDataForLogin(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getEmail());
                        } else {
                            Util.showToast(getApplicationContext(), "Sign In Fail From Firebase");
                        }
                    } } );
    }

    @OnClick(R.id.login_btn) void loginBtn(){
        String emailStr = email_et.getText().toString().trim();
        String pwStr = pw_et.getText().toString().trim();

        if(emailStr.equals("") || pwStr.equals("")){
            Util.showToast(getApplicationContext(), notExistErrorStr);
        }else if(!emailStr.contains("@") || !emailStr.contains(".com")){
            Util.showToast(getApplicationContext(), inputEmailErrorStr);
        }else if(pwStr.length()<6){
            Util.showToast(getApplicationContext(), inputPwErrorStr);
        }else{
            //loadingDialog.show();
            signIn(emailStr, pwStr);
        }
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
