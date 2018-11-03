package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommunityModel;
import model.UserModel;
import presenter.CommunityPresenter;
import presenter.view.FreeBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.FreeBoardAdapter;

public class CommunityBoardActivity extends BaseActivity implements FreeBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int LOAD_DATA_COUNT = 10;
    private static final int REQUEST_WRITE = 1000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int RESULT_DELETE = 3000;

    private FreeBoardAdapter freeBoardAdapter;
    private ArrayList<CommunityModel> communityModelArrayList;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private CommunityPresenter communityPresenter;
    private int detailPosition;    //진입하고자 하는 상세 게시글의 리스트 position 값

    private String typeOfCommunity;

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;

    @Override
    protected void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        if(freeBoardAdapter != null)
            freeBoardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        endlessRecyclerOnScrollListener.reset(0, true);
        communityPresenter.loadFreeBoardData(true, 0);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        typeOfCommunity = intent.getExtras().getString(Constants.EXTRA_COMMUNITY_BOARD_TYPE);

        init();
    }

    private void init(){
        swipeRefreshLayout.setOnRefreshListener(this);
        communityModelArrayList = new ArrayList<CommunityModel>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        freeBoardAdapter = new FreeBoardAdapter(getApplicationContext(), communityModelArrayList, new FreeBoardAdapter.FreeBoardAdapterListener() {
            @Override
            public void goToDetailArticle(int position, CommunityModel communityModel) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailCommunityActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID, UserModel.getInstance().getUid());
                intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, communityModel);
                intent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }

            @Override
            public void writeArticle() {

            }
        });
        communityPresenter = new CommunityPresenter(this, getApplicationContext(), communityModelArrayList, typeOfCommunity);
        initView();

        communityPresenter.loadFreeBoardData(true, 0);

        //LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!communityModelArrayList.isEmpty()){
                    communityPresenter.loadFreeBoardData(false, communityModelArrayList.get(communityModelArrayList.size()-1).getNo());
                }
            }
        };
        boardRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void initView(){
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(freeBoardAdapter);
    }

    @Override
    public void setFreeBoardList(){
        freeBoardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            if(resultCode == Activity.RESULT_OK){
                init();
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }else if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                communityModelArrayList.set(detailPosition, (CommunityModel)data.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL));
                freeBoardAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                freeBoardAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        if(isLogin()){
            Intent intent = new Intent(getApplicationContext(), WriteCommunityBoardActivity.class);
            intent.putExtra(Constants.EXTRA_COMMUNITY_BOARD_TYPE, Constants.FREE_OF_BOARD_TYPE_COMMUNITY);
            startActivityForResult(intent, REQUEST_WRITE);
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    @OnClick(R.id.up_to_scroll_btn) void upToScrollBtn(){
        LinearLayoutManager llm = (LinearLayoutManager) boardRecyclerView.getLayoutManager();
        llm.scrollToPosition(0);
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
