package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.groundmobile.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.background_layout) ViewGroup background_vg;

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.register_btn:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.login_btn:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
    }
}
