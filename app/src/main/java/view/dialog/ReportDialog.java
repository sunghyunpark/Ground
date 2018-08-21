package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.yssh.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportDialog extends Dialog {

    private String from, boardType;    // from 의 경우 게시글 신고 / 댓글 신고 구분짓는다. (article / comment)
    private int articleNo, commentNo;

    public ReportDialog(Context context, String from, String boardType, int articleNo, int commentNo){
        super(context);
        this.from = from;
        this.boardType = boardType;
        this.articleNo = articleNo;
        this.commentNo = commentNo;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.report_dialog);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.cancel_btn) void cancelBtn(){
        dismiss();
    }

    @OnClick(R.id.report_btn) void reportBtn(){
        dismiss();
    }

}
