package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.view.MyView;
import util.adapter.MyViewPagerAdapter;

public class MyContentsActivity extends BaseActivity implements MyView{

    private String type;    //내가 쓴 글, 내가 쓴 댓글, 관심 글(myArticle, myComment, myFavorite)

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.my_title_tv) TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contents);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getExtras().getString(Constants.EXTRA_MY_TYPE);

        init();
    }

    private void init(){
        if(type.equals(Constants.MY_ARTICLE_TYPE)){
            title_tv.setText("내가 작성한 글");
        }else if(type.equals(Constants.MY_COMMENT_TYPE)){
            title_tv.setText("내가 작성한 댓글");
        }else if(type.equals(Constants.MY_FAVORITE_TYPE)){
            title_tv.setText("나의 관심 글");
        }
        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), type);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
