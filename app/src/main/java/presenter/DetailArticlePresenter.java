package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
import api.response.CommentListResponse;
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

    public DetailArticlePresenter(Context context, DetailArticleView view, ArrayList<CommentModel> commentModelArrayList, CommentAdapter commentAdapter){
        super(view);
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
        this.commentAdapter = commentAdapter;
    }

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
    public void loadDetailArticle(int areaNo, int articleNo){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = apiService.getAboutBoard(areaNo, articleNo);
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    articleModel = aboutAreaBoardListResponse.getResult().get(0);
                    getView().setArticleData(articleModel);
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     *
     * @param refresh
     * @param articleNo
     * @param commentNo
     * @param boardType
     */
    public void loadComment(boolean refresh, int articleNo, final int commentNo, String boardType){
        if(refresh)
            commentModelArrayList.clear();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommentListResponse> call = apiService.getCommentList(articleNo, boardType, commentNo);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    if(size > 0){
                        getView().initComment(true);
                        for(int i=0;i<size;i++){
                            commentModelArrayList.add(commentListResponse.getResult().get(i));
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

}
