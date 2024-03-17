package com.example.electronicwallet.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface NodeJsApiService {
    @POST("auth/signup")
    Call<ResponseBody> signup(@Body RequestBody requestBody);
}
