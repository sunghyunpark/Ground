package model;

import java.io.Serializable;

/**
 * MatchArticle Model 은 매칭/용병/모집 에만 적용된다.
 */
public class MatchArticleModel implements Serializable {
    private int no;
    private String matchBoardType;
    private int areaNo;
    private String writerId;
    private String nickName;
    private String profile;
    private String profileThumb;
    private String title;
    private String contents;
    private String matchState;
    private String matchDate;
    private String averageAge;
    private int charge;
    private int playRule;
    private String blocked;
    private int viewCnt;
    private String commentCnt;
    private String createdAt;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMatchBoardType() {
        return matchBoardType;
    }

    public void setMatchBoardType(String matchBoardType) {
        this.matchBoardType = matchBoardType;
    }

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
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

    public String getMatchState() {
        return matchState;
    }

    public void setMatchState(String matchState) {
        this.matchState = matchState;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(String averageAge) {
        this.averageAge = averageAge;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getPlayRule() {
        return playRule;
    }

    public void setPlayRule(int playRule) {
        this.playRule = playRule;
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
