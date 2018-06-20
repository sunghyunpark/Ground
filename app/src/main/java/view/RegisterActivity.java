package view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_box) EditText email_et;
    @BindView(R.id.password_edit_box) EditText pw_et;
    @BindView(R.id.name_edit_box) EditText name_et;
    @BindString(R.string.error_not_exist_input_txt) String notExistErrorStr;
    @BindString(R.string.register_error_input_email_txt) String inputEmailErrorStr;
    @BindString(R.string.register_error_input_pw_txt) String inputPwErrorStr;

    private final static String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * register to Firebase
     * @param email
     * @param password
     */
    private void createAccount(String email, String password, final String name) {
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
                            LoginManager loginManager = new LoginManager(getApplicationContext());
                            loginManager.postUserDataForRegister();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "이미 동일한 계정이 존재합니다.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * 가입하기 버튼 클릭 시 간단하게 검증 후 firebase 로 전송함과 동시에 ground server 로 보낸다.
     */
    @OnClick(R.id.register_btn) void register(){
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

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
