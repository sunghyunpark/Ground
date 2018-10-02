package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.FreeArticleModelListResponse;
import base.presenter.BasePresenter;
import model.CommunityArticleModel;
import presenter.view.FreeBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class FreeBoardPresenter extends BasePresenter<FreeBoardView> {

    private Context context;
    private ApiInterface apiService;
    private ArrayList<CommunityArticleModel> communityArticleModelArrayList;

    public FreeBoardPresenter(FreeBoardView view, Context context, ArrayList<CommunityArticleModel> communityArticleModelArrayList){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.communityArticleModelArrayList = communityArticleModelArrayList;
    }

    public void loadFreeBoardData(boolean refresh, int no){
        if(refresh)
            communityArticleModelArrayList.clear();

        Call<FreeArticleModelListResponse> call = apiService.getFreeArticleList(no);
        call.enqueue(new Callback<FreeArticleModelListResponse>() {
            @Override
            public void onResponse(Call<FreeArticleModelListResponse> call, Response<FreeArticleModelListResponse> response) {
                FreeArticleModelListResponse freeArticleModelListResponse = response.body();
                if(freeArticleModelListResponse.getResult().size() > 0) {
                    for(CommunityArticleModel bm : freeArticleModelListResponse.getResult()){
                        Collections.addAll(communityArticleModelArrayList, bm);
                    }
                    getView().setFreeBoardList();
                }
            }

            @Override
            public void onFailure(Call<FreeArticleModelListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
