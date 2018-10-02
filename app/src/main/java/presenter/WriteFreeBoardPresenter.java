package presenter;

import android.content.Context;
import android.util.Log;

import java.io.File;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import base.presenter.BasePresenter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import presenter.view.WriteFreeBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class WriteFreeBoardPresenter extends BasePresenter<WriteFreeBoardView> {

    private Context context;
    private ApiInterface apiService;

    public WriteFreeBoardPresenter(WriteFreeBoardView view, Context context){
        super(view);
        this.context = context;
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void writeFreeBoard(String uidStr, String titleStr, String contentsStr, String photoPathOfLocal, String photoName){
        final File photoFile = new File(photoPathOfLocal);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        RequestBody uid = RequestBody.create(okhttp3.MultipartBody.FORM, uidStr);
        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, titleStr);
        RequestBody contents = RequestBody.create(okhttp3.MultipartBody.FORM, contentsStr);
        MultipartBody.Part photoBody = MultipartBody.Part.createFormData("photo", photoName, requestFile);

        Call<CommonResponse> call = apiService.writeFreeArticle(uid, title, contents, photoBody);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "글을 작성하였습니다.");
                    if(photoFile.exists()){
                        photoFile.delete();
                    }
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
