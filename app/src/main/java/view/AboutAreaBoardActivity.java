package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import util.BoardManager;
import util.EndlessRecyclerOnScrollListener;
import util.SessionManager;
import util.Util;
import util.adapter.AboutAreaBoardAdapter;

/**
 * 임의의 지역 > 게시판 리스트
 */
public class AboutAreaBoardActivity extends AppCompatActivity {

    private static final int LOAD_DATA_COUNT = 10;
    private SessionManager sessionManager;
    private BoardManager boardManager;
    private AboutAreaBoardAdapter aboutAreaBoardAdapter;
    private ArrayList<ArticleModel> articleModelArrayList;
    private String area;
    private int areaNo;

    @BindView(R.id.board_recyclerView) RecyclerView boardRecyclerView;
    @BindView(R.id.about_area_board_title_tv) TextView title_tv;

    @Override
    protected void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        if(aboutAreaBoardAdapter != null)
            aboutAreaBoardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_area_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo", 0);

        init(area, areaNo);
    }

    private void init(String area, final int areaNo){
        sessionManager = new SessionManager(getApplicationContext());
        boardManager = new BoardManager(getApplicationContext());
        articleModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        aboutAreaBoardAdapter = new AboutAreaBoardAdapter(getApplicationContext(), articleModelArrayList, area);
        boardRecyclerView.setLayoutManager(linearLayoutManager);
        boardRecyclerView.setAdapter(aboutAreaBoardAdapter);

        boardRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try{
                    boardManager.getMatchingBoard(areaNo, articleModelArrayList.get(articleModelArrayList.size()-1).getNo(), aboutAreaBoardAdapter, articleModelArrayList);
                }catch (IndexOutOfBoundsException ie){
                    boardManager.getMatchingBoard(areaNo, 0, aboutAreaBoardAdapter, articleModelArrayList);
                }
            }
        });

        title_tv.setText(area);

    }

    @OnClick(R.id.write_btn) void goWrite(){
        if(sessionManager.isLoggedIn()){
            //login
            Intent intent = new Intent(getApplicationContext(), WriteBoardActivity.class);
            intent.putExtra("area", area);
            intent.putExtra("areaNo", areaNo);
            startActivity(intent);
        }else{
            //not login
            Util.showToast(getApplicationContext(), "로그인을 해주세요.");
        }
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}