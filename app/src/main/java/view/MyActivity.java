package view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @OnClick({R.id.back_btn, R.id.my_article_layout, R.id.my_comment_layout, R.id.my_favorite_layout, R.id.memo_layout}) void Click(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.my_article_layout:
                Intent myArticleIntent = new Intent(getApplicationContext(), MyContentsActivity.class);
                myArticleIntent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_ARTICLE_TYPE);
                startActivity(myArticleIntent);
                break;
            case R.id.my_comment_layout:
                Intent myCommentIntent = new Intent(getApplicationContext(), MyContentsActivity.class);
                myCommentIntent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_COMMENT_TYPE);
                startActivity(myCommentIntent);
                break;
            case R.id.my_favorite_layout:
                Intent myFavoriteIntent = new Intent(getApplicationContext(), MyContentsActivity.class);
                myFavoriteIntent.putExtra(Constants.EXTRA_MY_TYPE, Constants.MY_FAVORITE_TYPE);
                startActivity(myFavoriteIntent);
                break;
            case R.id.memo_layout:
                Intent memoIntent = new Intent(getApplicationContext(), MemoActivity.class);
                startActivity(memoIntent);
                break;
        }
    }
}
