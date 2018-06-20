package api;

import api.response.CommonResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("users/register")
    Call<CommonResponse> registerApi(@Field("tag") String tag);
}
