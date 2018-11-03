package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

    @OnClick(R.id.my_article_layout) void myArticleLayoutClick(){
        Intent intent = new Intent(getApplicationContext(), MyContentsActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_ARTICLE_TYPE);
        startActivity(intent);
    }

    @OnClick(R.id.my_comment_layout) void myCommentLayoutClick(){
        Intent intent = new Intent(getApplicationContext(), MyContentsActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_COMMENT_TYPE);
        startActivity(intent);
    }

    @OnClick(R.id.my_favorite_layout) void myFavoriteLayoutClick(){
        Intent intent = new Intent(getApplicationContext(), MyContentsActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_FAVORITE_TYPE);
        startActivity(intent);
    }

    @OnClick(R.id.memo_layout) void memoLayoutClick(){
        Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
        startActivity(intent);
    }

}
