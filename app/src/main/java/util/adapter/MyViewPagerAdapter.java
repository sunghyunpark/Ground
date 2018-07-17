package util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import view.myFragment.HireFragment;
import view.myFragment.MatchFragment;
import view.myFragment.RecruitFragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;
    private String type;

    public MyViewPagerAdapter(FragmentManager fm, String type){
        super(fm);
        this.type = type;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return MatchFragment.newInstance(type);
            case 1:
                return HireFragment.newInstance(type);
            case 2:
                return RecruitFragment.newInstance(type);
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

