package com.example.electronicwallet.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import com.example.electronicwallet.network.Constants;
public class NodeJsApiClient {
    private static final String BASE_URL = Constants.SERVER_URL;
    private static final int CONNECT_TIMEOUT_SECONDS = 30; // Thiết lập thời gian chờ kết nối (giây)
    private static final int READ_TIMEOUT_SECONDS = 30; // Thiết lập thời gian chờ phản hồi (giây)
    private static final int WRITE_TIMEOUT_SECONDS = 30; // Thiết lập thời gian chờ ghi dữ liệu (giây)

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
