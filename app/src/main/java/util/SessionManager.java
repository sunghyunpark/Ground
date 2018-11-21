package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ground";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_PUSH_COMMENT_OF_MATCH = "commentOfMatch";
    private static final String KEY_PUSH_COMMENT_OF_FREE = "commentOfFree";
    private static final String KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED = "myFavoriteArticleMatched";
    private static final String KEY_PUSH_MATCH_DATE_ALARM = "matchDateAlarm";
    private static final String KEY_EVENT_PUSH = "isEvent";
    //Oreo Push Channel
    private static final String PUSH_CHANNEL_COMMENT_OF_MATCH = "commentOfMatch";
    private static final String PUSH_CHANNEL_COMMENT_OF_COMMUNITY = "commentOfCommunity";
    private static final String PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED = "myFavoriteArticleMatched";
    private static final String PUSH_CHANNEL_MATCH_DATE_ALARM = "matchDateAlarm";
    private static final String PUSH_CHANNEL_EVENT = "event";


    public SessionManager(Context context) {
        Context _context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    // 로그인 상태 여부 확인
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    // 매칭 게시글에 댓글이 달렸을 경우 푸시를 받을껀지 셋팅
   public void setPushCommentOfMatch(boolean commentOfMatch){
        editor.putBoolean(KEY_PUSH_COMMENT_OF_MATCH, commentOfMatch);
        editor.commit();
    }

    // 매칭 게시글에 댓글이 달렸을 경우 푸시 받을껀지 체크 여부 확인
    public boolean isPushCommentOfMatch(){
        return pref.getBoolean(KEY_PUSH_COMMENT_OF_MATCH, true);
    }

    // 자유게시판에 댓글이 달렸을 경우 푸시를 받을껀지 셋팅
    public void setPushCommentOfFree(boolean commentOfFree){
        editor.putBoolean(KEY_PUSH_COMMENT_OF_FREE, commentOfFree);
        editor.commit();
    }

    // 자유게시판에 댓글이 달렸을 경우 푸시 받을껀지 체크 여부 확인
    public boolean isPushCommentOfFree(){
        return pref.getBoolean(KEY_PUSH_COMMENT_OF_FREE, true);
    }

    // 관심있는 매칭 게시글이 매칭상태가 '완료'로 바뀌었을 때 푸시를 받을껀지 셋팅
    public void setPushMyFavoriteArticleMatched(boolean isOn){
        editor.putBoolean(KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED, isOn);
        editor.commit();
    }

    // 관심있는 매칭 게시글이 매칭 상태가 '완료'로 바뀌었을 때 푸시를 받을껀지 체크 여부 확인
    public boolean isMyFavoriteArticleMatchedOn(){
        return pref.getBoolean(KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED, true);
    }

    // 이벤트 푸시 받을껀지 셋팅
    public void setEventPush(boolean isOn){
        editor.putBoolean(KEY_EVENT_PUSH, isOn);
        editor.commit();
    }

    // 이벤트 푸시 받을껀지 체크 여부 확인
    public boolean isEventPushOn(){
        return pref.getBoolean(KEY_EVENT_PUSH, true);
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하지 않으면 생성 후 true 로 저장하여 보관한다.
     * @param isExist
     */
    public void setPushChannelCommentOfMatch(boolean isExist){
        editor.putBoolean(PUSH_CHANNEL_COMMENT_OF_MATCH, isExist);
        editor.commit();
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하는지 안하는지 체크
     * @return
     */
    public boolean isPushChannelCommentOfMatch(){
        return pref.getBoolean(PUSH_CHANNEL_COMMENT_OF_MATCH, false);
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하지 않으면 생성 후 true 로 저장하여 보관한다.
     * @param isExist
     */
    public void setPushChannelCommentOfCommunity(boolean isExist){
        editor.putBoolean(PUSH_CHANNEL_COMMENT_OF_COMMUNITY, isExist);
        editor.commit();
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하는지 안하는지 체크
     * @return
     */
    public boolean isPushChannelCommentOfCommunity(){
        return pref.getBoolean(PUSH_CHANNEL_COMMENT_OF_COMMUNITY, false);
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하지 않으면 생성 후 true 로 저장하여 보관한다.
     * @param isExist
     */
    public void setPushChannelMyFavoriteArticleMatched(boolean isExist){
        editor.putBoolean(PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED, isExist);
        editor.commit();
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하는지 안하는지 체크
     * @return
     */
    public boolean isPushChannelMyFavoriteArticleMatched(){
        return pref.getBoolean(PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED, false);
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하지 않으면 생성 후 true 로 저장하여 보관한다.
     * @param isExist
     */
    public void setPushChannelEvent(boolean isExist){
        editor.putBoolean(PUSH_CHANNEL_EVENT, isExist);
        editor.commit();
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하는지 안하는지 체크
     * @return
     */
    public boolean isPushChannelEvent(){
        return pref.getBoolean(PUSH_CHANNEL_EVENT, false);
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하지 않으면 생성 후 true 로 저장하여 보관한다.
     * @param isExist
     */
    public void setPushMatchDateAlarm(boolean isExist){
        editor.putBoolean(PUSH_CHANNEL_MATCH_DATE_ALARM, isExist);
        editor.commit();
    }

    /**
     * Oreo 이상의 버전에서 알림 채널이 존재하는지 안하는지 체크
     * @return
     */
    public boolean isPushChannelMatchDateAlarm(){
        return pref.getBoolean(PUSH_CHANNEL_MATCH_DATE_ALARM, false);
    }

}
