package api;

import api.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<LoginResponse> registerAPI(@Field("uid") String uid, @Field("loginType") String loginType, @Field("nickName") String nickName);

    /**
     * login api
     * @param uid
     * @return
     */
    @GET("api/users/{uid}")
    Call<LoginResponse> loginApi(@Path("uid") String uid);
}
