package view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.view.FreeBoardView;

public class FreeBoardActivity extends BaseActivity implements FreeBoardView, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;

    @Override
    public void onRefresh() {
        //새로고침시 이벤트 구현
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);
        ButterKnife.bind(this);
    }

    private void init(){

    }

    @Override
    public void setFreeBoardList(){

    }

    @OnClick(R.id.write_btn) void writeBtn(){

    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
