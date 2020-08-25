package com.grupy.grupy.services;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.grupy.grupy.channel.NotificationHelper;

import java.util.Map;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    //compulsory methods in order to use notification

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //the parameter remoteMessage is used to send notifications between devices
        super.onMessageReceived(remoteMessage);
        //we recive the information send by the notification
        Map<String , String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        if (title != null) {
            showNotification(title, body);
        }

    }

    //use notificationHelper
    private void showNotification(String title, String body) {
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title,body);
        notificationHelper.getManager().notify(1, builder.build());
    }
}
