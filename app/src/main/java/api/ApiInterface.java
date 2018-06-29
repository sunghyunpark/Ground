package api;

import api.response.AboutAreaBoardListResponse;
import api.response.CommonResponse;
import api.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    /**
     * write matching board api
     * @param areaNo
     * @param uid
     * @param title
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/matching")
    Call<CommonResponse> writeBoard(@Field("areaNo") int areaNo, @Field("uid") String uid, @Field("title") String title,
                                    @Field("contents") String contents);

    /**
     * get area board List api
     * @param areaNo
     * @return
     */
    @GET("api/boards/matching/{areaNo}")
    Call<AboutAreaBoardListResponse> getAboutAreaBoardLIst(@Path("areaNo") int areaNo);

    /**
     * get Article view api
     * @param areaNo
     * @param no
     * @return
     */
    @GET("api/boards/matching/view/{areaNo}/{no}")
    Call<AboutAreaBoardListResponse> getAboutBoard(@Path("areaNo") int areaNo, @Path("no") int no);

    /**
     * write Article Comment api
     * @param areaNo
     * @param no
     * @param writer_id
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/matching/view/comment")
    Call<CommonResponse> writeComment(@Field("areaNo") int areaNo, @Field("no") int no, @Field("writer_id") String writer_id, @Field("comment") String comment);

}
