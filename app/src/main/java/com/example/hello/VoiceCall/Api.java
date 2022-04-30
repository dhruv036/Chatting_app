package com.example.hello.VoiceCall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("backend?")
    Call<Result> getUser(
            @Query("token") String token,
            @Query("op") String operation,
            @Query("appid") String appid,
            @Query("addr") String userid
    );
}
