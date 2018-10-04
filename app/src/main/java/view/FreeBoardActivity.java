package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class FreeBoardActivity extends BaseActivity implements FreeBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int REQUEST_WRITE = 1000;
    private static final int REQUEST_DETAIL = 2000;

    private FreeBoardAdapter freeBoardAdapter;
    private ArrayList<CommunityModel> communityModelArrayList;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private CommunityPresenter communityPresenter;
    private int detailPosition;    //진입하고자 하는 상세 게시글의 리스트 position 값

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        communityPresenter.loadFreeBoardData(true, 0);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);
        ButterKnife.bind(this);

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
                intent.putExtra(GroundApplication.EXTRA_USER_ID, UserModel.getInstance().getUid());
                intent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, communityModel);
                intent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, true);
                startActivityForResult(intent, REQUEST_DETAIL);
            }

            @Override
            public void writeArticle() {

            }
        });
        communityPresenter = new CommunityPresenter(this, getApplicationContext(), communityModelArrayList);
        initView();

        communityPresenter.loadFreeBoardData(true, 0);
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
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        if(isLogin()){
            Intent intent = new Intent(getApplicationContext(), WriteFreeBoardActivity.class);
            startActivityForResult(intent, REQUEST_WRITE);
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
