package com.grad_proj.assembletickets.front.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.grad_proj.assembletickets.front.R;

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis()).setShowWhen(true)
                    .setTicker("공연 알림을 확인하세요")
                    .setContentTitle("Show Title")
                    .setContentText("티켓팅 / 공연 관람 시간이 곧 시작됩니다.")
                    .build();

            startForeground(1, notification);
        }
    }
}
