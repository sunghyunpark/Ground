package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.groundmobile.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemoHelpDialog extends Dialog {

    public MemoHelpDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.memo_help_dialog);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.ok_btn) void okBtn(){
        dismiss();
    }

}
