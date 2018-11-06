package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchArticleModel;
import model.BannerModel;
import model.UserModel;
import presenter.AreaBoardPresenter;
import presenter.view.AreaBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.AreaBoardAdapter;
import util.adapter.BannerViewPagerAdapter;

/**
 * 임의의 지역 > 게시판 리스트
 */
public class MatchAreaBoardActivity extends BaseActivity implements AreaBoardView, SwipeRefreshLayout.OnRefreshListener{

    // onActivityResult 에 사용되는 변수들
    private static final int REQUEST_WRITE = 1000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int RESULT_DELETE = 3000;

    // 게시글을 받아올 갯수
    private static final int LOAD_DATA_COUNT = 10;

    // 정렬 변수
    private static final String SORT_ALL = "all";
    private static final String SORT_MATCH_DATE = "matchDate";
    private static final String SORT_NOT_MATCH_STATE = "matchState";

    // 시합날짜 변수
    private String matchDate = GroundApplication.TODAY_YEAR+"-"+GroundApplication.TODAY_MONTH+"-"+GroundApplication.TODAY_DAY;
    private String sortMode;

    private AreaBoardAdapter areaBoardAdapter;    // 게시글 리스트 어뎁터
    private BannerViewPagerAdapter bannerViewPagerAdapter;    // 상단 배너 뷰페이저 어뎁터
    private AreaBoardPresenter areaBoardPresenter;    // AreaBoardPresenter
    private ArrayList<MatchArticleModel> matchArticleModelArrayList;    // 게시글 리스트
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private String area, boardType;
    private int areaNo;
    private int detailPosition;    //진입하고자 하는 상세 게시글의 리스트 position 값

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;
    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    /**
     * Pull to the refresh 를 할 때 초기화를 한다.
     */
    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        sortMode = SORT_ALL;
        init(area, sortMode);    //정렬 초기화
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        if(areaBoardAdapter != null)
            areaBoardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        areaBoardAdapter = null;
        areaBoardPresenter = null;
        matchArticleModelArrayList = null;
        linearLayoutManager = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boardType = intent.getExtras().getString(Constants.EXTRA_MATCH_BOARD_TYPE);
        area = intent.getExtras().getString(Constants.EXTRA_AREA_NAME);
        areaNo = intent.getIntExtra(Constants.EXTRA_AREA_NO, 0);

        sortMode = SORT_ALL;
        init(area, sortMode);    //정렬 초기화
    }

    private void init(final String area, String sortType){
        // 정렬 초기화
        sortMode = sortType;

        // 상단 배너 리스트 초기화
        ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
        // 배너 뷰페이저 어뎁터 초기화
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getApplicationContext(), bannerModelArrayList);

        // 게시글 리스트 초기화
        matchArticleModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        // 게시글 리스트 어뎁터 초기화를 한다.
        areaBoardAdapter = new AreaBoardAdapter(MatchAreaBoardActivity.this, matchArticleModelArrayList, area, bannerViewPagerAdapter, boardType, new AreaBoardAdapter.AreaBoardAdapterListener() {
            @Override
            public void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel) {
                // 임의의 게시글 탭했을 경우 상세 화면으로 진입한다.
                // 이때 ArticleModel 을 넘겨준다.
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailMatchArticleActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID, UserModel.getInstance().getUid());
                intent.putExtra(Constants.EXTRA_AREA_NAME, area);
                intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                intent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }

            // 전체 정렬
            @Override
            public void allSort(){
                resetArticleDataBySortType(SORT_ALL);
            }

            // 시합날짜 정렬
            @Override
            public void dateSort(String matchDateStr){
                matchDate = matchDateStr;
                resetArticleDataBySortType(SORT_MATCH_DATE);
            }

            // 진행중 정렬
            @Override
            public void matchStateSort(){
                resetArticleDataBySortType(SORT_NOT_MATCH_STATE);
            }

            @Override
            public void writeArticle(){
                onWriteClick();
            }

            @Override
            public void refreshList(){
                if(!isNetworkConnected()){
                    showMessage("네트워크 연결상태를 확인해주세요.");
                }else{
                    sortMode = SORT_ALL;
                    init(area, sortMode);    //정렬 초기화
                }
            }
        });

        // pull to the refresh 를 연결한다.
        swipeRefreshLayout.setOnRefreshListener(this);

        // AreaBoardPresenter 초기화
        areaBoardPresenter = new AreaBoardPresenter(getApplicationContext(), this, areaBoardAdapter, matchArticleModelArrayList, bannerModelArrayList);

        // 배너 데이터를 받아온다.
        areaBoardPresenter.loadTopBannerList();

        // 최초로 게시글 데이터들을 받아온다.
        areaBoardPresenter.loadArticleList(true, areaNo, 0, boardType, sortMode, matchDate);

        // LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!matchArticleModelArrayList.isEmpty()){
                    areaBoardPresenter.loadArticleList(false, areaNo, matchArticleModelArrayList.get(matchArticleModelArrayList.size()-1).getNo(), boardType, sortMode, matchDate);
                }
            }
        };
        // LoadMore 리스너 연결
        boardRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        initView();
    }

    /**
     * 상단 타이틀 초기화 및 게시글 리사이클러뷰를 셋팅한다.
     */
    private void initView(){
        title_tv.setText(area);
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(areaBoardAdapter);
    }

    /**
     * 게시글 목록 데이터를 다시 새로 불러온다
     * all, matchDate, matchState
     */
    private void resetArticleDataBySortType(String sortType){
        sortMode = sortType;
        endlessRecyclerOnScrollListener.reset(0, true);
        areaBoardPresenter.loadArticleList(true, areaNo, 0, boardType, sortMode, matchDate);
    }

    /**
     * 글쓰기 후 돌아왔을 때 다시 초기화 한다
     * 게시글 상세 화면 진입 후 돌아올 때 MatchArticleModel 을 다시 받아와 갱신한다.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            // 글쓰기 후 돌아왔을 경우
            if(resultCode == Activity.RESULT_OK){
                sortMode = SORT_ALL;
                init(area, sortMode);
            }
        }else if(requestCode == REQUEST_DETAIL){
            // 글 상세 화면 진입 후 돌아온 경우
            if(resultCode == Activity.RESULT_OK){
                matchArticleModelArrayList.set(detailPosition, (MatchArticleModel)data.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL));
                areaBoardAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                // 글 삭제 후 돌아온경우
                areaBoardAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    /**
     * AreaBoardPresenter 를 통해 상단 배너 데이터를 받아온뒤
     * getView() 를 통해 콜백 > notifyDataSetChanged()
     */
    @Override
    public void setBannerList(){
        bannerViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWriteClick(){
        if(isLogin()){
            //login
            Intent intent = new Intent(getApplicationContext(), WriteMatchBoardActivity.class);
            intent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, boardType);
            intent.putExtra(Constants.EXTRA_AREA_NAME, area);
            intent.putExtra(Constants.EXTRA_AREA_NO, areaNo);
            startActivityForResult(intent, REQUEST_WRITE);
        }else{
            //not login
            showMessage("로그인을 해주세요.");
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        onWriteClick();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

}
