package util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import view.recentPager.RecentHireBoardFragment;
import view.recentPager.RecentMatchBoardFragment;
import view.recentPager.RecentRecruitBoardFragment;

public class RecentBoardViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;
    private int limit;    // 홈에서 보이는 최신글과 더보기를 통해 진입했을 경우 불러오는 데이터 갯수가 다르기 때문에 사용

    public RecentBoardViewPagerAdapter(FragmentManager fm, int limit){
        super(fm);
        this.limit = limit;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return RecentMatchBoardFragment.newInstance(limit);
            case 1:
                return RecentHireBoardFragment.newInstance(limit);
            case 2:
                return RecentRecruitBoardFragment.newInstance(limit);
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

