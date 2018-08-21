package api;

import api.response.ArticleEtcResponse;
import api.response.ArticleModelListResponse;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import api.response.LoginResponse;
import api.response.UpdateTimeResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
     * edit profile(nickName) api
     * @param uid
     * @param userName
     * @return
     */
    @PUT("api/users/profile/{uid}/{userName}")
    Call<CommonResponse> editProfile(@Path("uid") String uid, @Path("userName") String userName);

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
     * edit match/hire/recruit board api
     * @param boardType
     * @param areaNo
     * @param no
     * @param title
     * @param contents
     * @return
     */
    @PUT("api/boards/edit/{boardType}/{areaNo}/{no}/{title}/{contents}")
    Call<CommonResponse> editBoard(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no,
                                   @Path("title") String title, @Path("contents") String contents);

    /**
     * delete match/hire/recruit board api
     * @param boardType
     * @param no
     * @param uid
     * @return
     */
    @DELETE("api/boards/delete/{boardType}/{no}/{uid}")
    Call<CommonResponse> deleteBoard(@Path("boardType") String boardType, @Path("no") int no, @Path("uid") String uid);

    /**
     * change Match State
     * @param areaNo
     * @param articleNo
     * @param state > Y/N
     * @return
     */
    @PUT("api/boards/view/matchState/{areaNo}/{no}/{state}")
    Call<CommonResponse> changeMatchState(@Path("areaNo") int areaNo, @Path("no") int articleNo, @Path("state") String state);

    /**
     * get match area board List api
     * @param areaNo
     * @return
     */
    @GET("api/boards/{boardType}/{areaNo}/{no}")
    Call<ArticleModelListResponse> getAreaBoardList(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no);

    /**
     * get Article view api
     * @param areaNo
     * @param no
     * @return
     */
    @GET("api/boards/{boardType}/view/{areaNo}/{no}/{uid}")
    Call<ArticleEtcResponse> getArticleEtcData(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no,
                                               @Path("uid") String uid);

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
     * delete comment api
     * @param boardType
     * @param commentNo
     * @param articleNo
     * @param areaNo
     * @return
     */
    @DELETE("api/boards/view/comment/delete/{boardType}/{no}/{articleNo}/{areaNo}")
    Call<CommonResponse> deleteComment(@Path("boardType") String boardType, @Path("no") int commentNo, @Path("articleNo") int articleNo, @Path("areaNo") int areaNo);

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
    @GET("api/boards/recent/{boardType}/{no}/{limit}")
    Call<ArticleModelListResponse> getRecentArticleList(@Path("boardType") String boardType, @Path("no") int no, @Path("limit") int limit);

    /**
     * get my Article List API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/article/{boardType}/{uid}/{no}")
    Call<ArticleModelListResponse> getMyArticleList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * get my Comment List API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/comment/{boardType}/{uid}/{no}")
    Call<CommentListResponse> getMyCommentList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * post Favorite Article state api
     * @param state
     * @param articleNo
     * @param uid
     * @param boardType
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/favorite")
    Call<CommonResponse> postFavoriteState(@Field("favoriteState") String state, @Field("articleNo") int articleNo, @Field("uid") String uid,
                                           @Field("boardType") String boardType);

    /**
     * get My Favorite Article List API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/favorite/{boardType}/{uid}/{no}")
    Call<ArticleModelListResponse> getMyFavoriteArticleList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * report article or comment
     * @param serviceName -> article / comment
     * @param serviceNo -> articleNo / commentNo
     * @param boardType
     * @param uid
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("api/support/report/{serviceName}/{serviceNo}/{boardType}/{uid}/{contents}")
    Call<CommonResponse> postReportContents(@Field("serviceName") String serviceName, @Field("serviceNo") int serviceNo, @Field("boardType") String boardType,
                                            @Field("uid") String uid, @Field("contents") String contents);
}
