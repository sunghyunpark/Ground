package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAreaBoardActivity extends AppCompatActivity {

    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String area = intent.getExtras().getString("area");
        int areaNo = intent.getIntExtra("areaNo", 0);

        init(area, areaNo);
    }

    private void init(String area, int areaNo){
        title_tv.setText(area);
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
