package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import view.EditBoardActivity;

public class DetailMoreDialog extends Dialog {

    private String uid;
    private String title, contents;
    private String boardType, area;
    private int areaNo;

    @BindView(R.id.edit_article_tv) TextView edit_tv;
    @BindView(R.id.delete_article_tv) TextView delete_tv;
    @BindView(R.id.report_tv) TextView report_tv;

    public DetailMoreDialog(Context context, String uid, String title, String contents, String boardType, String area, int areaNo){
        super(context);
        this.uid = uid;
        this.title = title;
        this.contents = contents;
        this.boardType = boardType;
        this.area = area;
        this.areaNo = areaNo;
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

    @OnClick(R.id.edit_article_tv) void editBtn(){
        Intent intent = new Intent(getContext(), EditBoardActivity.class);
        intent.putExtra("boardType", boardType);
        intent.putExtra("area", area);
        intent.putExtra("areaNo", areaNo);
        intent.putExtra("title", title);
        intent.putExtra("contents", contents);
        getContext().startActivity(intent);
        dismiss();
    }

    @OnClick(R.id.delete_article_tv) void deleteBtn(){

    }

    @OnClick(R.id.favorite_tv) void favoriteBtn(){

    }

    @OnClick(R.id.report_tv) void reportBtn(){

    }
}
