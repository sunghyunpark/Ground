package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yssh.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import presenter.view.MyView;
import util.Util;
import util.adapter.MyViewPagerAdapter;

public class MyActivity extends BaseActivity implements MyView{

    private String type;    //내가 쓴 글, 내가 쓴 댓글, 관심 글(myArticle, myComment, myFavorite)

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        Util.showToast(getApplicationContext(), type);

        init();
    }

    private void init(){
        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), type);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
