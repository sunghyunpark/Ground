package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.yssh.ground.R;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.LoginPresenter;
import presenter.view.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class EditProfileDialog extends Dialog implements LoginView{

    private EditProfileDialogListener editProfileDialogListener;
    private LoginPresenter loginPresenter;
    private ApiInterface apiService;

    @BindView(R.id.user_name_et) EditText user_name_et;
    @BindString(R.string.error_not_exist_input_txt) String notExistErrorStr;

    public EditProfileDialog(Context context, EditProfileDialogListener editProfileDialogListener){
        super(context);
        this.editProfileDialogListener = editProfileDialogListener;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.loginPresenter = new LoginPresenter(this, getContext());
    }

    public interface EditProfileDialogListener{
        public void editUserNameEvent();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.edit_profile_dialog);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        user_name_et.setText(UserModel.getInstance().getNickName());
    }

    /**
     * 서버로 이름 수정 보냄
     * @param nickName
     */
    private void editProfile(String nickName){
        Call<CommonResponse> call = apiService.editProfile(UserModel.getInstance().getUid(), nickName);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(getContext(), "프로필을 수정하였습니다.");
                    UserModel.getInstance().setNickName(user_name_et.getText().toString());
                    editProfileDialogListener.editUserNameEvent();
                    loginPresenter.insertUserData(UserModel.getInstance().getUid(), UserModel.getInstance().getLoginType(), UserModel.getInstance().getEmail(), user_name_et.getText().toString(), UserModel.getInstance().getProfile(),
                            UserModel.getInstance().getProfileThumb(), UserModel.getInstance().getFcmToken(), UserModel.getInstance().getCreatedAt());
                    dismiss();
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

    @OnClick(R.id.save_btn) void saveBtn(){
        if(user_name_et.getText().toString().length() > 10){
            Toast.makeText(getContext(), "이름(별명)은 최대 10자까지 입니다.", Toast.LENGTH_SHORT).show();
        }else if(user_name_et.getText().toString().trim().equals("")) {
            Toast.makeText(getContext(), notExistErrorStr, Toast.LENGTH_SHORT).show();
        }else{
            editProfile(user_name_et.getText().toString());
        }
    }

    @Override
    public void loginClick(){

    }

    @Override
    public void goMainActivity(){

    }
}
