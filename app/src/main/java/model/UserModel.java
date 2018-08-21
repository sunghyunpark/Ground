package model;

import com.google.gson.annotations.SerializedName;


public class UserModel {
    private static volatile UserModel user = null;

    private String uid;
    @SerializedName("login_type")
    private String loginType;
    private String email;
    @SerializedName("nick_name")
    private String nickName;
    private String profile;
    @SerializedName("profile_thumb")
    private String profileThumb;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("created_at")
    private String createdAt;

    public static  UserModel getInstance(){
        if(user == null)
            synchronized (UserModel.class){
                if(user==null){
                    user = new UserModel();
                }
            }

        return user;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfileThumb() {
        return profileThumb;
    }

    public void setProfileThumb(String profileThumb) {
        this.profileThumb = profileThumb;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
