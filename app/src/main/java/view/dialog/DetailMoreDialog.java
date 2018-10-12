package view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommunityModel;
import model.MatchArticleModel;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class DetailMoreDialog extends Dialog {

    private String boardType;    //Match 게시판인지 Community 게시판인지 판별하기 위한 type  EXTRA_MATCH_BOARD_TYPE or EXTRA_COMMUNITY_BOARD_TYPE 로 받아온다.
    private MatchArticleModel matchArticleModel;
    private CommunityModel communityModel;
    private DetailMoreDialogListener detailMoreDialogListener;

    @BindView(R.id.edit_article_tv) TextView edit_tv;
    @BindView(R.id.delete_article_tv) TextView delete_tv;
    @BindView(R.id.report_tv) TextView report_tv;

    /**
     * Match 게시판인 경우 생성자
     * @param context
     * @param matchArticleModel
     * @param detailMoreDialogListener
     */
    public DetailMoreDialog(Context context, MatchArticleModel matchArticleModel,
                            DetailMoreDialogListener detailMoreDialogListener){
        super(context);
        this.matchArticleModel = matchArticleModel;
        this.detailMoreDialogListener = detailMoreDialogListener;
        this.boardType = GroundApplication.BOARD_TYPE_MATCH;
    }

    /**
     * Community 게시판인 경우 생성자
     * @param context
     * @param communityModel
     * @param detailMoreDialogListener
     */
    public DetailMoreDialog(Context context, CommunityModel communityModel,
                            DetailMoreDialogListener detailMoreDialogListener){
        super(context);
        this.communityModel = communityModel;
        this.detailMoreDialogListener = detailMoreDialogListener;
        this.boardType = GroundApplication.BOARD_TYPE_COMMUNITY;
    }

    public interface DetailMoreDialogListener{
        public void deleteArticleEvent();
        public void editArticleEvent();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.detail_more_dialog);

        ButterKnife.bind(this);

        init();

    }

    private void init(){
        String writerId = boardType.equals(GroundApplication.BOARD_TYPE_MATCH) ? matchArticleModel.getWriterId() : communityModel.getWriterId();

        if(UserModel.getInstance().getUid().equals(writerId)){
            //내 글
            report_tv.setVisibility(View.GONE);
        }else{
            //내 글이 아닌 경우
            edit_tv.setVisibility(View.GONE);
            delete_tv.setVisibility(View.GONE);
        }
    }

    private void deleteBoard(String boardTypeOfArticle, int articleNo, String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = null;
        if(boardType.equals(GroundApplication.BOARD_TYPE_MATCH)){
            call = apiService.deleteBoard(boardTypeOfArticle, articleNo, uid);
        }else if(boardType.equals(GroundApplication.BOARD_TYPE_COMMUNITY)){
            call = apiService.deleteCommunityBoard(boardTypeOfArticle, articleNo, uid);
        }
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(getContext(), "게시글을 삭제하였습니다.");
                    dismiss();
                    detailMoreDialogListener.deleteArticleEvent();
                }else{
                    Util.showToast(getContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    @OnClick(R.id.edit_article_tv) void editBtn(){
        detailMoreDialogListener.editArticleEvent();
        dismiss();
    }

    @OnClick(R.id.delete_article_tv) void deleteBtn(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("삭제");
        alert.setMessage("정말 삭제 하시겠습니까?");
        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String boardTypeOfArticle = boardType.equals(GroundApplication.BOARD_TYPE_MATCH) ? matchArticleModel.getMatchBoardType() : communityModel.getBoardType();
                int noOfArticle = boardType.equals(GroundApplication.BOARD_TYPE_MATCH) ? matchArticleModel.getNo() : communityModel.getNo();
                deleteBoard(boardTypeOfArticle, noOfArticle, UserModel.getInstance().getUid());
            }
        });
        alert.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                    }
                });
        alert.show();
        dismiss();
    }

    @OnClick(R.id.report_tv) void reportBtn(){
        String boardTypeOfArticle = boardType.equals(GroundApplication.BOARD_TYPE_MATCH) ? matchArticleModel.getMatchBoardType() : communityModel.getBoardType();
        int noOfArticle = boardType.equals(GroundApplication.BOARD_TYPE_MATCH) ? matchArticleModel.getNo() : communityModel.getNo();

        ReportDialog reportDialog = new ReportDialog(getContext(), "article", boardTypeOfArticle, noOfArticle, 0);
        reportDialog.show();
        dismiss();
    }
}
