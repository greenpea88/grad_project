package com.grad_proj.assembletickets.front.Alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static String CHANNEL_ID = "default";
    private static String CHANNEL_NAME = "EventNotification";

    NotificationManager notificationManager;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
                builder = new NotificationCompat.Builder(this.context, CHANNEL_ID);
            }
        } else {
            builder = new NotificationCompat.Builder(this.context);
        }

        Intent notificationIntent = new Intent(this.context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis()).setShowWhen(true)
                .setTicker("공연 알림을 확인하세요")
                .setContentTitle("Show Title")
                .setContentText("티켓팅 / 공연 관람 시간이 곧 시작됩니다.")
                .setContentIntent(pendingIntent);

        if(notificationManager!=null){
            //notification 작동
            notificationManager.notify(1234, builder.build());
        }

    }
}
