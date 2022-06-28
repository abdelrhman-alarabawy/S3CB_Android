package com.example.ssscb_android;

import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ScalarService {
    @GET()
    retrofit2.Call<String> getStringResponse(@Url String url);
}