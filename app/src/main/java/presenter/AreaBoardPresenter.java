package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
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
    private String from;    // match / hire / recruit 어디서부터 접근을 했는지

    public AreaBoardPresenter(Context context, AreaBoardView view, AreaBoardAdapter adapter, ArrayList<ArticleModel> articleModelArrayList, String from){
        super(view);
        this.context = context;
        this.adapter = adapter;
        this.articleModelArrayList = articleModelArrayList;
        this.from = from;
    }
    /**
     * 매칭 게시판 리스트
     * @param areaNo
     * @param articleNo
     */
    public void loadArticleList(int areaNo, int articleNo){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call;
        switch (from){
            case "match":
                call = apiService.getMatchAreaBoardList(areaNo, articleNo);
                break;
            case "hire":
                call = apiService.getHireAreaBoardList(areaNo, articleNo);
                break;
            case "recruit":
                call = apiService.getRecruitAreaBoardList(areaNo, articleNo);
                break;
                default:
                    call = apiService.getMatchAreaBoardList(areaNo, articleNo);
                    break;
        }
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    int size = aboutAreaBoardListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        articleModelArrayList.add(aboutAreaBoardListResponse.getResult().get(i));
                        Log.d("ArticleLIst","boardData No : "+ aboutAreaBoardListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+aboutAreaBoardListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+aboutAreaBoardListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+aboutAreaBoardListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+aboutAreaBoardListResponse.getResult().get(i).getCreatedAt());
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
