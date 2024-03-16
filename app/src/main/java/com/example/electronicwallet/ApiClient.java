package com.example.electronicwallet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.11.16:333/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static ApiService apiService = retrofit.create(ApiService.class);

    public static ApiService getApiService() {
        return apiService;
    }
}
