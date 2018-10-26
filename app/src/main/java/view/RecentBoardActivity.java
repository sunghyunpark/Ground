package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.RecentBoardViewPagerAdapter;

public class RecentBoardActivity extends BaseActivity {

    private static final int REQUEST_DETAIL = 2000;
    private static final int RECENT_LOAD_DATA = 20;

    @BindView(R.id.recent_tab_layout) TabLayout recent_tabLayout;
    @BindView(R.id.recent_pager) ViewPager recent_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_board);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        RecentBoardViewPagerAdapter recentBoardViewPagerAdapter = new RecentBoardViewPagerAdapter(getSupportFragmentManager(), true, RECENT_LOAD_DATA);
        recent_pager.setAdapter(recentBoardViewPagerAdapter);
        recent_tabLayout.setupWithViewPager(recent_pager);
    }
}
