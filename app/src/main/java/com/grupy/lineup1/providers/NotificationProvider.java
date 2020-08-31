package com.grupy.lineup1.providers;

import com.grupy.lineup1.models.FCMBody;
import com.grupy.lineup1.models.FCMResponse;
import com.grupy.lineup1.retrofit.IFCMApi;
import com.grupy.lineup1.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {

    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
