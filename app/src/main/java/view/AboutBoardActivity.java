package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import model.CommentModel;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.BoardManager;
import util.SessionManager;
import util.Util;
import util.adapter.CommentAdapter;

public class AboutBoardActivity extends AppCompatActivity {

    private String area;
    private int no;
    private int areaNo;
    private String commentCnt;
    private ArticleModel articleModel;
    private BoardManager boardManager;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentModel> commentModelArrayList;
    private SessionManager sessionManager;

    @BindView(R.id.profile_iv) ImageView user_profile_iv;
    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.title_tv) TextView title_tv;
    @BindView(R.id.nick_name_tv) TextView nick_name_tv;
    @BindView(R.id.created_at_tv) TextView created_at_tv;
    @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
    @BindView(R.id.contents_tv) TextView contents_tv;
    @BindView(R.id.comment_recyclerView) RecyclerView comment_recyclerView;
    @BindView(R.id.comment_et) EditText comment_et;
    @BindView(R.id.comment_cnt_tv) TextView comment_cnt_tv;
    @BindView(R.id.comment_title_layout) ViewGroup comment_title_layout;
    @BindView(R.id.empty_comment_tv) TextView empty_comment_tv;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        no = intent.getIntExtra("no", 0);
        areaNo = intent.getIntExtra("areaNo", 0);
        commentCnt = intent.getExtras().getString("commentCnt");

    }

    @Override
    protected void onResume(){
        super.onResume();
        init();
    }

    private void init(){
        sessionManager = new SessionManager(getApplicationContext());
        articleModel = new ArticleModel();
        commentModelArrayList = new ArrayList<CommentModel>();
        LinearLayoutManager lL = new LinearLayoutManager(getApplicationContext());
        commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList);
        boardManager = new BoardManager(getApplicationContext());
        comment_recyclerView.setLayoutManager(lL);
        comment_recyclerView.setAdapter(commentAdapter);
        comment_recyclerView.setNestedScrollingEnabled(false);

        setCommentTitle(commentCnt);

        getAboutBoard(areaNo, no);
    }

    private void setCommentTitle(String commentCnt){
        if(commentCnt.equals("0")){
            comment_title_layout.setVisibility(View.GONE);
        }else{
            comment_cnt_tv.setText("댓글 "+commentCnt);
        }

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

    /**
     * Article 내용 받아오기
     * @param areaNo
     * @param no
     */
    private void getAboutBoard(int areaNo, int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = apiService.getAboutBoard(areaNo, no);
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    articleModel = aboutAreaBoardListResponse.getResult().get(0);
                    area_tv.setText(area);
                    title_tv.setText(articleModel.getTitle());
                    nick_name_tv.setText(articleModel.getNickName());
                    created_at_tv.setText(articleModel.getCreatedAt());
                    view_cnt_tv.setText("조회 "+articleModel.getViewCnt());
                    contents_tv.setText(articleModel.getContents());
                    setUserProfile("");

                    boardManager.getCommentList(false, articleModel.getNo(), 0, articleModel.getBoardType(), empty_comment_tv, comment_recyclerView,
                            commentModelArrayList, commentAdapter);
                }else{
                    Util.showToast(getApplicationContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getApplicationContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    @OnClick(R.id.write_comment_btn) void writeComment(){
        String commentStr = comment_et.getText().toString().trim();

        if(sessionManager.isLoggedIn()){
            //login
            if(commentStr.equals("")){
                Util.showToast(getApplicationContext(), errorNotExistInputStr);
            }else{
                boardManager.writerComment(areaNo, no, UserModel.getInstance().getUid(), commentStr, comment_et, articleModel.getBoardType(),
                        empty_comment_tv, comment_recyclerView, commentModelArrayList, commentAdapter);
            }
        }else{
            //not login
            Util.showToast(getApplicationContext(), "로그인을 해주세요.");
        }
    }

    @OnClick({R.id.more_comment_btn, R.id.comment_btn}) void goCommentPage(){
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra("areaNo", articleModel.getAreaNo());
        intent.putExtra("articleNo", articleModel.getNo());
        intent.putExtra("boardType", articleModel.getBoardType());
        startActivity(intent);
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
