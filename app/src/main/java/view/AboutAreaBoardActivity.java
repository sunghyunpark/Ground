package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SessionManager;
import util.Util;

public class AboutAreaBoardActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private String area;
    private int areaNo;

    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo", 0);

        init(area, areaNo);
    }

    private void init(String area, int areaNo){
        sessionManager = new SessionManager(getApplicationContext());

        title_tv.setText(area);
    }

    @OnClick(R.id.write_btn) void goWrite(){
        if(sessionManager.isLoggedIn()){
            //login
            Intent intent = new Intent(getApplicationContext(), WriteBoardActivity.class);
            intent.putExtra("area", area);
            intent.putExtra("areaNo", areaNo);
            startActivity(intent);
        }else{
            //not login
            Util.showToast(getApplicationContext(), "로그인을 해주세요.");
        }
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
