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
import model.MatchArticleModel;
import model.UserModel;
import presenter.AreaBoardPresenter;
import presenter.view.AreaBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.AreaSearchResultAdapter;

/**
 * 임의의 지역 > 게시판 리스트
 */
public class AreaSearchResultActivity extends BaseActivity implements AreaBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int RESULT_DELETE = 3000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int LOAD_DATA_COUNT = 10;

    private static final String SORT_ALL = "all";
    private static final String SORT_MATCH_DATE = "matchDate";
    private static final String SORT_NOT_MATCH_STATE = "matchState";
    private String matchDate = GroundApplication.TODAY_YEAR+"-"+GroundApplication.TODAY_MONTH+"-"+GroundApplication.TODAY_DAY;
    private String sortMode;

    private AreaSearchResultAdapter areaSearchResultAdapter;
    private AreaBoardPresenter areaBoardPresenter;
    private ArrayList<MatchArticleModel> matchArticleModelArrayList;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private String area, boardType;
    private String areaArrayStr;
    private int detailPosition;    //진입하고자 하는 상세 게시글의 리스트 position 값

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;
    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        resetArticleData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        if(areaSearchResultAdapter != null)
            areaSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        areaSearchResultAdapter = null;
        areaBoardPresenter = null;
        matchArticleModelArrayList = null;
        linearLayoutManager = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_search_result);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString(GroundApplication.EXTRA_AREA_NAME);
        areaArrayStr = intent.getExtras().getString(GroundApplication.EXTRA_AREA_NO);

        sortMode = SORT_ALL;
        init(area, sortMode);    //정렬 초기화
    }

    private void init(final String area, String sortType){
        sortMode = sortType;    //정렬 초기화

        matchArticleModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        areaSearchResultAdapter = new AreaSearchResultAdapter(AreaSearchResultActivity.this, matchArticleModelArrayList, new AreaSearchResultAdapter.AreaSearchResultAdapterListener() {
            @Override
            public void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailMatchArticleActivity.class);
                intent.putExtra(GroundApplication.EXTRA_USER_ID, UserModel.getInstance().getUid());
                intent.putExtra(GroundApplication.EXTRA_AREA_NAME, area);
                intent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, matchArticleModel);
                intent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }
            //전체 정렬
            @Override
            public void allSort(){
                resetArticleData();
            }
            //시합날짜 정렬
            @Override
            public void dateSort(String matchDateStr){
                sortMode = SORT_MATCH_DATE;
                matchDate = matchDateStr;
                endlessRecyclerOnScrollListener.reset(0, true);
                areaBoardPresenter.loadSearchResultList(true, areaArrayStr, 0, sortMode, matchDate);
            }
            //진행중 정렬
            @Override
            public void matchStateSort(){
                sortMode = SORT_NOT_MATCH_STATE;
                endlessRecyclerOnScrollListener.reset(0, true);
                areaBoardPresenter.loadSearchResultList(true, areaArrayStr, 0, sortMode, matchDate);
            }
            @Override
            public void writeArticle(){
                onWriteClick();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        areaBoardPresenter = new AreaBoardPresenter(getApplicationContext(), this, areaSearchResultAdapter, matchArticleModelArrayList, null);

        //최초로 게시글 데이터들을 받아온다.
        areaBoardPresenter.loadSearchResultList(true, areaArrayStr, 0, sortMode, matchDate);
        //LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!matchArticleModelArrayList.isEmpty()){
                    areaBoardPresenter.loadSearchResultList(false, areaArrayStr, matchArticleModelArrayList.get(matchArticleModelArrayList.size()-1).getNo(), sortMode, matchDate);
                }
            }
        };
        boardRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        initView();
    }

    private void initView(){
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(areaSearchResultAdapter);
    }

    /**
     * 게시글 목록 데이터를 다시 새로 불러온다(sort > all)
     */
    private void resetArticleData(){
        sortMode = SORT_ALL;
        endlessRecyclerOnScrollListener.reset(0, true);
        areaBoardPresenter.loadSearchResultList(true, areaArrayStr, 0, sortMode, matchDate);
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
        if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                matchArticleModelArrayList.set(detailPosition, (MatchArticleModel)data.getExtras().getSerializable(GroundApplication.EXTRA_ARTICLE_MODEL));
                areaSearchResultAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                areaSearchResultAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    @Override
    public void setBannerList(){

    }

    @Override
    public void onWriteClick(){

    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

    @OnClick(R.id.refresh_btn) void refreshBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            resetArticleData();
        }
    }
}
