package util;

import android.content.Context;
import android.util.Log;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardManager {
    private Context context;

    public BoardManager(Context context){
        this.context = context;
    }

    /**
     * 매칭 게시판에서 게시글 작성 후 전송
     * @param areaNo -> 지역 고유 no
     * @param uid
     * @param title
     * @param contents
     */
    public void postMatchingBoard(int areaNo, String uid, String title, String contents){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.writeBoard(areaNo, uid, title, contents);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "글을 작성하였습니다.");
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
