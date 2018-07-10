package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

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

public class CommentPresenter extends BasePresenter<CommentView> {

    private Context context;
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;

    public CommentPresenter(CommentView view, Context context, ArrayList<CommentModel> commentModelArrayList, CommentAdapter commentAdapter){
        super(view);
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
        this.commentAdapter = commentAdapter;
    }

    public void postComment(final int areaNo, final int articleNo, String writerId, String comment, final String boardType){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.writeComment(areaNo, articleNo, writerId, comment, boardType);
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

    public void loadComment(boolean refresh, int articleNo, final int commentNo, int areaNo, String boardType){
        if(refresh)
            commentModelArrayList.clear();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<CommentListResponse> call = apiService.getCommentList(boardType, articleNo, areaNo, commentNo);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    if(size > 0){
                        getView().initComment(true);
                        for(int i=0;i<size;i++){
                            commentModelArrayList.add(commentListResponse.getResult().get(i));
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

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<CommentListResponse> call = apiService.getCommentList(boardType, articleNo, areaNo, commentNo);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        commentModelArrayList.add(commentListResponse.getResult().get(i));
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
