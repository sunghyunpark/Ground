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
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ground";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_PUSH_COMMENT_OF_MATCH = "commentOfMatch";
    private static final String KEY_PUSH_COMMENT_OF_FREE = "commentOfFree";
    private static final String KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED = "myFavoriteArticleMatched";
    private static final String KEY_EVENT_PUSH = "isEvent";
    //Oreo Push Channel
    private static final String PUSH_CHANNEL_COMMENT_OF_MATCH = "commentOfMatch";
    private static final String PUSH_CHANNEL_COMMENT_OF_COMMUNITY = "commentOfCommunity";
    private static final String PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED = "myFavoriteArticleMatched";
    private static final String PUSH_CHANNEL_EVENT = "event";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setPushCommentOfMatch(boolean commentOfMatch){
        editor.putBoolean(KEY_PUSH_COMMENT_OF_MATCH, commentOfMatch);
        editor.commit();
    }

    public boolean isPushCommentOfMatch(){
        return pref.getBoolean(KEY_PUSH_COMMENT_OF_MATCH, true);
    }

    public void setPushCommentOfFree(boolean commentOfFree){
        editor.putBoolean(KEY_PUSH_COMMENT_OF_FREE, commentOfFree);
        editor.commit();
    }

    public boolean isPushCommentOfFree(){
        return pref.getBoolean(KEY_PUSH_COMMENT_OF_FREE, true);
    }

    public void setPushMyFavoriteArticleMatched(boolean isOn){
        editor.putBoolean(KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED, isOn);
        editor.commit();
    }

    public boolean isMyFavoriteArticleMatchedOn(){
        return pref.getBoolean(KEY_PUSH_MY_FAVORITE_ARTICLE_MATCHED, true);
    }

    public void setEventPush(boolean isOn){
        editor.putBoolean(KEY_EVENT_PUSH, isOn);
        editor.commit();
    }

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

}
