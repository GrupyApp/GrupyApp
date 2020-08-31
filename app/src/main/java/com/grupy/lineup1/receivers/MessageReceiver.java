package com.grupy.lineup1.receivers;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.grupy.lineup1.models.FCMBody;
import com.grupy.lineup1.models.FCMResponse;
import com.grupy.lineup1.models.Message;
import com.grupy.lineup1.providers.MessageProvider;
import com.grupy.lineup1.providers.NotificationProvider;
import com.grupy.lineup1.providers.TokenProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupy.lineup1.services.MyFirebaseMessagingClient.NOTIFICATION_REPLY;

public class MessageReceiver extends BroadcastReceiver {

    String mExtraIdSender;
    String mExtraIdReceiver;
    String mExtraIdChat;
    String mExtraUsernameSender;
    String mExtraUsernameReceiver;
    String mExtraImageSender;
    String mExtraImageReceiver;
    int mExtraIdNotification;

    TokenProvider mTokenProvider;
    NotificationProvider mNotificationProvider;

    @Override
    public void onReceive(Context context, Intent intent) {

        mTokenProvider = new TokenProvider();
        mNotificationProvider = new NotificationProvider();

        mExtraIdSender = intent.getExtras().getString("idSender");
        mExtraIdReceiver = intent.getExtras().getString("idReceiver");
        mExtraIdChat = intent.getExtras().getString("idChat");
        mExtraIdNotification = intent.getExtras().getInt("idNotification");
        mExtraUsernameSender = intent.getExtras().getString("usernameSender");
        mExtraUsernameReceiver = intent.getExtras().getString("usernameReceiver");
        mExtraImageSender = intent.getExtras().getString("imageSender");
        mExtraImageReceiver = intent.getExtras().getString("imageReceiver");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mExtraIdNotification);

        String message = getMessageText(intent).toString();

        sendMessage(message);
    }

    private void sendMessage(String messageText) {
        final Message message = new Message();
        message.setIdChat(mExtraIdChat);
        message.setIdSender(mExtraIdReceiver);   //Now we are the sender
        message.setIdReceiver(mExtraIdSender);
        message.setTimestamp(new Date().getTime());
        message.setViewed(false);
        message.setIdChat(mExtraIdChat);
        message.setMessage(messageText);

        MessageProvider messageProvider = new MessageProvider();
        messageProvider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    getToken(message);
                }
            }
        });
    }

    private void getToken(final Message message) {
        mTokenProvider.getToken(mExtraIdSender).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("token")) {
                        String token = documentSnapshot.getString("token");
                        Gson gson = new Gson();
                        ArrayList<Message> messagesArray = new ArrayList<>();
                        messagesArray.add(message);
                        String messages = gson.toJson(messagesArray);
                        sendNotification(token, messages, message);
                    }
                }
            }
        });
    }

    private void sendNotification(final String token, String messages, Message message) {
        final Map<String, String> data = new HashMap<>();
        data.put("title", "NUEVO MENSAJE");
        data.put("body", message.getMessage());
        data.put("idNotification", String.valueOf(mExtraIdNotification));
        data.put("messages", messages);
        data.put("usernameSender", mExtraUsernameReceiver.toUpperCase());
        data.put("usernameReceiver", mExtraUsernameSender.toUpperCase());
        data.put("idSender", message.getIdSender());
        data.put("idReceiver", message.getIdReceiver());
        data.put("idChat", message.getIdChat());

        data.put("imageSender", mExtraIdReceiver);
        data.put("imageReceiver", mExtraIdSender);

        FCMBody body = new FCMBody(token, "high", "4500s", data);
        mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Log.e("1122", "The error was: " + t.getMessage());
            }
        });
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NOTIFICATION_REPLY);
        }
        return null;
    }
}
