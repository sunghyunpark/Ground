package api;

import api.response.AboutAreaBoardListResponse;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import api.response.LoginResponse;
import api.response.UpdateTimeResponse;
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
     * write match/hire/recruit board api
     * @param areaNo
     * @param uid
     * @param title
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/")
    Call<CommonResponse> writeBoard(@Field("areaNo") int areaNo, @Field("uid") String uid, @Field("title") String title,
                                    @Field("contents") String contents, @Field("boardType") String boardType);

    /**
     * get match area board List api
     * @param areaNo
     * @return
     */
    @GET("api/boards/{boardType}/{areaNo}/{no}")
    Call<AboutAreaBoardListResponse> getAreaBoardList(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no);

    /**
     * get Article view api
     * @param areaNo
     * @param no
     * @return
     */
    @GET("api/boards/{boardType}/view/{areaNo}/{no}")
    Call<AboutAreaBoardListResponse> getDetailBoard(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no);

    /**
     * write Article Comment api
     * @param areaNo
     * @param articleNo
     * @param writer_id
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/view/comment")
    Call<CommonResponse> writeComment(@Field("areaNo") int areaNo, @Field("articleNo") int articleNo, @Field("writer_id") String writer_id, @Field("comment") String comment, @Field("boardType") String boardType);

    /**
     * get Article Comment List api
     * @param articleNo
     * @param areaNo
     * @param commentNo
     * @return
     */
    @GET("api/boards/{boardType}/view/{articleNo}/{areaNo}/commentList/{commentNo}")
    Call<CommentListResponse> getCommentList(@Path("boardType") String boardType, @Path("articleNo") int articleNo, @Path("areaNo") int areaNo, @Path("commentNo") int commentNo);

    /**
     * get area Board updated_at
     * boardType > match / hire / recruit
     * @return
     */
    @GET("api/boards/{boardType}/updated")
    Call<UpdateTimeResponse> getUpdateTimeList(@Path("boardType") String boardType);

    /**
     * get recent Board List LIMIT 5 API
     * @return
     */
    @GET("api/boards/{boardType}/recent")
    Call<AboutAreaBoardListResponse> getRecentArticleList(@Path("boardType") String boardType);
}
