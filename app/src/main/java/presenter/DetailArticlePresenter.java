package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import base.presenter.BasePresenter;
import model.ArticleModel;
import model.CommentModel;
import presenter.view.DetailArticleView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.CommentAdapter;

public class DetailArticlePresenter extends BasePresenter<DetailArticleView>{

    private Context context;
    //Article Data
    private ArticleModel articleModel;

    //Comment Data
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;

    /**
     * Comment
     * @param context
     * @param view
     * @param commentModelArrayList
     * @param commentAdapter
     */
    public DetailArticlePresenter(Context context, DetailArticleView view, ArrayList<CommentModel> commentModelArrayList, CommentAdapter commentAdapter){
        super(view);
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
        this.commentAdapter = commentAdapter;
    }

    /**
     * Article
     * @param context
     * @param view
     * @param articleModel
     */
    public DetailArticlePresenter(Context context, DetailArticleView view, ArticleModel articleModel){
        super(view);
        this.context = context;
        this.articleModel = articleModel;
    }

    /**
     * 게시글 관련 데이터를 받아온다.
     * getView()를 통해 DetailArticleActivity 로 articleModel 을 넘겨주며 초기화한다.
     * @param areaNo
     * @param articleNo
     */
    public void loadDetailArticle(String boardType, int areaNo, int articleNo, String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getDetailBoard(boardType, areaNo, articleNo, uid);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    articleModel = articleModelListResponse.getResult().get(0);
                    getView().setArticleData(articleModel);
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<ArticleModelListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 디테일 뷰 진입 시 하단 댓글 데이터를 불러온다.
     * @param refresh
     * @param articleNo
     * @param commentNo
     * @param areaNo
     */
    public void loadComment(boolean refresh, int articleNo, final int commentNo, int areaNo, String boardType){
        if(refresh)
            commentModelArrayList.clear();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommentListResponse> call = apiService.getCommentList(boardType, articleNo, areaNo, commentNo);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    if(size > 0){
                        getView().initComment(true);
                        for(CommentModel cm : commentListResponse.getResult()){
                            Collections.addAll(commentModelArrayList, cm);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }else{
                        getView().initComment(false);
                    }
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 디테일뷰 하단 댓글 입력 후 서버로 전송
     * @param areaNo
     * @param articleNo
     * @param writerId
     * @param comment
     * @param boardType
     */
    public void postComment(final int areaNo, final int articleNo, String writerId, String comment, final String boardType){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.writeComment(areaNo, articleNo, writerId, comment, boardType);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 작성하였습니다.");
                    loadComment(true, articleNo, 0, areaNo, boardType);
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 해당 디테일뷰 좋아요
     * @param articleNo
     * @param uid
     * @param boardType
     * @param state
     */
    public void postFavoriteState(int articleNo, String uid, String boardType, final String state){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.postFavoriteState(state, articleNo, uid, boardType);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    if(state.equals("Y")){
                        Util.showToast(context,"좋아요를 눌렀습니다.");
                    }else{
                        Util.showToast(context, "좋아요를 취소했습니다.");
                    }
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
