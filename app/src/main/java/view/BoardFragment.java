package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import view.boardPager.HireFragment;
import view.boardPager.MatchFragment;
import view.boardPager.RecruitFragment;
import view.dialog.MyContentsDialog;

public class BoardFragment extends Fragment {

    private static final int NUM_PAGES = 3;//페이지 수
    private MyContentsDialog myContentsDialog;

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
        myContentsDialog = new MyContentsDialog(getContext());
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

    @OnClick(R.id.my_btn) void showMyContentsDialog(){
        myContentsDialog.show();

        /*
        myContentsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });*/
    }
}
