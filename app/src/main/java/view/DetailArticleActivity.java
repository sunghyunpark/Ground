package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import model.CommentModel;
import model.UserModel;
import presenter.DetailArticlePresenter;
import presenter.view.DetailArticleView;
import util.Util;
import util.adapter.CommentAdapter;

public class DetailArticleActivity extends BaseActivity implements DetailArticleView{

    private String area, boardType;
    private int articleNo, areaNo;
    private ArticleModel articleModel;
    private DetailArticlePresenter commentPresenter;
    private DetailArticlePresenter detailArticlePresenter;
    private int favoriteState = -1;    // -1 : null, 0: not like, 1:like

    @BindView(R.id.profile_iv) ImageView user_profile_iv;
    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.title_tv) TextView title_tv;
    @BindView(R.id.nick_name_tv) TextView nick_name_tv;
    @BindView(R.id.created_at_tv) TextView created_at_tv;
    @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
    @BindView(R.id.contents_tv) TextView contents_tv;
    @BindView(R.id.comment_recyclerView) RecyclerView comment_recyclerView;
    @BindView(R.id.comment_et) EditText comment_et;
    @BindView(R.id.empty_comment_tv) TextView empty_comment_tv;
    @BindView(R.id.favorite_tb) ImageView favorite_tb;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onResume(){
        super.onResume();
        if(commentPresenter != null){
            commentPresenter.loadComment(true, articleNo, 0, areaNo, boardType);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        articleNo = intent.getIntExtra("no", 0);
        areaNo = intent.getIntExtra("areaNo", 0);
        boardType = intent.getExtras().getString("boardType");

        init();
    }

    private void init(){
        area_tv.setText(area);
        articleModel = new ArticleModel();
        ArrayList<CommentModel> commentModelArrayList = new ArrayList<CommentModel>();
        LinearLayoutManager lL = new LinearLayoutManager(getApplicationContext());
        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList, false);
        comment_recyclerView.setLayoutManager(lL);
        comment_recyclerView.setAdapter(commentAdapter);
        comment_recyclerView.setNestedScrollingEnabled(false);

        detailArticlePresenter = new DetailArticlePresenter(getApplicationContext(), this, articleModel);
        detailArticlePresenter.loadDetailArticle(boardType, areaNo, articleNo);

        commentPresenter = new DetailArticlePresenter(getApplicationContext(), this, commentModelArrayList, commentAdapter);
    }

    private void setUserProfile(String urlPath){
        //Glide Options
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.user_profile_img);
        requestOptions.error(R.mipmap.user_profile_img);
        requestOptions.circleCrop();    //circle

        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(GroundApplication.GROUND_DEV_API+urlPath)
                .into(user_profile_iv);
    }

    @Override
    public void initComment(boolean hasCommentItem){
        if(hasCommentItem){
            empty_comment_tv.setVisibility(View.GONE);
            comment_recyclerView.setVisibility(View.VISIBLE);
        }else{
            empty_comment_tv.setVisibility(View.VISIBLE);
            comment_recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setArticleData(ArticleModel articleModel){
        this.articleModel = articleModel;
        title_tv.setText(articleModel.getTitle());
        nick_name_tv.setText(articleModel.getNickName());
        created_at_tv.setText(Util.parseTime(articleModel.getCreatedAt()));
        view_cnt_tv.setText("조회 "+articleModel.getViewCnt());
        contents_tv.setText(articleModel.getContents());
        favoriteState = articleModel.getFavoriteState();
        setFavorite(favoriteState);
        setUserProfile(articleModel.getProfile());
    }

    /**
     * 받아온 Article Data에서 관심 상태를 초기화한다.
     * @param state
     */
    private void setFavorite(int state){
        if(state == 0){
            //not like
            favorite_tb.setBackgroundResource(R.mipmap.favorite_img);
        }else if(state == 1){
            //like
            favorite_tb.setBackgroundResource(R.mipmap.favorite_selected_img);
        }else{
            // favoriteState is null
            favorite_tb.setBackgroundResource(R.mipmap.favorite_img);
        }
    }

    /**
     *
     * @param state 현재 상태값 > 1이면 0으로 되어야 한다.
     */
    @Override
    public void favoriteClick(int state){
        if(favoriteState == 1){
            favoriteState = 0;
            setFavorite(favoriteState);
            detailArticlePresenter.postFavoriteState(articleNo, UserModel.getInstance().getUid(), boardType, "N");
        }else if(favoriteState == 0){
            favoriteState = 1;
            setFavorite(favoriteState);
            detailArticlePresenter.postFavoriteState(articleNo, UserModel.getInstance().getUid(), boardType, "Y");
        }else{
            // favoriteState is null
            Util.showToast(getApplicationContext(), "네트워크 연결상태를 확인해주세요.");
        }
    }

    @Override
    public void commentClick(){
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra("areaNo", articleModel.getAreaNo());
        intent.putExtra("articleNo", articleModel.getNo());
        intent.putExtra("boardType", articleModel.getBoardType());
        startActivity(intent);
    }

    @Override
    public void shareClick(){
        showMessage("공유 버튼 탭");
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
                //boardManager.writerComment(areaNo, articleNo, UserModel.getInstance().getUid(), commentStr, comment_et, articleModel.getBoardType(),
                  //      empty_comment_tv, comment_recyclerView, commentModelArrayList, commentAdapter);
            }
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    @OnClick({R.id.favorite_btn}) void favoriteBtnClicked(){
        favoriteClick(favoriteState);
    }

    @OnClick(R.id.write_comment_btn) void writeCommentBtn(){
        writeComment();
    }

    @OnClick(R.id.comment_btn) void commentBtn(){
        commentClick();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
