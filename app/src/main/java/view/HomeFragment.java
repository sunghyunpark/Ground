package view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import java.util.ArrayList;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.BannerModel;
import presenter.view.HomeView;
import util.Util;
import util.adapter.AreaBoardAdapter;
import util.adapter.BannerViewPagerAdapter;
import util.adapter.RecentBoardViewPagerAdapter;

public class HomeFragment extends BaseFragment implements HomeView{

    private View v;
    private ArrayList<BannerModel> bannerModelArrayList;
    private static final int SEND_RUNNING = 1000;
    private Handler handler;

    @BindView(R.id.banner_pager) ViewPager banner_pager;
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
        setRecentArticlePager();
        setBannerPager();

    }

    /**
     * 최신글 영역
     */
    @Override
    public void setRecentArticlePager(){
        RecentBoardViewPagerAdapter pagerAdapter = new RecentBoardViewPagerAdapter(getChildFragmentManager());
        recent_pager.setAdapter(pagerAdapter);
        recent_tabLayout.setupWithViewPager(recent_pager);
    }

    /**
     * 최상단 배너 영역
     */
    @Override
    public void setBannerPager(){
        BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(getContext(), bannerModelArrayList, 3);
        banner_pager.setAdapter(bannerViewPagerAdapter);

        banner_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position < 3)        //1번째 아이템에서 마지막 아이템으로 이동하면
                    banner_pager.setCurrentItem(position+3, false); //이동 애니메이션을 제거 해야 한다
                else if(position >= 3*2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                    banner_pager.setCurrentItem(position - 3, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Util.BannerHandler(this, banner_pager, 3);
        BannerThread thread = new BannerThread();
        thread.start();
    }

    private class BannerThread extends java.lang.Thread{
        boolean stopped = false;

        private BannerThread(){
            this.stopped = false;
        }

        private void stopThread(){
            this.stopped = true;
        }

        @Override
        public void run(){
            super.run();
            while (!stopped){
                Message message = handler.obtainMessage();
                message.what = SEND_RUNNING;
                handler.sendMessage(message);
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
