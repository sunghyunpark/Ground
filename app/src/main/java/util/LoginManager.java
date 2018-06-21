package util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import api.response.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginManager {

    private Context context;
    private FirebaseAuth mAuth;

    public LoginManager(Context context){
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
    }

    /**
     * 회원가입 후 server 로 전송.
     * @param uid
     * @param loginType
     * @param nickName
     */
    public void postUserDataForRegister(String uid, String loginType, String nickName){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegisterResponse> call = apiService.registerApi(uid, loginType, nickName);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if(registerResponse.getCode() == 200){
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    Log.d("userData", "code : "+registerResponse.getCode()+"\n"+
                    "message : "+registerResponse.getMessage()+"\n"+
                    "uid : "+registerResponse.getResult().getUid()+"\n"+
                    "loginType : "+registerResponse.getResult().getLoginType()+"\n"+
                    "nickName : "+registerResponse.getResult().getNickName());
                }else{
                    Toast.makeText(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
