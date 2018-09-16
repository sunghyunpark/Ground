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
     * 회원가입 API
     * @param uid -> user uid
     * @param loginType -> email
     * @param nickName -> user nick name
     * @return
     */
    @FormUrlEncoded
    @POST("api/users")
    Call<LoginResponse> registerAPI(@Field("uid") String uid, @Field("loginType") String loginType, @Field("nickName") String nickName, @Field("fcmToken") String fcmToken);

    /**
     * 로그인 API
     * @param uid
     * @return
     */
    @GET("api/users/{uid}")
    Call<LoginResponse> loginApi(@Path("uid") String uid);

    /**
     * 프로필 수정(닉네임) API
     * @param uid
     * @param userName
     * @return
     */
    @FormUrlEncoded
    @PUT("api/users/profile/")
    Call<CommonResponse> editProfile(@Field("uid") String uid, @Field("userName") String userName);

    /**
     * User FCM 토큰 갱신 API
     * @param fcmToken
     * @param uid
     * @return
     */
    @PUT("api/users/profile/fcmToken/{uid}/{fcmToken}")
    Call<CommonResponse> updateFcmToken(@Path("uid") String uid, @Path("fcmToken") String fcmToken);

    /**
     * 글 쓰기 API
     * @param areaNo
     * @param uid
     * @param title
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("api/boards/")
    Call<CommonResponse> writeBoard(@Field("areaNo") int areaNo, @Field("uid") String uid, @Field("title") String title,
                                    @Field("contents") String contents, @Field("boardType") String boardType, @Field("matchDate") String matchDate,
                                    @Field("averageAge") String averageAge);

    /**
     * 글 수정 API
     * @param boardType
     * @param areaNo
     * @param no
     * @param title
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @PUT("api/boards/edit/")
    Call<CommonResponse> editBoard(@Field("boardType") String boardType, @Field("areaNo") int areaNo, @Field("no") int no,
                                   @Field("title") String title, @Field("contents") String contents, @Field("matchDate") String matchDate,
                                   @Field("averageAge") String averageAge);

    /**
     * 글 삭제 API
     * @param boardType
     * @param no
     * @param uid
     * @return
     */
    @DELETE("api/boards/delete/{boardType}/{no}/{uid}")
    Call<CommonResponse> deleteBoard(@Path("boardType") String boardType, @Path("no") int no, @Path("uid") String uid);

    /**
     * 매칭 상태 변경 API
     * @param areaNo
     * @param articleNo
     * @param state > Y/N
     * @return
     */
    @FormUrlEncoded
    @PUT("api/boards/view/matchState/")
    Call<CommonResponse> changeMatchState(@Field("areaNo") int areaNo, @Field("no") int articleNo, @Field("state") String state);

    /**
     * 게시글 리스트 API
     * @param areaNo
     * @return
     */
    @GET("api/boards/{boardType}/{areaNo}/{no}/{order}/{matchDate}")
    Call<ArticleModelListResponse> getAreaBoardList(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no, @Path("order") String order,
                                                    @Path("matchDate") String matchDate);

    /**
     * 게시글의 좋아요 상태 API
     * @param areaNo
     * @param no
     * @return
     */
    @GET("api/boards/list/detailView/favorite/{boardType}/{areaNo}/{no}/{uid}")
    Call<ArticleEtcResponse> getArticleEtcData(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no,
                                               @Path("uid") String uid);

    /**
     * 게시글 데이터 API
     * @param areaNo
     * @param no
     * @return
     */
    @GET("api/boards/list/detailView/{boardType}/{areaNo}/{no}/{uid}")
    Call<ArticleModelListResponse> getArticleData(@Path("boardType") String boardType, @Path("areaNo") int areaNo, @Path("no") int no,
                                               @Path("uid") String uid);


    /**
     * 게시글 댓글 작성 API
     * @param areaNo
     * @param articleNo
     * @param writer_id
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST("api/comment/view/comment")
    Call<CommonResponse> writeComment(@Field("areaNo") int areaNo, @Field("articleNo") int articleNo, @Field("writer_id") String writer_id, @Field("comment") String comment, @Field("boardType") String boardType);

    /**
     * 게시글 댓글 리스트 API
     * @param articleNo
     * @param areaNo
     * @param commentNo
     * @return
     */
    @GET("api/comment/{boardType}/view/{articleNo}/{areaNo}/commentList/{commentNo}")
    Call<CommentListResponse> getCommentList(@Path("boardType") String boardType, @Path("articleNo") int articleNo, @Path("areaNo") int areaNo, @Path("commentNo") int commentNo);

    /**
     * 게시글 댓글 삭제 API
     * @param boardType
     * @param commentNo
     * @param articleNo
     * @param areaNo
     * @return
     */
    @DELETE("api/comment/view/comment/delete/{boardType}/{no}/{articleNo}/{areaNo}")
    Call<CommonResponse> deleteComment(@Path("boardType") String boardType, @Path("no") int commentNo, @Path("articleNo") int articleNo, @Path("areaNo") int areaNo);

    /**
     * 지역별 게시판 업데이트 시간 API
     * boardType > match / hire / recruit
     * @return
     */
    @GET("api/boards/updated/{boardType}")
    Call<UpdateTimeResponse> getUpdateTimeList(@Path("boardType") String boardType);

    /**
     * 최신 게시글 API
     * @return
     */
    @GET("api/home/recent/{boardType}/{no}/{limit}")
    Call<ArticleModelListResponse> getRecentArticleList(@Path("boardType") String boardType, @Path("no") int no, @Path("limit") int limit);

    /**
     * 오늘의 시합 API
     * @param articleNo
     * @param limit
     * @return
     */
    @GET("api/home/today/{no}/{limit}")
    Call<ArticleModelListResponse> getTodayMatchArticleList(@Path("no") int articleNo, @Path("limit") int limit);

    /**
     * MY > 내 게시글 API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/article/{boardType}/{uid}/{no}")
    Call<ArticleModelListResponse> getMyArticleList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * MY > 내 댓글 API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/comment/{boardType}/{uid}/{no}")
    Call<CommentListResponse> getMyCommentList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * 게시글 좋아요 API
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
     * MY > 관심있는 게시글 API
     * @param boardType
     * @param uid
     * @param no
     * @return
     */
    @GET("api/my/favorite/{boardType}/{uid}/{no}")
    Call<ArticleModelListResponse> getMyFavoriteArticleList(@Path("boardType") String boardType, @Path("uid") String uid, @Path("no") int no);

    /**
     * 게시글 or 댓글 신고하기 API
     * @param serviceName -> article / comment
     * @param serviceNo -> articleNo / commentNo
     * @param boardType
     * @param uid
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("api/support/report")
    Call<CommonResponse> postReportContents(@Field("serviceName") String serviceName, @Field("serviceNo") int serviceNo, @Field("boardType") String boardType,
                                            @Field("uid") String uid, @Field("contents") String contents);
}
