package util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import view.boardPager.HireFragment;
import view.boardPager.MatchFragment;
import view.boardPager.RecruitFragment;

public class BoardViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public BoardViewPagerAdapter(FragmentManager fm){
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
