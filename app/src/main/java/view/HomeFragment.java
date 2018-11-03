package view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.BannerModel;
import model.MatchArticleModel;
import model.UserModel;
import model.YouTubeModel;
import presenter.HomePresenter;
import presenter.view.HomeView;
import util.Util;
import util.adapter.BannerViewPagerAdapter;
import util.adapter.GroundUtilAdapter;
import util.adapter.RecentBoardViewPagerAdapter;
import util.adapter.RecommendYouTubeViewPagerAdapter;
import util.adapter.TodayMatchAdapter;

public class HomeFragment extends BaseFragment implements HomeView{

    private static final int RECENT_LOAD_DATA = 3;    // 홈 > 최신글에서 보여지는 글의 갯수
    private static final int TODAY_MATCH_LOAD_DATA = 3;    // 홈 > 오늘의 시합에서 보여지는 글의 갯수
    private static final int SEND_RUNNING = 1000;    // 슬라이드 광고 handler message

    private RecentBoardViewPagerAdapter recentBoardViewPagerAdapter;    // 최신글 뷰페이저 어뎁터
    private BannerViewPagerAdapter bannerViewPagerAdapter;    // 상단 배너 뷰페이저 어뎁터
    private TodayMatchAdapter todayMatchAdapter;    // 오늘의 시합 어뎁터
    private ArrayList<YouTubeModel> youTubeModelArrayList;    // 이런 영상 어때요? 데이터 List
    private ArrayList<String> groundUtilUpdateList;
    private HomePresenter homePresenter;

    private Handler handler;
    private BannerThread thread;

    @BindView(R.id.banner_pager) ViewPager banner_pager;
    @BindView(R.id.recent_tab_layout) TabLayout recent_tabLayout;
    @BindView(R.id.recent_pager) ViewPager recent_pager;
    @BindView(R.id.ground_util_recyclerView) RecyclerView groundUtilRecyclerView;
    @BindView(R.id.today_match_recyclerView) RecyclerView todayMatchRecyclerView;
    @BindView(R.id.recommend_youtube_pager) ViewPager recommendYouTubePager;
    @BindView(R.id.recommend_youtube_layout) ViewGroup recommend_youtube_layout;
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
        recentBoardViewPagerAdapter = null;
        bannerViewPagerAdapter = null;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    /**
     * onResume 에서 오늘의 시합 데이터를 받아온다.
     */
    @Override
    public void onResume(){
        super.onResume();
        homePresenter.loadTodayMatchList(true, 0, TODAY_MATCH_LOAD_DATA);
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

    /**
     * - 최상단 슬라이드 배너
     * : HomePresenter 사용하여 서버로부터 최상단 슬라이드 배너, 최신글 하단 띠 배너, 오늘의 시합 하단 띠 배너 데이터를 받아온다.
     *
     * - HomePresenter
     * : 초기화
     *
     * - 오늘의 시합
     * : 오늘의 시합 List, 어뎁터 초기화
     *
     * - 최신글
     * : 뷰페이저 어뎁터 초기화
     *
     * - 이런 영상은 어때요?
     * : 유튜브 List, 유튜브 데이터를 받아온다.
     *
     * - 그라운드
     * : 그라운드 유틸 List 및 담을 데이터 초기화
     */
    private void init(){
        // 오늘의 시합 데이터를 담을 리스트를 초기화 한 뒤 어뎁터에 넘겨준다.
        ArrayList<MatchArticleModel> todayMatchArticleModelArrayList = new ArrayList<MatchArticleModel>();
        todayMatchAdapter = new TodayMatchAdapter(getContext(), todayMatchArticleModelArrayList, false, new TodayMatchAdapter.todayMatchListener() {
            @Override
            public void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel) {
                Intent intent = new Intent(getContext(), DetailMatchArticleActivity.class);
                intent.putExtra(Constants.EXTRA_AREA_NAME, area);
                intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                intent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, true);
                intent.putExtra(Constants.EXTRA_USER_ID, UserModel.getInstance().getUid());
                startActivity(intent);
            }
        });

        // HomePresenter 를 초기화 한다.
        homePresenter = new HomePresenter(this, getContext(), todayMatchArticleModelArrayList);

        // 최신글 뷰페이저 어뎁터를 초기화 한다.
        recentBoardViewPagerAdapter = new RecentBoardViewPagerAdapter(getChildFragmentManager(), false,  RECENT_LOAD_DATA);

        // 홈 최상단 광고 슬라이드 배너를 담을 리스트를 초기화 한 뒤 서버로 부터 받아온다.
        // 최신글, 오늘의 시합 하단 띠 배너들은 별도 리스트를 여기서 넘겨주지 않는다.
        // HomePresenter 에서 최상단 광고 슬라이드 리스트, 최신글, 오늘의 시합 띠 배너를 받아온 뒤 getView.setBanner() 를 통해 받아온다.
        ArrayList<BannerModel> mainBannerList = new ArrayList<BannerModel>();
        homePresenter.loadBannerList(mainBannerList);

        // 유튜브 데이터를 담을 리스트를 초기화 한뒤 서버로 부터 받아온다.
        youTubeModelArrayList = new ArrayList<>();
        homePresenter.loadRecommendYouTubeList(youTubeModelArrayList);

