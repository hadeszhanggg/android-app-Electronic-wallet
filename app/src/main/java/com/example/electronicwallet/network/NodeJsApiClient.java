package com.example.electronicwallet.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import com.example.electronicwallet.network.Constants;
public class NodeJsApiClient {
    private static final String BASE_URL = "http://192.168.11.16:333/";
    private static final int CONNECT_TIMEOUT_SECONDS = 30;
    private static final int READ_TIMEOUT_SECONDS = 30;
    private static final int WRITE_TIMEOUT_SECONDS = 30;

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static NodeJsApiService apiService = retrofit.create(NodeJsApiService.class);

    public static NodeJsApiService getNodeJsApiService() {
        return apiService;
    }
}
