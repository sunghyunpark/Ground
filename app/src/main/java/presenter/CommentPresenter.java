package presenter;

import android.content.Context;
import android.util.Log;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import base.presenter.BasePresenter;
import model.CommentModel;
import presenter.view.CommentView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.CommentAdapter;

/**
 * CommentActivity 에서 사용된다.
 * 댓글의 경우 매치(매치 / 용병 / 모집)와 커뮤니티(자유게시판)이 같아 공통적으로 사용하고자함.
 * 따라서 아래의 타입에 따라 분기처리하여 사용하도록함.
 * boardType -> match(match / hire / recruit) / free
 */
public class CommentPresenter extends BasePresenter<CommentView> {

    private Context context;
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;
    private ApiInterface apiService;
    private String type;    // match(match / hire / recruie) / free

    public CommentPresenter(CommentView view, Context context, ArrayList<CommentModel> commentModelArrayList, CommentAdapter commentAdapter, String type){
        super(view);
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
        this.commentAdapter = commentAdapter;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.type = type;
    }

    public void postComment(final int areaNo, final int articleNo, String writerId, String comment, final String boardType){
        Call<CommonResponse> call = null;

        if(type.equals(Constants.BOARD_TYPE_MATCH)){
            call = apiService.writeComment(areaNo, articleNo, writerId, comment, boardType);
        }else if(type.equals(Constants.BOARD_TYPE_COMMUNITY)){
            call = apiService.writeCommunityArticleComment(articleNo, boardType, writerId, comment);
        }
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 작성하였습니다.");
                    loadComment(true, articleNo, 0, areaNo, boardType);
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

    /**
     * 댓글 삭제
     * @param boardType
     * @param commentNo
     * @param articleNo
     * @param areaNo
     */
    public void deleteComment(String boardType, int commentNo, int articleNo, int areaNo){
        Call<CommonResponse> call = null;

        if(type.equals(Constants.BOARD_TYPE_MATCH)){
            call = apiService.deleteComment(boardType, commentNo, articleNo, areaNo);
        }else if(type.equals(Constants.BOARD_TYPE_COMMUNITY)){
            call = apiService.deleteCommunityComment(boardType, commentNo, articleNo);
        }
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 삭제하였습니다.");
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


    public void loadComment(boolean refresh, int articleNo, final int commentNo, int areaNo, String boardType){
        if(refresh)
            commentModelArrayList.clear();
        Call<CommentListResponse> call = null;

        if(type.equals(Constants.BOARD_TYPE_MATCH)){
            call = apiService.getCommentList(boardType, articleNo, areaNo, commentNo);
        }else if(type.equals(Constants.BOARD_TYPE_COMMUNITY)){
            call = apiService.getCommunityArticleCommentList(boardType, articleNo, commentNo);
        }
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    if(size > 0){
                        getView().initComment(true);
                        for(CommentModel cm : commentListResponse.getResult()){
                            Collections.addAll(commentModelArrayList, cm);
                        }
                        commentAdapter.notifyDataSetChanged();
                        getView().loadMoreComment();
                    }else{
                        getView().initComment(false);
                    }
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    public void loadCommentMore(boolean refresh, int articleNo, final int commentNo, int areaNo, String boardType){
        if(refresh)
            commentModelArrayList.clear();
        Call<CommentListResponse> call = null;
        if(type.equals(Constants.BOARD_TYPE_MATCH)){
            call = apiService.getCommentList(boardType, articleNo, areaNo, commentNo);
        }else if(type.equals(Constants.BOARD_TYPE_COMMUNITY)){
            call = apiService.getCommunityArticleCommentList(boardType, articleNo, commentNo);
        }
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    for(CommentModel cm : commentListResponse.getResult()){
                        Collections.addAll(commentModelArrayList, cm);
                    }
                    commentAdapter.notifyDataSetChanged();
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
