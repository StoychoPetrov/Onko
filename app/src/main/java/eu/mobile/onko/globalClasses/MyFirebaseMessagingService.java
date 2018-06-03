package eu.mobile.onko.globalClasses;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import eu.mobile.onko.R;

/**
 * Created by Stoycho Petrov on 3.6.2018 г..
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if(notification != null){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "onko_channel")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, mBuilder.build());
        }

        Log.d("FIRIBASE MESSAGE", remoteMessage.getNotification().getBody());
    }
}
