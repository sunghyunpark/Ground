package util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yssh.ground.MainActivity;
import com.yssh.ground.SessionManager;

import api.ApiClient;
import api.ApiInterface;
import api.response.LoginResponse;
import database.RealmConfig;
import database.model.UserVO;
import io.realm.Realm;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginManager {

    private Context context;
    private FirebaseAuth mAuth;
    private Realm realm;
    private RealmConfig realmConfig;
    private SessionManager sessionManager;

    public LoginManager(Context context){
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.realmConfig = new RealmConfig();
        this.sessionManager = new SessionManager(context);
    }

    public void RealmDestroy(){
        if(this.realm != null)
            this.realm.close();
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

        Call<LoginResponse> call = apiService.registerAPI(uid, loginType, nickName);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(loginResponse.getCode() == 200){
                    //토스트를 중앙에 띄워준다.
                    Util.showToast(context, "success");

                    Log.d("userData", "code : "+ loginResponse.getCode()+"\n"+
                    "message : "+ loginResponse.getMessage()+"\n"+
                    "uid : "+ loginResponse.getResult().getUid()+"\n"+
                    "loginType : "+ loginResponse.getResult().getLoginType()+"\n"+
                    "nickName : "+ loginResponse.getResult().getNickName());

                    insertUserData(loginResponse.getResult().getUid(), loginResponse.getResult().getLoginType(), loginResponse.getResult().getEmail(),
                            loginResponse.getResult().getNickName(), loginResponse.getResult().getProfile(), loginResponse.getResult().getProfileThumb(),
                            loginResponse.getResult().getCreatedAt());

                    goMainActivity();
                }else{
                    Toast.makeText(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 로그인 후 server 로 GET 요청 후 데이터를 받아옴
     * @param uid
     */
    public void getUserDataForLogin(String uid, final String email){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<LoginResponse> call = apiService.loginApi(uid);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(loginResponse.getCode() == 200){
                    Log.d("userData", "code : "+ loginResponse.getCode()+"\n"+
                            "message : "+ loginResponse.getMessage()+"\n"+
                            "uid : "+ loginResponse.getResult().getUid()+"\n"+
                            "loginType : "+ loginResponse.getResult().getLoginType()+"\n"+
                            "nickName : "+ loginResponse.getResult().getNickName());

                    insertUserData(loginResponse.getResult().getUid(), loginResponse.getResult().getLoginType(), email,
                            loginResponse.getResult().getNickName(), loginResponse.getResult().getProfile(), loginResponse.getResult().getProfileThumb(),
                            loginResponse.getResult().getCreatedAt());

                    goMainActivity();
                }else{
                    Toast.makeText(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Realm DB 에 user data를 insert 한뒤 싱글톤 객체에 저장
     * @param uid
     * @param loginType
     * @param email
     * @param nickName
     * @param profile
     * @param profileThumb
     * @param createdAt
     */
    private void insertUserData(String uid, String loginType, String email, String nickName, String profile, String profileThumb, String createdAt){
        realm = Realm.getInstance(realmConfig.UserRealmVersion(context));
        realm.beginTransaction();

        UserVO userVO = new UserVO();
        userVO.setUid(uid);
        userVO.setLoginType(loginType);
        userVO.setEmail(email);
        userVO.setNickName(nickName);
        userVO.setProfile(profile);
        userVO.setProfileThumb(profileThumb);
        userVO.setCreatedAt(createdAt);

        realm.copyToRealmOrUpdate(userVO);
        realm.commitTransaction();

        updateUserData(uid);
    }

    /**
     * UserModel 싱글톤 객체에 데이터를 저장
     * @param uid
     */
    public void updateUserData(String uid){
        realm = Realm.getInstance(realmConfig.UserRealmVersion(context));

        UserVO userVO = realm.where(UserVO.class).equalTo("uid",uid).findFirst();
        UserModel.getInstance().setUid(uid);
        UserModel.getInstance().setLoginType(userVO.getLoginType());
        UserModel.getInstance().setEmail(userVO.getEmail());
        UserModel.getInstance().setNickName(userVO.getNickName());
        UserModel.getInstance().setProfile(userVO.getProfile());
        UserModel.getInstance().setProfileThumb(userVO.getProfileThumb());
        UserModel.getInstance().setCreatedAt(userVO.getCreatedAt());
    }

    /**
     * Firebase Logout
     * @param uid
     */
    public void logOut(String uid){
        mAuth.signOut();
        realm = Realm.getInstance(realmConfig.UserRealmVersion(context));
        UserVO userVO = realm.where(UserVO.class).equalTo("uid",uid).findFirst();
        realm.beginTransaction();
        userVO.deleteFromRealm();
        realm.commitTransaction();
    }

    private void goMainActivity(){
        sessionManager.setLogin(true);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
