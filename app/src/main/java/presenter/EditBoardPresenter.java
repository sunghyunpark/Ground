package presenter;

import android.content.Context;
import android.util.Log;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import base.presenter.BasePresenter;
import model.MatchArticleModel;
import presenter.view.EditBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class EditBoardPresenter extends BasePresenter<EditBoardView> {

    private Context context;
    private ApiInterface apiService;

    public EditBoardPresenter(EditBoardView view, Context context){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void EditBoard(MatchArticleModel matchArticleModel){
        Call<CommonResponse> call = apiService.editBoard(matchArticleModel.getMatchBoardType(), matchArticleModel.getAreaNo(), matchArticleModel.getNo(), matchArticleModel.getTitle(), matchArticleModel.getContents(),
                matchArticleModel.getMatchDate(), matchArticleModel.getAverageAge(), matchArticleModel.getCharge(), matchArticleModel.getPlayRule());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "글을 수정하였습니다.");
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

}
