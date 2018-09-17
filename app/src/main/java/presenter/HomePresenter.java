package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import api.response.BannerListResponse;
import base.presenter.BasePresenter;
import model.ArticleModel;
import model.BannerModel;
import presenter.view.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;
    private ApiInterface apiService;
    private ArrayList<ArticleModel> todayArticleModelArrayList;
    private ArrayList<BannerModel> bannerModelArrayList;

    public HomePresenter(HomeView view, Context context, ArrayList<ArticleModel> todayArticleModelArrayList, ArrayList<BannerModel> bannerModelArrayList){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.todayArticleModelArrayList = todayArticleModelArrayList;
        this.bannerModelArrayList = bannerModelArrayList;
    }

    /**
     * HOME > 상단 배너 리스트 데이터를 받아온다.
     */
    public void loadTopBannerList(){
        Call<BannerListResponse> call = apiService.getHomeBanner();
        call.enqueue(new Callback<BannerListResponse>() {
            @Override
            public void onResponse(Call<BannerListResponse> call, Response<BannerListResponse> response) {
                BannerListResponse bannerListResponse = response.body();
                if(bannerListResponse.getResult().size() > 0) {
                    for(BannerModel bm : bannerListResponse.getResult()){
                        Collections.addAll(bannerModelArrayList, bm);
                    }
                    getView().setBannerPager();
                }
            }

            @Override
            public void onFailure(Call<BannerListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
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
                        getView().notifyTodayMatchArticle(true, size);
                    }else{
                        getView().notifyTodayMatchArticle(false, 0);
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
