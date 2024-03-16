package com.example.electronicwallet.network;
import retrofit2.Call;

import com.google.gson.JsonObject;

import retrofit2.http.GET;

public interface ApiService {
    @GET("province/")
    Call<JsonObject> getProvinces();
}
