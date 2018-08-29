package firebase;

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
import com.groundmobile.ground.MainActivity;
import com.groundmobile.ground.R;

import java.util.Map;

import view.DetailArticleActivity;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private String[] areaNameArray;
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
        Map<String, String> pushDataMap = remoteMessage.getData();
        sendNotification(pushDataMap);
    }

    private void sendNotification(Map<String, String> dataMap) {
        /*
        Intent intent;
        if(dataMap.get("type").equals("comment")){
            intent = new Intent(this, DetailArticleActivity.class);
            intent.putExtra("area", areaNameArray[Integer.parseInt(dataMap.get("areaNo"))]);
            intent.putExtra("areaNo", Integer.parseInt(dataMap.get("areaNo")));
            intent.putExtra("hasArticleModel", false);
            intent.putExtra("boardType", dataMap.get("boardType"));
            intent.putExtra("articleNo", Integer.parseInt(dataMap.get("articleNo")));
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        */

        Intent detailIntent = new Intent(this, DetailArticleActivity.class);
        if(dataMap.get("type").equals("comment")){
            detailIntent.putExtra("area", areaNameArray[Integer.parseInt(dataMap.get("areaNo"))]);
            detailIntent.putExtra("areaNo", Integer.parseInt(dataMap.get("areaNo")));
            detailIntent.putExtra("hasArticleModel", false);
            detailIntent.putExtra("boardType", dataMap.get("boardType"));
            detailIntent.putExtra("articleNo", Integer.parseInt(dataMap.get("articleNo")));
        }
        Intent mainIntent = new Intent(this, MainActivity.class);
        //detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mainIntent);
        stackBuilder.addNextIntent(detailIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ground_app_icon_72)
                .setContentTitle(dataMap.get("title"))
                .setContentText(dataMap.get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.WHITE, 1500, 1500)
                .setContentIntent(resultPendingIntent);

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0 /* ID of notification */, nBuilder.build());
    }
}
