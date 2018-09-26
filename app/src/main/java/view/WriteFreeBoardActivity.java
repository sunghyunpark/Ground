package view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.WriteFreeBoardPresenter;
import presenter.view.WriteFreeBoardView;

public class WriteFreeBoardActivity extends BaseActivity implements WriteFreeBoardView{

    private WriteFreeBoardPresenter writeFreeBoardPresenter;

    @BindView(R.id.board_title_et) EditText title_et;
    @BindView(R.id.board_contents_et) EditText contents_et;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free_board);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        writeFreeBoardPresenter = new WriteFreeBoardPresenter(this, getApplicationContext());
    }

    @Override
    public void WriteBoard(){
        String titleStr = title_et.getText().toString().trim();
        String contentsStr = contents_et.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("")){
            showMessage(errorNotExistInputStr);
        }else{
            writeFreeBoardPresenter.writeFreeBoard(UserModel.getInstance().getUid(), titleStr, contentsStr, "N", "N");
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        WriteBoard();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
