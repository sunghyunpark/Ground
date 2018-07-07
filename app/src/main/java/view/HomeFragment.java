package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.RecentBoardViewPagerAdapter;

public class HomeFragment extends BaseFragment {

    private View v;

    @BindView(R.id.recent_tab_layout) TabLayout recent_tabLayout;
    @BindView(R.id.recent_pager) ViewPager recent_pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        init();

        return v;
    }

    private void init(){
        RecentBoardViewPagerAdapter pagerAdapter = new RecentBoardViewPagerAdapter(getChildFragmentManager());
        recent_pager.setAdapter(pagerAdapter);
        recent_tabLayout.setupWithViewPager(recent_pager);
    }

}
