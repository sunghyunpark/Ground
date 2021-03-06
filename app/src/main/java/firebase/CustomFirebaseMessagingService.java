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
import com.groundmobile.ground.Constants;
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
    private static final String PUSH_CHANNEL_COMMENT_OF_MATCH = "commentOfMatch";    // 매치 게시판(match/hire/recruit)에 댓글이 달렸을 시
    private static final String PUSH_CHANNEL_COMMENT_OF_COMMUNITY = "commentOfCommunity";    // 자유게시판에 댓글이 달렸을 시
    private static final String PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED = "myFavoriteArticleMatched";    // 관심 매치게시판이 '완료'로 상태 변경되었을 때
    private static final String PUSH_CHANNEL_MATCH_DATE_ALARM = "matchDateAlarm";    // 원하는 날짜 게시글 푸시 알림
    private static final String PUSH_CHANNEL_EVENT = "event";    // 이벤트 전체 푸시

    //Oreo 푸시 채널명
    private static final String PUSH_CHANNEL_NAME_COMMENT_OF_MATCH = "매치/용병/모집 댓글";
    private static final String PUSH_CHANNEL_NAME_COMMENT_OF_COMMUNITY = "자유게시판 댓글";
    private static final String PUSH_CHANNEL_NAME_MY_FAVORITE_ARTICLE_MATCHED = "관심 시합 게시글의 상태 변경";
    private static final String PUSH_CHANNEL_NAME_MATCH_DATE_ALARM = "게시글 알림";
    private static final String PUSH_CHANNEL_NAME_EVENT = "이벤트";

    //푸시 타입
    private static final String PUSH_TYPE_COMMENT_OF_MATCH = "commentOfMatch";    // 매치(match/hire/recruit) 게시판의 댓글
    private static final String PUSH_TYPE_COMMENT_OF_FREE = "commentOfFree";    // 자유게시판 댓글
    private static final String PUSH_TYPE_MY_FAVORITE_ARTICLE_MATCHED = "match";    // 관심있는 게시글 매칭 상태 변경
    private static final String PUSH_TYPE_MATCH_DATE_ALARM = "matchDateAlarm";    // 원하는 날짜 및 지역 게시글 알림.
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
            case PUSH_TYPE_COMMENT_OF_MATCH:    // Match(match/hire/recruit) 게시글의 댓글
                if(sessionManager.isPushCommentOfMatch()){
                    sendNotification(pushDataMap);
                }
                break;
            case PUSH_TYPE_COMMENT_OF_FREE:    // Free 게시글의 댓글
                if(sessionManager.isPushCommentOfFree()){
                    sendNotification(pushDataMap);
                }
                break;
            case PUSH_TYPE_MY_FAVORITE_ARTICLE_MATCHED:    // Matching 게시글의 Matching 상태 푸시
                if(sessionManager.isMyFavoriteArticleMatchedOn()){
                    sendNotification(pushDataMap);
                }
                break;
            case PUSH_TYPE_MATCH_DATE_ALARM:    // 원하는 날짜 및 지역 게시글 등록 시 알림
                sendNotification(pushDataMap);
                break;
            case PUSH_TYPE_EVENT:    // 이벤트 푸시
                if(sessionManager.isEventPushOn()){
                    sendNotification(pushDataMap);
                }
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
        if(dataMap.get("type").equals(PUSH_TYPE_COMMENT_OF_MATCH)){    // 댓글 푸시인 경우
            channelId = PUSH_CHANNEL_COMMENT_OF_MATCH;
            detailIntent = new Intent(this, DetailMatchArticleActivity.class);
            detailIntent.putExtra(Constants.EXTRA_AREA_NAME, areaNameArray[Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO))]);
            detailIntent.putExtra(Constants.EXTRA_AREA_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO)));
            detailIntent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, dataMap.get(Constants.EXTRA_BOARD_TYPE));
            detailIntent.putExtra(Constants.EXTRA_USER_ID, getUserId());
            detailIntent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, false);
            detailIntent.putExtra(Constants.EXTRA_ARTICLE_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_ARTICLE_NO)));
        }else if(dataMap.get("type").equals(PUSH_TYPE_COMMENT_OF_FREE)){
            channelId = PUSH_CHANNEL_COMMENT_OF_COMMUNITY;
            detailIntent = new Intent(this, DetailCommunityActivity.class);
            detailIntent.putExtra(Constants.EXTRA_COMMUNITY_BOARD_TYPE, dataMap.get(Constants.EXTRA_BOARD_TYPE));
            detailIntent.putExtra(Constants.EXTRA_USER_ID, getUserId());
            detailIntent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, false);
            detailIntent.putExtra(Constants.EXTRA_ARTICLE_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_ARTICLE_NO)));
        }else if(dataMap.get("type").equals(PUSH_TYPE_MY_FAVORITE_ARTICLE_MATCHED)){
            channelId = PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED;
            detailIntent = new Intent(this, DetailMatchArticleActivity.class);
            detailIntent.putExtra(Constants.EXTRA_AREA_NAME, areaNameArray[Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO))]);
            detailIntent.putExtra(Constants.EXTRA_AREA_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO)));
            detailIntent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, dataMap.get(Constants.EXTRA_BOARD_TYPE));
            detailIntent.putExtra(Constants.EXTRA_USER_ID, getUserId());
            detailIntent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, false);
            detailIntent.putExtra(Constants.EXTRA_ARTICLE_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_ARTICLE_NO)));
        }else if(dataMap.get("type").equals(PUSH_TYPE_MATCH_DATE_ALARM)){
            channelId = PUSH_CHANNEL_MATCH_DATE_ALARM;
            detailIntent = new Intent(this, DetailMatchArticleActivity.class);
            detailIntent.putExtra(Constants.EXTRA_AREA_NAME, areaNameArray[Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO))]);
            detailIntent.putExtra(Constants.EXTRA_AREA_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_AREA_NO)));
            detailIntent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, dataMap.get(Constants.EXTRA_BOARD_TYPE));
            detailIntent.putExtra(Constants.EXTRA_USER_ID, getUserId());
            detailIntent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, false);
            detailIntent.putExtra(Constants.EXTRA_ARTICLE_NO, Integer.parseInt(dataMap.get(Constants.EXTRA_ARTICLE_NO)));
        }else if(dataMap.get("type").equals(PUSH_TYPE_EVENT)){
            channelId = PUSH_CHANNEL_EVENT;
        }

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(mainIntent);
        if(!dataMap.get("type").equals(PUSH_TYPE_EVENT)){
            stackBuilder.addNextIntent(detailIntent);
        }
        stackBuilder.addParentStack(MainActivity.class);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if(dataMap.get("type").equals(PUSH_TYPE_COMMENT_OF_MATCH) && !sessionManager.isPushChannelCommentOfMatch()){
                // 매치(match/hire/recruit) 게시판의 댓글인 경우
                channelId = PUSH_CHANNEL_COMMENT_OF_MATCH;
                channelName = PUSH_CHANNEL_NAME_COMMENT_OF_MATCH;
                sessionManager.setPushChannelCommentOfMatch(true);
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                nManager.createNotificationChannel(mChannel);
            }else if(dataMap.get("type").equals(PUSH_TYPE_COMMENT_OF_FREE) && !sessionManager.isPushChannelCommentOfCommunity()){
                // 자유게시판의 댓글인 경우
                channelId = PUSH_CHANNEL_COMMENT_OF_COMMUNITY;
                channelName = PUSH_CHANNEL_NAME_COMMENT_OF_COMMUNITY;
                sessionManager.setPushChannelCommentOfCommunity(true);
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                nManager.createNotificationChannel(mChannel);
            }else if(dataMap.get("type").equals(PUSH_CHANNEL_NAME_MY_FAVORITE_ARTICLE_MATCHED) && !sessionManager.isPushChannelMyFavoriteArticleMatched()){
                // 내가 관심 설정한 매치 게시글의 진행 상태가 '완료'로 변경된 경우
                channelId = PUSH_CHANNEL_MY_FAVORITE_ARTICLE_MATCHED;
                channelName = PUSH_CHANNEL_NAME_MY_FAVORITE_ARTICLE_MATCHED;
                sessionManager.setPushChannelMyFavoriteArticleMatched(true);
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                nManager.createNotificationChannel(mChannel);
            }else if(dataMap.get("type").equals(PUSH_TYPE_MATCH_DATE_ALARM) && !sessionManager.isPushChannelMatchDateAlarm()){
                // 원하는 날짜 및 지역 게시글이 등록되면 푸시 알림
                channelId = PUSH_CHANNEL_MATCH_DATE_ALARM;
                channelName = PUSH_CHANNEL_NAME_MATCH_DATE_ALARM;
                sessionManager.setPushMatchDateAlarm(true);
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                nManager.createNotificationChannel(mChannel);
            }else if(dataMap.get("type").equals(PUSH_TYPE_EVENT) && !sessionManager.isPushChannelEvent()){
                // 이벤트 전체 푸시
                channelId = PUSH_CHANNEL_EVENT;
                channelName = PUSH_CHANNEL_NAME_EVENT;
                sessionManager.setPushChannelEvent(true);
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                nManager.createNotificationChannel(mChannel);
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
