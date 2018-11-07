package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchArticleModel;
import model.UserModel;
import presenter.TodayBoardPresenter;
import presenter.view.TodayBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.TodayMatchAdapter;

public class TodayBoardActivity extends BaseActivity implements TodayBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int RESULT_DELETE = 3000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int DATA_LOAD_COUNT = 20;

    private TodayMatchAdapter todayMatchAdapter;    // 오늘의 시합 어뎁터
    private TodayBoardPresenter todayBoardPresenter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private ArrayList<MatchArticleModel> todayMatchArticleModelArrayList;

    private int detailPosition;

    @BindView(R.id.today_match_recyclerView) RecyclerView todayMatchRecyclerView;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        endlessRecyclerOnScrollListener.reset(0, true);
        todayBoardPresenter.loadTodayMatchList(true, 0, DATA_LOAD_COUNT);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_board);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        // 오늘의 시합 데이터를 담을 리스트를 초기화 한 뒤 어뎁터에 넘겨준다.
        todayMatchArticleModelArrayList = new ArrayList<MatchArticleModel>();
        todayMatchAdapter = new TodayMatchAdapter(getApplicationContext(), todayMatchArticleModelArrayList, true, new TodayMatchAdapter.todayMatchListener() {
            @Override
            public void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailMatchArticleActivity.class);
                intent.putExtra(Constants.EXTRA_AREA_NAME, area);
                intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                intent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, true);
                intent.putExtra(Constants.EXTRA_USER_ID, UserModel.getInstance().getUid());
                startActivityForResult(intent, REQUEST_DETAIL);
            }
        });

        // recyclerview 초기화.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        todayMatchRecyclerView.setLayoutManager(linearLayoutManager);
        todayMatchRecyclerView.setAdapter(todayMatchAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        // HomePresenter 를 초기화 한다.
        todayBoardPresenter = new TodayBoardPresenter(this, getApplicationContext(), todayMatchArticleModelArrayList);

        // 데이터를 최초로 불러온다.
        todayBoardPresenter.loadTodayMatchList(true, 0, DATA_LOAD_COUNT);

        //LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, DATA_LOAD_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!todayMatchArticleModelArrayList.isEmpty()){
                    todayBoardPresenter.loadTodayMatchList(false, todayMatchArticleModelArrayList.get(todayMatchArticleModelArrayList.size()-1).getNo(), DATA_LOAD_COUNT);
                }
            }
        };
        todayMatchRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                todayMatchArticleModelArrayList.set(detailPosition, (MatchArticleModel)data.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL));
                todayMatchAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                todayMatchAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    @Override
    public void notifyTodayMatchArticle(){
        todayMatchAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

    @OnClick(R.id.refresh_btn) void refreshBtn(){
        endlessRecyclerOnScrollListener.reset(0, true);
        todayBoardPresenter.loadTodayMatchList(true, 0, DATA_LOAD_COUNT);
    }

    @OnClick(R.id.up_to_scroll_btn) void upToScrollBtn(){
        LinearLayoutManager llm = (LinearLayoutManager) todayMatchRecyclerView.getLayoutManager();
        llm.scrollToPosition(0);
    }
}
