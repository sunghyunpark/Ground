package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.UserModel;

public class DetailMoreDialog extends Dialog {

    private String uid;

    @BindView(R.id.edit_article_tv) TextView edit_tv;
    @BindView(R.id.delete_article_tv) TextView delete_tv;
    @BindView(R.id.report_tv) TextView report_tv;

    public DetailMoreDialog(Context context, String uid){
        super(context);
        this.uid = uid;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.detail_more_dialog);

        ButterKnife.bind(this);

        init();

    }

    private void init(){
        if(UserModel.getInstance().getUid().equals(uid)){
            //내 글
            report_tv.setVisibility(View.GONE);
        }else{
            //내 글이 아닌 경우
            edit_tv.setVisibility(View.GONE);
            delete_tv.setVisibility(View.GONE);
        }
    }
}
