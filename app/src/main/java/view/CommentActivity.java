package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommentModel;
import model.UserModel;
import util.BoardManager;
import util.EndlessRecyclerOnScrollListener;
import util.SessionManager;
import util.Util;
import util.adapter.CommentAdapter;

public class CommentActivity extends AppCompatActivity {

    private static final int LOAD_DATA_COUNT = 5;
    private BoardManager boardManager;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentModel> commentModelArrayList;
    private SessionManager sessionManager;
    private int areaNo, no;
    private String boardType;

    @BindView(R.id.empty_comment_tv) TextView empty_tv;
    @BindView(R.id.comment_recyclerView) RecyclerView commentRecyclerView;
    @BindView(R.id.comment_et) EditText comment_et;
    @BindView(R.id.title_tv) TextView title_tv;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        areaNo = intent.getIntExtra("areaNo", 0);
        no = intent.getIntExtra("no", 0);
        boardType = intent.getExtras().getString("boardType");

        init();
    }

    private void init(){
        sessionManager = new SessionManager(getApplicationContext());
        boardManager = new BoardManager(getApplicationContext());
        commentModelArrayList = new ArrayList<CommentModel>();
        LinearLayoutManager lL = new LinearLayoutManager(getApplicationContext());
        commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList);
        commentRecyclerView.setLayoutManager(lL);
        commentRecyclerView.setAdapter(commentAdapter);

        commentRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(lL, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                Log.d("onLoadMore", "hi");
                try{
                    boardManager.getCommentList(no, commentModelArrayList.get(commentModelArrayList.size()-1).getNo(), boardType, empty_tv, commentRecyclerView, commentModelArrayList, commentAdapter, title_tv);
                }catch (IndexOutOfBoundsException ie){
                    boardManager.getCommentList(no, 0, boardType, empty_tv, commentRecyclerView, commentModelArrayList, commentAdapter, title_tv);
                }
            }
        });
    }

    @OnClick(R.id.write_btn) void writeComment(){
        String commentStr = comment_et.getText().toString().trim();

        if(sessionManager.isLoggedIn()){
            //login
            if(commentStr.equals("")){
                Util.showToast(getApplicationContext(), errorNotExistInputStr);
            }else{
                boardManager.writerComment(areaNo, no, UserModel.getInstance().getUid(), commentStr, comment_et, boardType,
                        empty_tv, commentRecyclerView, commentModelArrayList, commentAdapter, title_tv);
            }
        }else{
            //not login
            Util.showToast(getApplicationContext(), "로그인을 해주세요.");
        }
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
