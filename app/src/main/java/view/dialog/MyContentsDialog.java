package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.yssh.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import util.Util;

public class MyContentsDialog extends Dialog{

    public MyContentsDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.my_contents_dialog);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.my_contents_tv) void goToMyContentsListPage(){
        Util.showToast(getContext(), "내 글");
    }

    @OnClick(R.id.my_comments_tv) void goToMyCommentsListPage(){
        Util.showToast(getContext(), "내 댓글");
    }

    @OnClick(R.id.my_favorite_tv) void goToMyFavoriteListPage(){
        Util.showToast(getContext(), "관심");
    }
}
