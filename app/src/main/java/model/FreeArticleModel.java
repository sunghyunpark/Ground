package model;

import com.google.gson.annotations.SerializedName;

public class FreeArticleModel {
    private int no;
    @SerializedName("writer_id")
    private String writerId;
    @SerializedName("nick_name")
    private String nickName;
    private String profile;
    @SerializedName("profile_thumb")
    private String profileThumb;
    private String title;
    private String contents;
    @SerializedName("photo")
    private String photoUrl;
    @SerializedName("photo_thumb")
    private String photoThumbUrl;
    @SerializedName("view_cnt")
    private int viewCnt;
    @SerializedName("comment_cnt")
    private int commentCnt;
    @SerializedName("created_at")
    private String createdAt;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
