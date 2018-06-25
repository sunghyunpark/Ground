package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutBoardActivity extends AppCompatActivity {

    private String area;
    private int areaNo;

    @BindView(R.id.area_tv) TextView area_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo", 0);

        init();

    }

    private void init(){
        area_tv.setText(area);

    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
