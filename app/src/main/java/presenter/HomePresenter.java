package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import base.presenter.BasePresenter;
import model.ArticleModel;
import presenter.view.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;
    private ApiInterface apiService;
    private ArrayList<ArticleModel> todayArticleModelArrayList;

    public HomePresenter(HomeView view, Context context, ArrayList<ArticleModel> todayArticleModelArrayList){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.todayArticleModelArrayList = todayArticleModelArrayList;
    }

    /**
     * 오늘의 시합 데이터를 받아온다.
     */
    public void loadTodayMatchList(){
        if(todayArticleModelArrayList != null){
            todayArticleModelArrayList.clear();
        }
        Call<ArticleModelListResponse> call = apiService.getTodayMatchArticleList(0, 5);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    if(size > 0){
                        for(ArticleModel am : articleModelListResponse.getResult()){
                            Collections.addAll(todayArticleModelArrayList, am);
                        }
                        getView().notifyTodayMatchArticle(true);
                    }else{
                        getView().notifyTodayMatchArticle(false);
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
