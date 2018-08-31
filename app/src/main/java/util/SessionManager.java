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
    private static final String KEY_COMMENT_PUSH = "isOn";
    private static final String KEY_MATCH_PUSH = "isMatch";
    private static final String KEY_EVENT_PUSH = "isEvent";


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

    public void setCommentPush(boolean isOn){
        editor.putBoolean(KEY_COMMENT_PUSH, isOn);
        editor.commit();
    }

    public boolean isCommentPushOn(){
        return pref.getBoolean(KEY_COMMENT_PUSH, true);
    }

    public void setMatchPush(boolean isOn){
        editor.putBoolean(KEY_MATCH_PUSH, isOn);
        editor.commit();
    }

    public boolean isMatchPushOn(){
        return pref.getBoolean(KEY_MATCH_PUSH, true);
    }

    public void setEventPush(boolean isOn){
        editor.putBoolean(KEY_EVENT_PUSH, isOn);
        editor.commit();
    }

    public boolean isEventPushOn(){
        return pref.getBoolean(KEY_EVENT_PUSH, true);
    }


}
