package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import util.BoardManager;
import util.Util;

public class WriteBoardActivity extends AppCompatActivity {

    private String area;
    private int areaNo;
    private BoardManager boardManager;

    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.board_title_et) EditText board_title_et;
    @BindView(R.id.board_contents_et) EditText board_contents_et;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo",0);

        init();
    }

    private void init(){
        boardManager = new BoardManager(getApplicationContext());
        area_tv.setText(area);
    }

    @OnClick(R.id.write_btn) void write(){
        String titleStr = board_title_et.getText().toString().trim();
        String contentsStr = board_contents_et.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("")){
            Util.showToast(getApplicationContext(), errorNotExistInputStr);
        }else{
            boardManager.postMatchingBoard(areaNo, UserModel.getInstance().getUid(), titleStr, contentsStr);
            finish();
        }
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
