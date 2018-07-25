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

    public AreaBoardPresenter(Context context, AreaBoardView view, AreaBoardAdapter adapter, ArrayList<ArticleModel> articleModelArrayList){
        super(view);
        this.context = context;
        this.adapter = adapter;
        this.articleModelArrayList = articleModelArrayList;
    }
    /**
     * 게시판 리스트
     * @param areaNo
     * @param articleNo
     */
    public void loadArticleList(boolean refresh, int areaNo, int articleNo, String boardType){
        if(refresh)
            articleModelArrayList.clear();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getAreaBoardList(boardType, areaNo, articleNo);
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
