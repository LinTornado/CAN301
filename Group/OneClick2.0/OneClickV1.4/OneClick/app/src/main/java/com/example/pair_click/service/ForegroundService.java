package com.example.pair_click.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.LogUtils;
import com.example.pair_click.R;


public class ForegroundService extends Service {

    private static final int SERVICE_ID = 10086;
    private static final String CHANNEL_ID = "channel";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("start Service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("destroy Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "OneClick is now starting background services, turn off OneClick's notification permission in the system setting if you wish to see no more this message", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel); //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentText("保活");
        Notification notification = builder.build();
        startForeground(SERVICE_ID, notification);
        return START_STICKY;
    }
}
