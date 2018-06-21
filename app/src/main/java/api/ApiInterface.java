package api;

import api.response.CommonResponse;
import api.response.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    /**
     * register api
     * @param uid -> user uid
     * @param loginType -> email
     * @param nickName -> user nick name
     * @return
     */
    @FormUrlEncoded
    @POST("api/users")
    Call<RegisterResponse> registerApi(@Field("uid") String uid, @Field("loginType") String loginType, @Field("nickName") String nickName);
}
