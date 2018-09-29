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
import presenter.view.AreaBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.AreaBoardAdapter;

public class AreaBoardPresenter extends BasePresenter<AreaBoardView> {

    private Context context;
    private AreaBoardAdapter adapter;
    private ArrayList<ArticleModel> articleModelArrayList;
    private ArrayList<BannerModel> bannerModelArrayList;
    private ApiInterface apiService;

    public AreaBoardPresenter(Context context, AreaBoardView view, AreaBoardAdapter adapter, ArrayList<ArticleModel> articleModelArrayList, ArrayList<BannerModel> bannerModelArrayList){
        super(view);
        this.context = context;
        this.adapter = adapter;
        this.articleModelArrayList = articleModelArrayList;
        this.bannerModelArrayList = bannerModelArrayList;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    /**
     * 지역별 게시판 상단 배너
     */
    public void loadTopBannerList(){
        Call<BannerListResponse> call = apiService.getBoardBanner();
        call.enqueue(new Callback<BannerListResponse>() {
            @Override
            public void onResponse(Call<BannerListResponse> call, Response<BannerListResponse> response) {
                BannerListResponse bannerListResponse = response.body();
                if(bannerListResponse.getMainBanner().size() > 0) {
                    for(BannerModel bm : bannerListResponse.getMainBanner()){
                        Collections.addAll(bannerModelArrayList, bm);
                    }
                    getView().setBannerList();
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
     * 게시판 리스트
     * @param areaNo
     * @param articleNo
     */
    public void loadArticleList(boolean refresh, int areaNo, int articleNo, String boardType, String order, String matchDate){
        if(refresh)
            articleModelArrayList.clear();

        Call<ArticleModelListResponse> call = apiService.getAreaBoardList(boardType, areaNo, articleNo, order, matchDate);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    for(ArticleModel am : articleModelListResponse.getResult()){
                        Collections.addAll(articleModelArrayList, am);
                    }
                    adapter.notifyDataSetChanged();
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
