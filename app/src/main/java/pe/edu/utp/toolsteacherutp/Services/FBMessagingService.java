package pe.edu.utp.toolsteacherutp.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pe.edu.utp.toolsteacherutp.Activities.MainActivity;
import pe.edu.utp.toolsteacherutp.R;

/**
 * Created by elbuenpixel on 12/03/17.
 */

public class FBMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
    // [END receive_message]

    private void sendNotification(String titleNotification, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.utp)
                .setContentTitle(titleNotification)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isVibrate = preference.getBoolean("notifications_new_message_vibrate", false);
        boolean isNoticate = preference.getBoolean("notifications_new_message", false);
        if ( isNoticate ){
            String strRingtonePreference = preference.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
            notificationBuilder.setSound( Uri.parse(strRingtonePreference) );
            if ( isVibrate ){
                notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            }
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }

    }
}
