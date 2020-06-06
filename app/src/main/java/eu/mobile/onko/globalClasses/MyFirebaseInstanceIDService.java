package eu.mobile.onko.globalClasses;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import eu.mobile.onko.R;

/**
 * Created by Stoycho Petrov on 3.6.2018 г..
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
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

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString(Utils.PREFERENCES_PUSH_TOKEN, token).apply();
    }
}
