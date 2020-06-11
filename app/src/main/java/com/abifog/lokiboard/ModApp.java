package com.abifog.lokiboard;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class ModApp extends Application {

    private static Context mContext;
    public static final String CHANNEL_ID = "keyboardServiceChannel";
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        createNotificationChannel();

    }

    public static Context getAppContext() {
        return mContext;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alpha Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
