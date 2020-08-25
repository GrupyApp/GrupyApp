package com.grupy.grupy.services;


import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MuFirebaseMessagingClient extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    //compulsory methods in order to use notification
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //the parameter remote message is used to send notifications between devices
        super.onMessageReceived(remoteMessage);
    }
}
