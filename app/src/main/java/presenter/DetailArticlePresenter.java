package presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleEtcResponse;
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
    //Comment Data
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;
    private ApiInterface apiService;

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
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void loadArticleData(String boardType, int areaNo, int articleNo, String uid){
        Call<ArticleModelListResponse> call = apiService.getArticleData(boardType, areaNo, articleNo, uid);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    getView().loadArticleData(articleModelListResponse.getResult().get(0));
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

    public void loadFavoriteState(final String boardType, final int areaNo, final int articleNo, final String uid){
        Call<ArticleEtcResponse> call = apiService.getArticleEtcData(boardType, areaNo, articleNo, uid);
        call.enqueue(new Callback<ArticleEtcResponse>() {
            @Override
            public void onResponse(Call<ArticleEtcResponse> call, Response<ArticleEtcResponse> response) {
                ArticleEtcResponse articleEtcResponse = response.body();
                if(articleEtcResponse.getCode() == 200){
                    getView().setFavoriteState(articleEtcResponse.getFavoriteState());
                }
            }

            @Override
            public void onFailure(Call<ArticleEtcResponse> call, Throwable t) {
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
     * 댓글 삭제
     * @param boardType
     * @param commentNo
     * @param articleNo
     * @param areaNo
     */
    public void deleteComment(String boardType, int commentNo, int articleNo, int areaNo){
        Call<CommonResponse> call = apiService.deleteComment(boardType, commentNo, articleNo, areaNo);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 삭제하였습니다.");
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

    public void changeMatchState(int areaNo, int articleNo, final String state){
        Call<CommonResponse> call = apiService.changeMatchState(areaNo, articleNo, state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    if(state.equals("Y")){
                        Util.showToast(context,"완료 상태로 변경되었습니다.");
                    }else{
                        Util.showToast(context, "진행중 상태로 변경되었습니다.");
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