        // 그라운드유틸 업데이트 시간을 모두 디폴트로 초기화한다.
        groundUtilUpdateList = new ArrayList<>();
        for(int i=0;i<4;i++){
            groundUtilUpdateList.add(Constants.DEFAULT_TIME_FORMAT);
        }
    }

    private void initUI(){
        // 최신글 영역 초기화
        setRecentArticlePager();

        // 그라운드 유틸 업데이트 시간 초기화
        homePresenter.loadGroundUtilData(groundUtilUpdateList);

        // 오늘의 시합 recyclerView 초기화.
        setTodayMatchBoard();
    }

    /**
     * 최신글 영역 초기화
     * 최신글 viewPager 와 TabLayout 을 연결한다.
     */
    @Override
    public void setRecentArticlePager(){
        recent_pager.setAdapter(recentBoardViewPagerAdapter);
        recent_tabLayout.setupWithViewPager(recent_pager);
    }

    /**
     * 상단 그라운드 유틸 영역
     * 가로 리사이클러뷰
     * HomePresenter 에서 그라운드유틸 데이터를 받아온뒤에 셋팅이된다.
     */
    @Override
    public void setGroundRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GroundUtilAdapter groundUtilAdapter = new GroundUtilAdapter(getContext(), groundUtilUpdateList);
        groundUtilRecyclerView.setAdapter(groundUtilAdapter);
        groundUtilRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * HomePresenter 에서 서버로부터 받아온 데이터들을 넘겨 받아 적용한다.
     * - 최상단 광고 슬라이드
     * - 최신글 하단 띠 배너
     * - 오늘의 시합 하단 띠 배너
     */
    @Override
    public void setBanner(ArrayList<BannerModel> bannerModelArrayList, BannerModel RBBanner, BannerModel TBBanner){
        // 최상단 광고 슬라이드 뷰페이저를 연결하고 자동 슬라이드 적용한다.
        final int listSize = bannerModelArrayList.size();
        requireTopSlideBannerTargetAPI();

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

        // 최신글 하단 띠 배너 적용
        setRecentBoardBanner(RBBanner);

        // 오늘의 시합 하단 띠 배너 적용
        setTodayMatchBoardBanner(TBBanner);

    }

    // setNestedScrollingEnable() 함수 자체가 21이상 부터 사용이 가능하여 TargetApi 를 적용함.
    @TargetApi(21)
    private void requireTopSlideBannerTargetAPI(){
        banner_pager.setNestedScrollingEnabled(false);
    }

    /**
     * HomePresenter 에서 서버로부터 받아온 RBBanner 를 적용한다.
     * banner type 이 off 이면 해당 배너를 Gone 하여 미노출한다.
     */
    private void setRecentBoardBanner(BannerModel RBBanner){
        if(RBBanner.getType().equals("off")){
            recommendBanner_iv.setVisibility(View.GONE);
        }else{
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(GroundApplication.getAppContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(GroundApplication.GROUND_DEV_API+RBBanner.getImgPath())
                    .apply(new RequestOptions().transform(new RoundedCorners(20)))
                    .into(recommendBanner_iv);
        }
    }

    /**
     * HomePresenter 에서 서버로부터 받아온 TBBanner 를 적용한다.
     * banner type 이 off 이면 해당 배너를 Gone 하여 미노출한다.
     */
    private void setTodayMatchBoardBanner(final BannerModel TBBanner){
        if(TBBanner.getType().equals("off")){
            chatbotBanner_iv.setVisibility(View.GONE);
        }else{
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(GroundApplication.getAppContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(GroundApplication.GROUND_DEV_API+TBBanner.getImgPath())
                    .apply(new RequestOptions().transform(new RoundedCorners(20)))
                    .into(chatbotBanner_iv);

            chatbotBanner_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(TBBanner.getUrl()));
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 오늘의 시합
     * recyclerView 초기화한다.
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
            if(listSize >= TODAY_MATCH_LOAD_DATA){
                // 게시글이 3개 이상인 경우 '더보기' 버튼을 노출한다.
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

    /**
     * 이런 영상은 어때요?
     * - state > 서버 컨트롤을 위해 off 일 땐 '이런 영상은 어때요?' 영역 자체를 Gone 처리.
     * @param state
     */
    @Override
    public void setRecommendYouTube(String state){
        if(state.equals("off")){
            recommend_youtube_layout.setVisibility(View.GONE);
        }else{

            RecommendYouTubeViewPagerAdapter recommendYouTubeViewPagerAdapter = new RecommendYouTubeViewPagerAdapter(getContext(), youTubeModelArrayList);
            float density = getResources().getDisplayMetrics().density;
            int pageMargin = 8 * (int)density; // 8dp

            requireRecommendYouTubeTargetAPI();
            recommendYouTubePager.setPageMargin(pageMargin);
            recommendYouTubePager.setClipToPadding(false);
            recommendYouTubePager.setPadding(60, 0, 300, 0);
            recommendYouTubePager.setAdapter(recommendYouTubeViewPagerAdapter);


            /*
            RecommendYouTubeSlideAdapter recommendYouTubeSlideAdapter = new RecommendYouTubeSlideAdapter(getContext(), youTubeModelArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recommendYouTubeRecyclerView.setLayoutManager(linearLayoutManager);
            recommendYouTubeRecyclerView.setAdapter(recommendYouTubeSlideAdapter);
            recommendYouTubeRecyclerView.setNestedScrollingEnabled(false);
            */
        }
    }
    
    // setNestedScrollingEnable() 함수 자체가 21이상 부터 사용이 가능하여 TargetApi 를 적용함.
    @TargetApi(21)
    private void requireRecommendYouTubeTargetAPI(){
        recommendYouTubePager.setNestedScrollingEnabled(false);
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
            homePresenter.loadTodayMatchList(true, 0, TODAY_MATCH_LOAD_DATA);
        }
    }

    @OnClick(R.id.recent_more_btn) void recentMoreBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            Intent intent = new Intent(getContext(), RecentBoardActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.today_match_more_btn) void todayMatchMoreBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            Intent intent = new Intent(getContext(), TodayBoardActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.my_btn) void myBtn(){
        Intent intent = new Intent(getContext(), MyActivity.class);
        startActivity(intent);
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
