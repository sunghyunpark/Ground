package util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import view.recentPager.RecentHireBoardFragment;
import view.recentPager.RecentMatchBoardFragment;
import view.recentPager.RecentRecruitBoardFragment;

public class RecentBoardViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public RecentBoardViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return RecentMatchBoardFragment.newInstance();
            case 1:
                return RecentHireBoardFragment.newInstance();
            case 2:
                return RecentRecruitBoardFragment.newInstance();
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

