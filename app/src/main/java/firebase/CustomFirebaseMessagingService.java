package firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.MainActivity;
import com.groundmobile.ground.R;

import java.util.Map;

import database.RealmConfig;
import database.model.UserVO;
import io.realm.Realm;
import util.SessionManager;
import view.DetailCommunityActivity;
import view.DetailMatchArticleActivity;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    //Oreo 푸시 채널 ID
    private static final String PUSH_CHANNEL_COMMENT_OF_MATCH = "commentOfMatch";
    private static final String PUSH_CHANNEL_COMMENT_OF_COMMUNITY = "commentOfCommunity";

    //Oreo 푸시 채널명
    private static final String PUSH_CHANNEL_NAME_COMMENT_OF_MATCH = "매치/용병/모집 댓글";
    private static final String PUSH_CHANNEL_NAME_COMMNET_OF_COMMUNITY = "자유게시판 댓글";

    //푸시 타입
    private static final String PUSH_TYPE_COMMENT = "comment";
    private static final String PUSH_TYPE_MATCH = "match";
    private static final String PUSH_TYPE_EVENT = "event";
    private String[] areaNameArray;
    private SessionManager sessionManager;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Resources res = getResources();
        areaNameArray = res.getStringArray(R.array.matching_board_list);
        sessionManager = new SessionManager(getApplicationContext());
        Map<String, String> pushDataMap = remoteMessage.getData();

        switch (pushDataMap.get("type")){
            case PUSH_TYPE_COMMENT:
                if(sessionManager.isCommentPushOn()){
                    sendNotification(pushDataMap);
                }
                break;
            case PUSH_TYPE_MATCH:
                if(sessionManager.isMatchPushOn()){
                    sendNotification(pushDataMap);
                }
                break;
            case PUSH_TYPE_EVENT:
                if(sessionManager.isEventPushOn()){
                    sendNotification(pushDataMap);
                }
                break;
                default:
                    sendNotification(pushDataMap);
                    break;
        }

    }

    /**
     * 로컬 DB에서 uid를 가져온다
     * @return
     */
    private String getUserId(){
        RealmConfig realmConfig = new RealmConfig();
        Realm realm = Realm.getInstance(realmConfig.UserRealmVersion(getApplicationContext()));
        UserVO userVO = realm.where(UserVO.class).findFirst();

        return userVO.getUid();
    }

    private void sendNotification(Map<String, String> dataMap) {
        String channelId = null;
        String channelName = null;

        Intent detailIntent = null;
        if(dataMap.get("type").equals(PUSH_TYPE_COMMENT)){
            // 댓글 푸시인 경우
            if(dataMap.get("boardType").equals(GroundApplication.BOARD_TYPE_MATCH)){
                // Match(match/hire/recruit) 게시글 댓글인 경우
                detailIntent = new Intent(this, DetailMatchArticleActivity.class);
                channelId = PUSH_CHANNEL_COMMENT_OF_MATCH;
                detailIntent.putExtra(GroundApplication.EXTRA_AREA_NAME, areaNameArray[Integer.parseInt(dataMap.get("areaNo"))]);
                detailIntent.putExtra(GroundApplication.EXTRA_AREA_NO, Integer.parseInt(dataMap.get("areaNo")));
                detailIntent.putExtra(GroundApplication.EXTRA_MATCH_BOARD_TYPE, dataMap.get("boardType"));
            }else if(dataMap.get("boardType").equals(GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY)){
                // Community(free) 게시글 댓글인 경우
                detailIntent = new Intent(this, DetailCommunityActivity.class);
                detailIntent.putExtra(GroundApplication.EXTRA_COMMUNITY_BOARD_TYPE, dataMap.get("boardType"));
                channelId = PUSH_CHANNEL_COMMENT_OF_COMMUNITY;
            }
            detailIntent.putExtra(GroundApplication.EXTRA_USER_ID, getUserId());
            detailIntent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, false);
            detailIntent.putExtra(GroundApplication.EXTRA_ARTICLE_NO, Integer.parseInt(dataMap.get("articleNo")));
        }
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(mainIntent);
        stackBuilder.addNextIntent(detailIntent);
        stackBuilder.addParentStack(MainActivity.class);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if(dataMap.get("type").equals(PUSH_TYPE_COMMENT)){
                if(!sessionManager.isPushChannelCommentOfMatch()){
                    channelId = PUSH_CHANNEL_COMMENT_OF_MATCH;
                    channelName = PUSH_CHANNEL_NAME_COMMENT_OF_MATCH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    nManager.createNotificationChannel(mChannel);
                    sessionManager.setPushChannelCommentOfMatch(true);
                }
            }
        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ground_app_icon_72)
                .setContentTitle(dataMap.get("title"))
                .setContentText(dataMap.get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.WHITE, 1500, 1500)
                .setContentIntent(resultPendingIntent);

        nManager.notify(0 /* ID of notification */, nBuilder.build());
    }
}
