package com.grupy.lineup1.retrofit;

import com.grupy.lineup1.models.FCMBody;
import com.grupy.lineup1.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {


    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAdvGiK88:APA91bFw152Yw1OXVSxa156bDtGRMFmcsQt7xLqHDZvM3wy2hZS0zxsTjeAG4IV49mOfs72NBBQv0YsLm31nRJfln0Av-5IGWV6bpQkJbLhZ-MUGFojKljTnnrJTfQkj1qNRbitlS6nl"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
