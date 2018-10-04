package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleEtcResponse;
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

    public void loadArticleData(int communityNo){
        Call<CommunityModelListResponse> call = apiService.getFreeArticleList(communityNo);
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
    public void loadFavoriteState(final int articleNo, final String uid){
        Call<ArticleEtcResponse> call = apiService.getFreeArticleFavoriteState(articleNo, uid);
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
    public void postFavoriteState(int articleNo, String uid, final String state){
        Call<CommonResponse> call = apiService.postFavoriteStateFreeArticle(state, articleNo, uid);
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
