package presenter;

import android.content.Context;
import android.util.Log;

import com.groundmobile.ground.GroundApplication;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommunityModelListResponse;
import base.presenter.BasePresenter;
import model.CommunityModel;
import presenter.view.FreeBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class CommunityPresenter extends BasePresenter<FreeBoardView> {

    private Context context;
    private ApiInterface apiService;
    private ArrayList<CommunityModel> communityModelArrayList;
    private String typeOfCommunity;

    public CommunityPresenter(FreeBoardView view, Context context, ArrayList<CommunityModel> communityModelArrayList, String typeOfCommunity){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.communityModelArrayList = communityModelArrayList;
        this.typeOfCommunity = typeOfCommunity;
    }

    public void loadFreeBoardData(boolean refresh, int no){
        if(refresh)
            communityModelArrayList.clear();
        Call<CommunityModelListResponse> call = null;
        if(typeOfCommunity.equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
            // 자유 게시판인 경우
            call = apiService.getCommunityArticleList(typeOfCommunity, no);
        }
        call.enqueue(new Callback<CommunityModelListResponse>() {
            @Override
            public void onResponse(Call<CommunityModelListResponse> call, Response<CommunityModelListResponse> response) {
                CommunityModelListResponse communityModelListResponse = response.body();
                if(communityModelListResponse.getResult().size() > 0) {
                    for(CommunityModel bm : communityModelListResponse.getResult()){
                        Collections.addAll(communityModelArrayList, bm);
                    }
                    getView().setFreeBoardList();
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
}
