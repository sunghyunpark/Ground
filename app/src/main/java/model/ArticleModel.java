package model;

import com.google.gson.annotations.SerializedName;

public class ArticleModel {
    private int no;
    @SerializedName("board_type")
    private String boardType;
    @SerializedName("area_no")
    private int areaNo;
    @SerializedName("writer_id")
    private String WriterId;
    @SerializedName("nick_name")
    private String nickName;
    private String title;
    private String contents;
    private String blocked;
    @SerializedName("view_cnt")
    private String viewCnt;
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

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(String viewCnt) {
        this.viewCnt = viewCnt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
