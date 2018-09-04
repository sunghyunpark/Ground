package view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        init(area);
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
        boardType = intent.getExtras().getString("boardType");
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo", 0);

        init(area);
    }

    private void init(final String area){
        ArrayList bannerModelArrayList = new ArrayList<>();    //banner List
        BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(getApplicationContext(), bannerModelArrayList, 3);//일단 3이라 두고 서버 연동 시 bannerModelArrayList.size()로 넣어야함
        articleModelArrayList = new ArrayList<>();
        articleModelArrayList.clear();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        areaBoardAdapter = new AreaBoardAdapter(getApplicationContext(), articleModelArrayList, area, bannerViewPagerAdapter, 3, boardType, new AreaBoardAdapter.AreaBoardAdapterListener() {
            @Override
            public void goToDetailArticle(int position, String area, ArticleModel articleModel) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailArticleActivity.class);
                intent.putExtra("uid", UserModel.getInstance().getUid());
                intent.putExtra("area", area);
                intent.putExtra("articleModel", articleModel);
                intent.putExtra("hasArticleModel", true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }
            @Override
            public void allSort(){
                init(area);
            }
            @Override
            public void dateSort(){
                DatePickerDialog dialog = new DatePickerDialog(AreaBoardActivity.this, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);
                dialog.show();
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

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String strBeforeFormat = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            String strAfterFormat = "";
            try {
                Date originDate = originFormat.parse(strBeforeFormat);

                strAfterFormat = newFormat.format(originDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            showMessage(strAfterFormat);
        }
    };

    @Override
    public void loadMoreArticle(LinearLayoutManager linearLayoutManager){
        boardRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try{
                    areaBoardPresenter.loadArticleList(false, areaNo, articleModelArrayList.get(articleModelArrayList.size()-1).getNo(), boardType);
                }catch (IndexOutOfBoundsException ie){
                    areaBoardPresenter.loadArticleList(true, areaNo, 0, boardType);
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
                init(area);
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

    @OnClick(R.id.refresh_btn) void refreshBtn(){
        if(!isNetworkConnected()){
            showMessage("네트워크 연결상태를 확인해주세요.");
        }else{
            init(area);
        }
    }
}
