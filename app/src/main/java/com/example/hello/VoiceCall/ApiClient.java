package com.example.hello.VoiceCall;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

 static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            String base_url = "https://api.mesibo.com/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

