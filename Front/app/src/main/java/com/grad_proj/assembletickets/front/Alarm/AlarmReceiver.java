package com.grad_proj.assembletickets.front.Alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, HomeActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"default");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            String channelName="event 알람 채널";
            String description = "설정된 시간에 알람을 울립니다";
            int important = NotificationManager.IMPORTANCE_HIGH; //소리와 알림 메세지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default",channelName,important);
            channel.setDescription(description);

            if(notificationManager!=null){
                //notification channel을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        }
        else{
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("{Time to watch~~")
                .setContentTitle("상태바 드래그시 보이는 타이틀")
                .setContentText("상태바 드래그시 보이는 서브 타이틀")
                .setContentInfo("INFO")
                .setContentIntent(pendingIntent);

        if(notificationManager!=null){
            //notification 작동
            notificationManager.notify(1234,builder.build());
        }

    }
}
