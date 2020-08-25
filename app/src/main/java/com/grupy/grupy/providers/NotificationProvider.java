package com.grupy.grupy.providers;

import com.grupy.grupy.models.FCMBody;
import com.grupy.grupy.models.FCMResponse;
import com.grupy.grupy.retrofit.IFCMApi;
import com.grupy.grupy.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {

    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
