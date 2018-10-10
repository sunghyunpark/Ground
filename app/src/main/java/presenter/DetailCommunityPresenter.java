package presenter;

import android.content.Context;
import android.util.Log;

import com.groundmobile.ground.GroundApplication;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleEtcResponse;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import api.response.CommunityModelListResponse;
import base.presenter.BasePresenter;
import model.CommentModel;
import presenter.view.DetailCommunityView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.CommentAdapter;

public class DetailCommunityPresenter extends BasePresenter<DetailCommunityView> {

    private Context context;
    //Comment Data
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;
    private ApiInterface apiService;

    public DetailCommunityPresenter(DetailCommunityView view, Context context, ArrayList<CommentModel> commentModelArrayList, CommentAdapter commentAdapter){
        super(view);
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
        this.commentAdapter = commentAdapter;
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    /**
     * DetailCommunity 화면 진입 시 data 가 없는 경우 (ex. 푸시를 타고 진입하는 경우) 에는 본 메소드를 통해 새롭게 data 를 받아온다.
     * @param communityNo
     */
    public void loadArticleData(int communityNo, String typeOfCommunity){
        Call<CommunityModelListResponse> call = null;

        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            call = apiService.getCommunityArticleList(typeOfCommunity, communityNo);
        }
        call.enqueue(new Callback<CommunityModelListResponse>() {
            @Override
            public void onResponse(Call<CommunityModelListResponse> call, Response<CommunityModelListResponse> response) {
                CommunityModelListResponse communityModelListResponse = response.body();
                if(communityModelListResponse.getCode() == 200){
                    getView().loadArticleData(communityModelListResponse.getResult().get(0));
                }
            }

            @Override
            public void onFailure(Call<CommunityModelListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 자유게시판 좋아요 상태
     * @param articleNo
     * @param uid
     */
    public void loadFavoriteState(final int articleNo, final String uid, String typeOfCommunity){
        Call<ArticleEtcResponse> call = null;
        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            call = apiService.getCommunityArticleFavoriteState(typeOfCommunity, articleNo, uid);
        }
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
     * 해당 디테일뷰 좋아요
     * @param articleNo
     * @param uid
     * @param state
     */
    public void postFavoriteState(int articleNo, String uid, final String state, String typeOfCommunity){
        Call<CommonResponse> call = null;
        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            call = apiService.postFavoriteStateCommunityArticle(state, articleNo, uid, typeOfCommunity);
        }
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

    public void loadComment(boolean refresh, int articleNo, final int commentNo, String typeOfCommunity){
        if(refresh)
            commentModelArrayList.clear();

        Call<CommentListResponse> call = null;
        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            call = apiService.getCommunityArticleCommentList(typeOfCommunity, articleNo, commentNo);
        }
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

    public void postComment(final int articleNo, String writerId, String comment, final String typeOfCommunity){
        Call<CommonResponse> call = null;

        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            call = apiService.writeCommunityArticleComment(articleNo, typeOfCommunity, writerId, comment);
        }
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 작성하였습니다.");
                    loadComment(true, articleNo, 0, typeOfCommunity);
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
