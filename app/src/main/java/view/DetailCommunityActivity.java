package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommentModel;
import model.CommunityModel;
import model.UserModel;
import presenter.DetailCommunityPresenter;
import presenter.view.DetailCommunityView;
import util.Util;
import util.adapter.CommentAdapter;

public class DetailCommunityActivity extends BaseActivity implements DetailCommunityView{

    private boolean hasArticleModel;
    private String uid;
    private int communityNo;
    private CommunityModel communityModel;

    private DetailCommunityPresenter detailCommunityPresenter;

    private int favoriteState = -1;    // -1 : null, 0: not like, 1:like

    @BindView(R.id.rootView) ViewGroup root_view;
    @BindView(R.id.board_type_tv) TextView board_type_tv;
    @BindView(R.id.photo_iv) ImageView photo_iv;
    @BindView(R.id.profile_iv) ImageView user_profile_iv;
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
        initUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        hasArticleModel = intent.getExtras().getBoolean(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL);
        uid = intent.getExtras().getString(GroundApplication.EXTRA_USER_ID);
        UserModel.getInstance().setUid(uid);    //푸시를 통해 바로 액티비티 진입 시 uid값을 새로 받아오지만 moreBtn과 같이 UserModel을 이용하는 부분도 있어서 다시 넣어준다.
        communityModel = new CommunityModel();

        if(hasArticleModel){
            // 데이터가 있는 경우
            communityModel = (CommunityModel)intent.getExtras().getSerializable(GroundApplication.EXTRA_ARTICLE_MODEL);
            communityModel.setViewCnt(communityModel.getViewCnt()+1);
            //showMessage("area : "+area+"\nhasArticleModel : "+hasArticleModel+"\nboardType : "+articleModel.getBoardType()+"\nareaNo : "+articleModel.getAreaNo()+"\narticleNo : "+articleModel.getNo());
        }else{
            // 데이터가 없어서 새로 불러와야 하는 경우
            communityNo = intent.getIntExtra(GroundApplication.EXTRA_ARTICLE_NO, 0);
            //showMessage("area : "+area+"\nhasArticleModel : "+hasArticleModel+"\nboardType : "+boardType+"\nareaNo : "+areaNo+"\narticleNo : "+articleNo);
        }

        init();
    }

    private void init(){
        ArrayList<CommentModel> commentModelArrayList = new ArrayList<CommentModel>();
        LinearLayoutManager lL = new LinearLayoutManager(getApplicationContext());
        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList, false, new CommentAdapter.CommentListener() {
            @Override
            public void deleteCommentEvent(int commentNo) {
                //detailArticlePresenter.deleteComment(articleModel.getBoardType(), commentNo, articleModel.getNo(), articleModel.getAreaNo());
            }
            @Override
            public void reportCommentEvent(int commentNo){
                //ReportDialog reportDialog = new ReportDialog(DetailCommunityActivity.this, "comment", articleModel.getBoardType(), articleModel.getNo(), commentNo);
                //reportDialog.show();
            }
        });
        comment_recyclerView.setLayoutManager(lL);
        comment_recyclerView.setAdapter(commentAdapter);
        comment_recyclerView.setNestedScrollingEnabled(false);

        detailCommunityPresenter = new DetailCommunityPresenter(this, getApplicationContext(), commentModelArrayList, commentAdapter);
    }

    private void initUI(){
        board_type_tv.setText("자유게시판");
        if(hasArticleModel){
            setArticleData(communityModel);
        }else{
            detailCommunityPresenter.loadArticleData(communityNo);
        }
    }

    /**
     * 사용자 프로필 이미지를 초기화해준다.
     * 현재는 별도의 프로필 스펙을 적용하지 않은 상태라 디폴트 이미지만 보여준다.
     * @param urlPath
     */
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
    public void favoriteClick(){
        if(favoriteState == 1){
            favoriteState = 0;
            setFavorite(favoriteState);
            detailCommunityPresenter.postFavoriteState(communityModel.getNo(), uid,  "N");
        }else if(favoriteState == 0){
            favoriteState = 1;
            setFavorite(favoriteState);
            detailCommunityPresenter.postFavoriteState(communityModel.getNo(), uid, "Y");
        }else{
            // favoriteState is null
            Util.showToast(getApplicationContext(), "네트워크 연결상태를 확인해주세요.");
        }
    }

    @Override
    public void commentClick(){

    }

    @Override
    public void captureClick(){

    }

    @Override
    public void writeComment(){

    }

    @Override
    public void setArticleData(final CommunityModel communityModel){
        title_tv.setText(communityModel.getTitle());
        nick_name_tv.setText(communityModel.getNickName());
        created_at_tv.setText(Util.parseTimeWithoutSec(communityModel.getCreatedAt()));
        view_cnt_tv.setText("조회 "+communityModel.getViewCnt());
        contents_tv.setText(communityModel.getContents());
        setUserProfile(communityModel.getProfile());

        if(!communityModel.getPhotoUrl().equals("")){
            photo_iv.setVisibility(View.VISIBLE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();

            Glide.with(getApplicationContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(GroundApplication.GROUND_DEV_API+communityModel.getPhotoUrl())
                    .into(photo_iv);
        }else{
            photo_iv.setVisibility(View.GONE);
        }

        photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageViewerActivity.class);
                intent.putExtra(GroundApplication.EXTRA_BOARD_PHOTO_URL, communityModel.getPhotoUrl());
                startActivity(intent);
            }
        });

        if(detailCommunityPresenter != null){
            detailCommunityPresenter.loadFavoriteState(communityModel.getNo(), uid);
            //detailCommunityPresenter.loadComment(true, communityModel.getNo(), 0, communityModel.getAreaNo(), communityModel.getBoardType());
        }
    }

    @Override
    public void loadArticleData(CommunityModel communityModel){

    }

    @Override
    public void setFavoriteState(int state){
        favoriteState = state;
        setFavorite(favoriteState);
    }

    /**
     * commentPresenter 를 통해 받아온 댓글 리스트 중 commentList 의 size 를 통해
     * 댓글이 있는지 없는지 판별을 한 뒤에 initComment 를 통해 Gone 처리 여부 수행한다.
     * @param hasComment
     */
    @Override
    public void initComment(boolean hasComment){
        if(hasComment){
            empty_comment_tv.setVisibility(View.GONE);
            comment_recyclerView.setVisibility(View.VISIBLE);
        }else{
            empty_comment_tv.setVisibility(View.VISIBLE);
            comment_recyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 받아온 Article Data에서 관심 상태를 초기화한다.
     * @param state
     */
    private void setFavorite(int state){
        if(state == 0){
            //not like
            favorite_tb.setImageResource(R.mipmap.favorite_img);
        }else if(state == 1){
            //like
            favorite_tb.setImageResource(R.mipmap.favorite_selected_img);
        }else{
            // favoriteState is null
            favorite_tb.setImageResource(R.mipmap.favorite_img);
        }
    }

    @OnClick({R.id.favorite_btn}) void favoriteBtnClicked(){
        favoriteClick();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, communityModel);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * 하드웨어 back 버튼으로 뒤로가기 시 게시글 리스트 화면에 articleModel 과 함께 result 값을 돌려준다.
     */
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, communityModel);
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }

}