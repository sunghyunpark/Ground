package view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.BoardAreaAdapter;
import view.boardPager.HireFragment;
import view.boardPager.MatchFragment;
import view.boardPager.RecruitFragment;

public class BoardFragment extends Fragment {

    private static final int NUM_PAGES = 3;//페이지 수
    private int temp = 0; //현재 페이지

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, v);

        init();
        return v;
    }

    private void init(){
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return MatchFragment.newInstance();
                case 1:
                    return HireFragment.newInstance();
                case 2:
                    return RecruitFragment.newInstance();
                    default:
                        return null;
            }
        }

        @Override
        public int getCount(){
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "매칭";
                case 1:
                    return "용병";
                case 2:
                    return "모집";
                    default:
                        return null;
            }
        }
    }
}
