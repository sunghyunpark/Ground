package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import api.response.BannerListResponse;
import api.response.UpdateTimeResponse;
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


    public HomePresenter(HomeView view, Context context, ArrayList<ArticleModel> todayArticleModelArrayList){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.todayArticleModelArrayList = todayArticleModelArrayList;
    }

    /**
     * HOME > 자유게시판 New 이미지를 띄우기 위해 업데이트 시간을 받아온다.
     * 다른 item 의 경우 아직 업데이트 시간을 받아올 만한 컨텐츠가 없어서 일단 하드코딩으로 되어있다.
     * 에러가 발생하거나 네트워크가 off 인 경우에도 일단 recyclerview 를 노출시킨다.
     * @param groundUtilUpdateList
     */
    public void loadGroundUtilData(final ArrayList<String> groundUtilUpdateList){
        Call<UpdateTimeResponse> call = apiService.getUpdateTimeList("free");
        call.enqueue(new Callback<UpdateTimeResponse>() {
            @Override
            public void onResponse(Call<UpdateTimeResponse> call, Response<UpdateTimeResponse> response) {
                UpdateTimeResponse updateTimeResponse = response.body();
                if(updateTimeResponse.getCode() == 200){
                    int size = updateTimeResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        groundUtilUpdateList.set(0,updateTimeResponse.getResult().get(i).getUpdatedAt());
                    }
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
                getView().setGroundRecyclerView();
            }

            @Override
            public void onFailure(Call<UpdateTimeResponse> call, Throwable t) {
                // Log error here since request failed
                getView().setGroundRecyclerView();
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * HOME > 상단 배너 리스트 데이터를 받아온다.
     */
    public void loadMainBannerList(final ArrayList<BannerModel> bannerModelArrayList){
        Call<BannerListResponse> call = apiService.getHomeBanner();
        call.enqueue(new Callback<BannerListResponse>() {
            @Override
            public void onResponse(Call<BannerListResponse> call, Response<BannerListResponse> response) {
                BannerListResponse bannerListResponse = response.body();
                if(bannerListResponse.getMainBanner().size() > 0) {
                    for(BannerModel bm : bannerListResponse.getMainBanner()){
                        Collections.addAll(bannerModelArrayList, bm);
                    }
                }
                getView().setBanner(bannerModelArrayList, bannerListResponse.getRBBanner(), bannerListResponse.getTBBanner());
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
