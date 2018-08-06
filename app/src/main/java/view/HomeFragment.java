package view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import util.adapter.BannerViewPagerAdapter;
import util.adapter.GroundUtilAdapter;
import util.adapter.RecentBoardViewPagerAdapter;

public class HomeFragment extends BaseFragment implements HomeView{

    private RecentBoardViewPagerAdapter pagerAdapter;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private ArrayList<BannerModel> bannerModelArrayList;

    /* 메모리 관련 이슈때문에 잠시 주석처리
    private static final int SEND_RUNNING = 1000;
    private Handler handler;
    private BannerThread thread;
    */

    @BindView(R.id.banner_pager) ViewPager banner_pager;
    @BindView(R.id.recent_tab_layout) TabLayout recent_tabLayout;
    @BindView(R.id.recent_pager) ViewPager recent_pager;
    @BindView(R.id.ground_util_recyclerView) RecyclerView groundUtilRecyclerView;

    @Override
    public void onDestroy(){
        super.onDestroy();
        /* 메모리 관련 이슈때문에 잠시 주석처리
        if(thread != null){
            thread.stopThread();
            this.handler.removeMessages(0);
        }
        */
    }

    @Override
    public void onPause(){
        super.onPause();
        pagerAdapter = null;
        bannerViewPagerAdapter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        initUI();
        return v;
    }

    private void init(){
        pagerAdapter = new RecentBoardViewPagerAdapter(getChildFragmentManager());
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getContext(), bannerModelArrayList, 3);
    }

    private void initUI(){
        setRecentArticlePager();
        setBannerPager();
        setGroundRecyclerView();
    }

    /**
     * 최신글 영역
     * 최신글 영역의 TabLayout 과 viewPager를 초기화한다.
     */
    @Override
    public void setRecentArticlePager(){
        recent_pager.setAdapter(pagerAdapter);
        recent_pager.setOffscreenPageLimit(3);
        recent_tabLayout.setupWithViewPager(recent_pager);
    }

    /**
     * 상단 그라운드 유틸 영역
     * 가로 리사이클러뷰
     */
    @Override
    public void setGroundRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GroundUtilAdapter groundUtilAdapter = new GroundUtilAdapter(getContext());

        groundUtilRecyclerView.setAdapter(groundUtilAdapter);
        groundUtilRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 최상단 배너 영역
     */
    @Override
    public void setBannerPager(){
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

        /*메모리 관련 이슈때문에 잠시 주석처리
        handler = new Util.BannerHandler(this, banner_pager, 3);
        thread = new BannerThread();
        thread.start();
        */
    }

    /* 메모리 관련 이슈때문에 잠시 주석처리
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
    */

}
