package com.example.electronicwallet.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String getProvinces_URL = "https://vapi.vnappmob.com/api/";

    private static Retrofit getProvinces_retrofit = new Retrofit.Builder()
            .baseUrl( getProvinces_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static ApiService apiGetProvinces= getProvinces_retrofit.create(ApiService.class);

    public static ApiService getProvinces() {
        return apiGetProvinces;
    }
}
