package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import view.MyActivity;

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
        Intent intent = new Intent(getContext(), MyActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_ARTICLE_TYPE);
        getContext().startActivity(intent);
        this.cancel();
    }

    @OnClick(R.id.my_comments_tv) void goToMyCommentsListPage(){
        Intent intent = new Intent(getContext(), MyActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_COMMENT_TYPE);
        getContext().startActivity(intent);
        this.cancel();
    }

    @OnClick(R.id.my_favorite_tv) void goToMyFavoriteListPage(){
        Intent intent = new Intent(getContext(), MyActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_FAVORITE_TYPE);
        getContext().startActivity(intent);
        this.cancel();
    }
}
