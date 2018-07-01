package util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
import api.response.CommentListResponse;
import api.response.CommonResponse;
import model.ArticleModel;
import model.CommentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.adapter.AboutAreaBoardAdapter;
import util.adapter.CommentAdapter;
import view.AboutAreaBoardActivity;

public class BoardManager {
    private Context context;

    public BoardManager(Context context){
        this.context = context;
    }

    /**
     * 매칭 게시판에서 게시글 작성 후 전송
     * @param areaNo -> 지역 고유 no
     * @param uid
     * @param title
     * @param contents
     */
    public void postMatchingBoard(int areaNo, String uid, String title, String contents){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.writeBoard(areaNo, uid, title, contents);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "글을 작성하였습니다.");
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
     * 매칭 지역 게시판의 리스트를 받아옴
     * @param areaNo
     * @param aboutAreaBoardAdapter
     * @param articleModelArrayList
     */
    public void getMatchingBoard(int areaNo, int no, final AboutAreaBoardAdapter aboutAreaBoardAdapter, final ArrayList<ArticleModel> articleModelArrayList){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = apiService.getAboutAreaBoardLIst(areaNo, no);
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    int size = aboutAreaBoardListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        articleModelArrayList.add(aboutAreaBoardListResponse.getResult().get(i));
                        Log.d("boardData No : ", aboutAreaBoardListResponse.getResult().get(i).getNo()+"\n"+
                        "boardData WriterId : "+aboutAreaBoardListResponse.getResult().get(i).getWriterId()+"\n"+
                        "boardData title"+aboutAreaBoardListResponse.getResult().get(i).getTitle()+"\n"+
                        "boardData contents : "+aboutAreaBoardListResponse.getResult().get(i).getContents()+"\n"+
                        "boardData created_at : "+aboutAreaBoardListResponse.getResult().get(i).getCreatedAt());
                    }
                    aboutAreaBoardAdapter.notifyDataSetChanged();
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 게시글 화면에서 댓글 작성
     * @param areaNo
     * @param no
     * @param writer_id
     * @param comment
     * @param comment_et
     */
    public void writerComment(int areaNo, final int no, String writer_id, String comment, final EditText comment_et, final String boardType, final TextView empty_tv, final RecyclerView commentRecyclerview,
                              final ArrayList<CommentModel> commentModelArrayList, final CommentAdapter commentAdapter, final TextView comment_cnt_tv){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.writeComment(areaNo, no, writer_id, comment);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "댓글을 작성하였습니다.");
                    comment_et.setText(null);
                    getCommentList(no, 0, boardType, empty_tv, commentRecyclerview, commentModelArrayList, commentAdapter, comment_cnt_tv);
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
     * Article Comment
     * @param articleNo
     * @param boardType
     * @param empty_tv
     * @param commentRecyclerview
     * @param commentModelArrayList
     * @param commentAdapter
     * @param comment_cnt_tv
     */
    public void getCommentList(int articleNo, int no, String boardType, final TextView empty_tv, final RecyclerView commentRecyclerview,
                               final ArrayList<CommentModel> commentModelArrayList, final CommentAdapter commentAdapter, final TextView comment_cnt_tv){
        if(commentModelArrayList != null){
            commentModelArrayList.clear();
        }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommentListResponse> call = apiService.getCommentList(articleNo, boardType, no);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();
                    if(size > 0){
                        //exist commentList
                        comment_cnt_tv.setText("댓글 "+size);
                        empty_tv.setVisibility(View.GONE);
                        commentRecyclerview.setVisibility(View.VISIBLE);
                        for(int i=0;i<size;i++){
                            commentModelArrayList.add(commentListResponse.getResult().get(i));
                        }
                        commentAdapter.notifyDataSetChanged();
                    }else{
                        //not exist commentList
                        comment_cnt_tv.setText("댓글 0");
                        empty_tv.setVisibility(View.VISIBLE);
                        commentRecyclerview.setVisibility(View.GONE);
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
}
