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

import com.groundmobile.ground.R;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchArticleModel;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class DetailMoreDialog extends Dialog {

    private MatchArticleModel matchArticleModel;

    private DetailMoreDialogListener detailMoreDialogListener;

    @BindView(R.id.edit_article_tv) TextView edit_tv;
    @BindView(R.id.delete_article_tv) TextView delete_tv;
    @BindView(R.id.report_tv) TextView report_tv;

    public DetailMoreDialog(Context context, MatchArticleModel matchArticleModel,
                            DetailMoreDialogListener detailMoreDialogListener){
        super(context);
        this.matchArticleModel = matchArticleModel;
        this.detailMoreDialogListener = detailMoreDialogListener;
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
        if(UserModel.getInstance().getUid().equals(matchArticleModel.getWriterId())){
            //내 글
            report_tv.setVisibility(View.GONE);
        }else{
            //내 글이 아닌 경우
            edit_tv.setVisibility(View.GONE);
            delete_tv.setVisibility(View.GONE);
        }
    }

    private void deleteBoard(String boardType, int articleNo, String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.deleteBoard(boardType, articleNo, uid);
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
                deleteBoard(matchArticleModel.getMatchBoardType(), matchArticleModel.getNo(), UserModel.getInstance().getUid());
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
        ReportDialog reportDialog = new ReportDialog(getContext(), "article", matchArticleModel.getMatchBoardType(), matchArticleModel.getNo(), 0);
        reportDialog.show();
        dismiss();
    }
}
