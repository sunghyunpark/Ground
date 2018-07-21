package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

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
    public void loadArticleList(int areaNo, int articleNo, String boardType){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getAreaBoardList(boardType, areaNo, articleNo);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        articleModelArrayList.add(articleModelListResponse.getResult().get(i));
                        Log.d("ArticleLIst","boardData No : "+ articleModelListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+ articleModelListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+ articleModelListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+ articleModelListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+ articleModelListResponse.getResult().get(i).getCreatedAt());
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
