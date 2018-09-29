package view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import model.BannerModel;
import presenter.HomePresenter;
import presenter.view.HomeView;
import util.Util;
import util.adapter.BannerViewPagerAdapter;
import util.adapter.GroundUtilAdapter;
import util.adapter.RecentBoardViewPagerAdapter;
import util.adapter.TodayMatchAdapter;

public class HomeFragment extends BaseFragment implements HomeView{

    private RecentBoardViewPagerAdapter pagerAdapter;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private TodayMatchAdapter todayMatchAdapter;
    private ArrayList<BannerModel> mainBannerList;
    private ArrayList<ArticleModel> todayArticleModelArrayList;
    private HomePresenter homePresenter;

    private static final int SEND_RUNNING = 1000;
    private Handler handler;
    private BannerThread thread;


    @BindView(R.id.banner_pager) ViewPager banner_pager;
    @BindView(R.id.recent_tab_layout) TabLayout recent_tabLayout;
    @BindView(R.id.recent_pager) ViewPager recent_pager;
    @BindView(R.id.ground_util_recyclerView) RecyclerView groundUtilRecyclerView;
    @BindView(R.id.today_match_recyclerView) RecyclerView todayMatchRecyclerView;
    @BindView(R.id.today_match_empty_tv) TextView todayMatchEmptyTv;
    @BindView(R.id.today_match_more_btn) Button todayMatchMoreBtn;
    @BindView(R.id.recommend_banner_iv) ImageView recommendBanner_iv;
    @BindView(R.id.chatbot_banner_iv) ImageView chatbotBanner_iv;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(thread != null){
            thread.stopThread();
            this.handler.removeMessages(0);
        }

        pagerAdapter = null;
        bannerViewPagerAdapter = null;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        homePresenter.loadTodayMatchList();
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
        todayArticleModelArrayList = new ArrayList<ArticleModel>();
        mainBannerList = new ArrayList<BannerModel>();
        homePresenter = new HomePresenter(this, getContext(), todayArticleModelArrayList);
        pagerAdapter = new RecentBoardViewPagerAdapter(getChildFragmentManager());
        homePresenter.loadMainBannerList(mainBannerList);    // 상단 슬라이드 배너 데이터 받아옴
        todayMatchAdapter = new TodayMatchAdapter(getContext(), todayArticleModelArrayList);
    }

    private void initUI(){
        setRecentArticlePager();
        setGroundRecyclerView();
        setTodayMatchBoard();
    }

    /**
     * 최신글 영역
     * 최신글 영역의 TabLayout 과 viewPager를 초기화한다.
     */
    @Override
    public void setRecentArticlePager(){
        recent_pager.setAdapter(pagerAdapter);
        //recent_pager.setOffscreenPageLimit(3);
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
     * 최상단 배너 영역, 최신글 하단, 오늘의 시합 하단 띠배너
     */
    @Override
    public void setBanner(ArrayList<BannerModel> bannerModelArrayList, BannerModel RBBanner, BannerModel TBBanner){
        final int listSize = bannerModelArrayList.size();
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getContext(), bannerModelArrayList);

        banner_pager.setAdapter(bannerViewPagerAdapter);
        banner_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position < listSize)        //1번째 아이템에서 마지막 아이템으로 이동하면
                    banner_pager.setCurrentItem(position+listSize, false); //이동 애니메이션을 제거 해야 한다
                else if(position >= listSize*2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                    banner_pager.setCurrentItem(position - listSize, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Util.BannerHandler(this, banner_pager, listSize);
        thread = new BannerThread();
        thread.start();

        setRecentBoardBanner(RBBanner);

        setTodayMatchBoardBanner(TBBanner);

    }

    /**
     * 최신글 하단 띠 배너
     */
    private void setRecentBoardBanner(BannerModel RBBanner){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(GroundApplication.GROUND_DEV_API+RBBanner.getImgPath())
                .into(recommendBanner_iv);
    }

    /**
     * 오늘의 시합 하단 띠 배너
     */
    private void setTodayMatchBoardBanner(BannerModel TBBanner){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(GroundApplication.GROUND_DEV_API+TBBanner.getImgPath())
                .into(chatbotBanner_iv);
        Log.d("todayBanner",GroundApplication.GROUND_DEV_API+TBBanner.getImgPath());
    }

    /**
     * 오늘의 시합
     */
    @Override
    public void setTodayMatchBoard(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        todayMatchRecyclerView.setLayoutManager(linearLayoutManager);
        todayMatchRecyclerView.setAdapter(todayMatchAdapter);
        todayMatchRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 받아온 오늘의 시합 데이터를 갱신한다.
     */
    @Override
    public void notifyTodayMatchArticle(boolean hasData, int listSize){
        if(hasData){
            todayMatchAdapter.notifyDataSetChanged();
            todayMatchEmptyTv.setVisibility(View.GONE);
            todayMatchRecyclerView.setVisibility(View.VISIBLE);
            if(listSize >= 5){
                todayMatchMoreBtn.setVisibility(View.VISIBLE);
            }else{
                todayMatchMoreBtn.setVisibility(View.GONE);
            }
        }else{
            todayMatchMoreBtn.setVisibility(View.GONE);
            todayMatchEmptyTv.setVisibility(View.VISIBLE);
            todayMatchRecyclerView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.recommend_banner_iv) void recommendBannerBtn(){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        //String subject = "문자의 제목";
        String text = "https://play.google.com/store/apps/details?id="+getContext().getPackageName();
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // Title of intent
        Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
        startActivity(chooser);
    }

    @OnClick(R.id.recent_refresh_btn) void recentRefreshBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            setRecentArticlePager();
        }
    }

    @OnClick(R.id.today_match_refresh_btn) void todayMatchRefreshBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            homePresenter.loadTodayMatchList();
        }
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
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage();
                message.what = SEND_RUNNING;
                handler.sendMessage(message);
            }
        }
    }


}
