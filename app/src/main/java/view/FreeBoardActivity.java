package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.FreeArticleModel;
import presenter.FreeBoardPresenter;
import presenter.view.FreeBoardView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.FreeBoardAdapter;

public class FreeBoardActivity extends BaseActivity implements FreeBoardView, SwipeRefreshLayout.OnRefreshListener{

    private static final int REQUEST_WRITE = 1000;

    private FreeBoardAdapter freeBoardAdapter;
    private ArrayList<FreeArticleModel> freeArticleModelArrayList;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private FreeBoardPresenter freeBoardPresenter;

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        freeBoardPresenter.loadFreeBoardData(true, 0);
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
        freeArticleModelArrayList = new ArrayList<FreeArticleModel>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        freeBoardAdapter = new FreeBoardAdapter(getApplicationContext(), freeArticleModelArrayList);
        freeBoardPresenter = new FreeBoardPresenter(this, getApplicationContext(), freeArticleModelArrayList);
        initView();

        freeBoardPresenter.loadFreeBoardData(true, 0);
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
        Intent intent = new Intent(getApplicationContext(), WriteFreeBoardActivity.class);
        startActivityForResult(intent, REQUEST_WRITE);
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
