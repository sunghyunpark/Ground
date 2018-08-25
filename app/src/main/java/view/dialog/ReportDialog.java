package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.Toast;

import com.groundmobile.ground.R;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class ReportDialog extends Dialog {

    private String serviceName, boardType;    // serviceName 의 경우 게시글 신고 / 댓글 신고 구분짓는다. (article / comment)
    private int serviceNo;
    private ApiInterface apiService;

    @BindView(R.id.checkbox1) CheckBox checkBox1;
    @BindView(R.id.checkbox2) CheckBox checkBox2;
    @BindString(R.string.report_checkbox_txt_1) String reportStr1;
    @BindString(R.string.report_checkbox_txt_2) String reportStr2;

    public ReportDialog(Context context, String serviceName, String boardType, int articleNo, int commentNo){
        super(context);
        this.serviceName = serviceName;
        this.boardType = boardType;
        Log.d("ReportDialog", boardType+"123123");
        if(serviceName.equals("article")){
            serviceNo = articleNo;
        }else{
            serviceNo = commentNo;
        }
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.report_dialog);

        ButterKnife.bind(this);
    }

    private void reportContents(String contents){
        Call<CommonResponse> call = apiService.postReportContents(serviceName, serviceNo, boardType, UserModel.getInstance().getUid(), contents);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(getContext(), "신고가 접수되었습니다.");
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

    @OnClick(R.id.cancel_btn) void cancelBtn(){
        dismiss();
    }

    @OnClick(R.id.report_btn) void reportBtn(){
        if((!checkBox1.isChecked()) && (!checkBox2.isChecked())){
            Util.showToast(getContext(), "항목을 선택해주세요.");
        }else{
            String reportStr = "";
            reportStr += (checkBox1.isChecked()) ? reportStr1 : "";
            reportStr += (checkBox2.isChecked()) ? reportStr2 : "";

            reportContents(reportStr);
            dismiss();
        }
    }

}
