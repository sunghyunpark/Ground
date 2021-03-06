package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommentModel;
import model.UserModel;
import presenter.CommentPresenter;
import presenter.view.CommentView;
import util.EndlessRecyclerOnScrollListener;
import util.Util;
import util.adapter.CommentAdapter;
import view.dialog.ReportDialog;

public class CommentActivity extends BaseActivity implements CommentView{

    private static final int LOAD_DATA_COUNT = 10;
    private ArrayList<CommentModel> commentModelArrayList;
    private int areaNo, articleNo;
    private String boardType, articleType;
    private CommentPresenter commentPresenter;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.empty_comment_tv) TextView empty_tv;
    @BindView(R.id.comment_recyclerView) RecyclerView commentRecyclerView;
    @BindView(R.id.comment_et) EditText comment_et;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        articleType=  intent.getExtras().getString(Constants.EXTRA_BOARD_TYPE);    // match(match / hire / recruit) / free
        if(articleType.equals(Constants.BOARD_TYPE_MATCH)){
            areaNo = intent.getIntExtra(Constants.EXTRA_AREA_NO, 0);
            boardType = intent.getExtras().getString(Constants.EXTRA_MATCH_BOARD_TYPE);
        }else if(articleType.equals(Constants.BOARD_TYPE_COMMUNITY)){
            boardType = intent.getExtras().getString(Constants.EXTRA_COMMUNITY_BOARD_TYPE);
        }
        articleNo = intent.getIntExtra(Constants.EXTRA_ARTICLE_NO, 0);

        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void init(){
        commentModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList, true, new CommentAdapter.CommentListener() {
            @Override
            public void deleteCommentEvent(int commentNo) {
                commentPresenter.deleteComment(boardType, commentNo, articleNo, areaNo);
            }
            @Override
            public void reportCommentEvent(int commentNo){
                ReportDialog reportDialog = new ReportDialog(CommentActivity.this, "comment", boardType, articleNo, commentNo);
                reportDialog.show();
            }
        });
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(linearLayoutManager);

        commentPresenter = new CommentPresenter(this, getApplicationContext(), commentModelArrayList, commentAdapter, articleType);
        commentPresenter.loadComment(true, articleNo, 0, areaNo, boardType);
    }

    /**
     * loadMoreComment 가 호출될 경우에만 commentRecyclerView 에 addOnScrollListener 를 적용한다.
     */
    @Override
    public void loadMoreComment(){
        commentRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try{
                    commentPresenter.loadCommentMore(false, articleNo, commentModelArrayList.get(commentModelArrayList.size()-1).getNo(), areaNo, boardType);
                }catch (ArrayIndexOutOfBoundsException ie){
                    commentPresenter.loadComment(true, articleNo, 0, areaNo, boardType);
                }
            }
        });
    }

    @Override
    public void writeComment(){
        String commentStr = comment_et.getText().toString().trim();
        if(isLogin()){
            if(commentStr.equals("")){
                Util.showToast(getApplicationContext(), errorNotExistInputStr);
            }else{
                comment_et.setText(null);
                commentPresenter.postComment(areaNo, articleNo, UserModel.getInstance().getUid(), commentStr, boardType);
            }
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    @Override
    public void initComment(boolean hasCommentItem){
        if(hasCommentItem){
            empty_tv.setVisibility(View.GONE);
            commentRecyclerView.setVisibility(View.VISIBLE);
        }else{
            empty_tv.setVisibility(View.VISIBLE);
            commentRecyclerView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.write_btn, R.id.back_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.write_btn:
                writeComment();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

}
