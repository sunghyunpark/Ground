package model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArticleModel implements Serializable {
    private int no;
    @SerializedName("board_type")
    private String boardType;
    @SerializedName("area_no")
    private int areaNo;
    @SerializedName("writer_id")
    private String WriterId;
    @SerializedName("nick_name")
    private String nickName;
    private String profile;
    @SerializedName("profile_thumb")
    private String profileThumb;
    private String title;
    private String contents;
    @SerializedName("match_state")
    private String matchState;
    private String blocked;
    @SerializedName("view_cnt")
    private int viewCnt;
    @SerializedName("comment_cnt")
    private String commentCnt;
    @SerializedName("created_at")
    private String createdAt;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
    }

    public String getWriterId() {
        return WriterId;
    }

    public void setWriterId(String writerId) {
        WriterId = writerId;
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

    public String getMatchState() {
        return matchState;
    }

    public void setMatchState(String matchState) {
        this.matchState = matchState;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public String getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(String commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
