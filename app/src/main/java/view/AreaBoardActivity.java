package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import model.UserModel;
import presenter.AreaBoardPresenter;
import presenter.view.AreaBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.AreaBoardAdapter;
import util.adapter.BannerViewPagerAdapter;

/**
 * 임의의 지역 > 게시판 리스트
 */
public class AreaBoardActivity extends BaseActivity implements AreaBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int RESULT_DELETE = 3000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int LOAD_DATA_COUNT = 10;
    private static final int REQUEST_WRITE = 1000;

    private static final String SORT_ALL = "all";
    private static final String SORT_MATCH_DATE = "matchDate";
    private static final String SORT_NOT_MATCH_STATE = "matchState";
    private String matchDate = GroundApplication.TODAY_YEAR+"-"+GroundApplication.TODAY_MONTH+"-"+GroundApplication.TODAY_DAY;
    private String sortMode;

    private AreaBoardAdapter areaBoardAdapter;
    private AreaBoardPresenter areaBoardPresenter;
    private ArrayList<ArticleModel> articleModelArrayList;
    private LinearLayoutManager linearLayoutManager;
    private String area, boardType;
    private int areaNo;
    private int detailPosition;    //진입하고자 하는 상세 게시글의 리스트 position 값

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;
    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        sortMode = SORT_ALL;
        init(area, sortMode);
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
        /* 메모리 관련 이슈때문에 잠시 주석처리
        if(areaBoardAdapter != null)
            areaBoardAdapter.stopBannerThread();
        */
        areaBoardAdapter = null;
        areaBoardPresenter = null;
        articleModelArrayList = null;
        linearLayoutManager = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boardType = intent.getExtras().getString(GroundApplication.EXTRA_BOARD_TYPE);
        area = intent.getExtras().getString(GroundApplication.EXTRA_AREA_NAME);
        areaNo = intent.getIntExtra(GroundApplication.EXTRA_AREA_NO, 0);

        sortMode = SORT_ALL;
        init(area, sortMode);    //정렬 초기화
    }

    private void init(final String area, String sortType){
        sortMode = sortType;    //정렬 초기화
        ArrayList bannerModelArrayList = new ArrayList<>();    //banner List
        BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(getApplicationContext(), bannerModelArrayList, 3);//일단 3이라 두고 서버 연동 시 bannerModelArrayList.size()로 넣어야함
        articleModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        areaBoardAdapter = new AreaBoardAdapter(AreaBoardActivity.this, articleModelArrayList, area, bannerViewPagerAdapter, 3, boardType, new AreaBoardAdapter.AreaBoardAdapterListener() {
            @Override
            public void goToDetailArticle(int position, String area, ArticleModel articleModel) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailArticleActivity.class);
                intent.putExtra(GroundApplication.EXTRA_USER_ID, UserModel.getInstance().getUid());
                intent.putExtra(GroundApplication.EXTRA_AREA_NAME, area);
                intent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, articleModel);
                intent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }
            @Override
            public void allSort(){
                sortMode = SORT_ALL;
                init(area, sortMode);

            }
            @Override
            public void dateSort(String matchDateStr){
                sortMode = SORT_MATCH_DATE;
                matchDate = matchDateStr;
                init(area, sortMode);
            }
            @Override
            public void writeArticle(){
                onWriteClick();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        areaBoardPresenter = new AreaBoardPresenter(getApplicationContext(), this, areaBoardAdapter, articleModelArrayList);

        loadMoreArticle(linearLayoutManager);

        initView();
    }

    private void initView(){
        title_tv.setText(area);
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(areaBoardAdapter);
    }

    @Override
    public void loadMoreArticle(LinearLayoutManager linearLayoutManager){
        boardRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                showMessage("loadMore");
                // do something...
                try{
                    areaBoardPresenter.loadArticleList(false, areaNo, articleModelArrayList.get(articleModelArrayList.size()-1).getNo(), boardType, sortMode, matchDate);
                }catch (IndexOutOfBoundsException ie){
                    areaBoardPresenter.loadArticleList(true, areaNo, 0, boardType, sortMode, matchDate);
                }
            }
        });

    }

    /**
     * 글쓰기 후 돌아왔을 때 다시 초기화 한다
     * 게시글 상세 화면 진입 후 돌아올 때 ArticleModel 을 다시 받아와 갱신한다.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            if(resultCode == Activity.RESULT_OK){
                init(area, sortMode);
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }else if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                articleModelArrayList.set(detailPosition, (ArticleModel)data.getExtras().getSerializable("articleModel"));
                areaBoardAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                areaBoardAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    @Override
    public void onWriteClick(){
        if(isLogin()){
            //login
            Intent intent = new Intent(getApplicationContext(), WriteBoardActivity.class);
            intent.putExtra(GroundApplication.EXTRA_BOARD_TYPE, boardType);
            intent.putExtra(GroundApplication.EXTRA_AREA_NAME, area);
            intent.putExtra(GroundApplication.EXTRA_AREA_NO, areaNo);
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

    @OnClick(R.id.refresh_btn) void refreshBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            sortMode = SORT_ALL;
            init(area, sortMode);
        }
    }
}
