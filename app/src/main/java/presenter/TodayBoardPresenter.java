package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import base.presenter.BasePresenter;
import model.MatchArticleModel;
import presenter.view.TodayBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class TodayBoardPresenter extends BasePresenter<TodayBoardView> {

    private Context context;
    private ApiInterface apiService;
    private ArrayList<MatchArticleModel> todayMatchArticleModelArrayList;

    public TodayBoardPresenter(TodayBoardView view, Context context, ArrayList<MatchArticleModel> todayMatchArticleModelArrayList){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.todayMatchArticleModelArrayList = todayMatchArticleModelArrayList;
    }

    /**
     * 오늘의 시합 데이터를 받아온다.
     */
    public void loadTodayMatchList(boolean refresh, int no, int limit){
        if(refresh){
            todayMatchArticleModelArrayList.clear();
        }
        Call<ArticleModelListResponse> call = apiService.getTodayMatchArticleList(no, limit);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    if(size > 0){
                        for(MatchArticleModel am : articleModelListResponse.getResult()){
                            Collections.addAll(todayMatchArticleModelArrayList, am);
                        }
                        getView().notifyTodayMatchArticle();
                    }
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
}
