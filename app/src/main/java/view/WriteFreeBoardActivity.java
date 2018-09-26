package view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.view.WriteFreeBoardView;

public class WriteFreeBoardActivity extends BaseActivity implements WriteFreeBoardView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free_board);
        ButterKnife.bind(this);

    }

    @Override
    public void WriteBoard(){

    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
