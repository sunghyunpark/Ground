package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
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
}
