package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import model.BannerModel;
import presenter.AreaBoardPresenter;
import presenter.view.AreaBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.AreaBoardAdapter;
import util.adapter.BannerViewPagerAdapter;

/**
 * 임의의 지역 > 게시판 리스트
 */
public class AreaBoardActivity extends BaseActivity implements AreaBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int LOAD_DATA_COUNT = 10;
    private static final int REQUEST_WRITE = 1000;
    private AreaBoardAdapter areaBoardAdapter;
    private AreaBoardPresenter areaBoardPresenter;
    private ArrayList<ArticleModel> articleModelArrayList;
    private String area, boardType;
    private int areaNo;

    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private ArrayList<BannerModel> bannerModelArrayList;

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;
    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        init(area, areaNo);
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
        if(areaBoardAdapter != null)
            areaBoardAdapter.stopBannerThread();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boardType = intent.getExtras().getString("boardType");
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo", 0);

        init(area, areaNo);
    }

    private void init(String area, final int areaNo){
        bannerModelArrayList = new ArrayList<>();    //banner List
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getApplicationContext(), bannerModelArrayList, 3);//일단 3이라 두고 서버 연동 시 bannerModelArrayList.size()로 넣어야함
        articleModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        areaBoardAdapter = new AreaBoardAdapter(getApplicationContext(), articleModelArrayList, area, bannerViewPagerAdapter, 3);
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(areaBoardAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        areaBoardPresenter = new AreaBoardPresenter(getApplicationContext(), this, areaBoardAdapter, articleModelArrayList);

        boardRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try{
                    areaBoardPresenter.loadArticleList(areaNo, articleModelArrayList.get(articleModelArrayList.size()-1).getNo(), boardType);
                }catch (IndexOutOfBoundsException ie){
                    areaBoardPresenter.loadArticleList(areaNo, 0, boardType);
                }
            }
        });

        title_tv.setText(area);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_WRITE) {
            if(resultCode == Activity.RESULT_OK){
                init(area, areaNo);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }

    @Override
    public void onWriteClick(){
        if(isLogin()){
            //login
            Intent intent = new Intent(getApplicationContext(), WriteBoardActivity.class);
            intent.putExtra("boardType", boardType);
            intent.putExtra("area", area);
            intent.putExtra("areaNo", areaNo);
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
