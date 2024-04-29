package com.example.electronicwallet.network;

import com.example.electronicwallet.models.Voucher;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.example.electronicwallet.models.Bill;

import java.util.List;

public interface NodeJsApiService {
    @POST("auth/signup")
    Call<ResponseBody> signup(@Body RequestBody requestBody);
    @POST("auth/signin")
    Call<ResponseBody> signin(@Body RequestBody requestBody);
    @GET("users/getAllBills")
    Call<List<Bill>> getAllBills(@Header("Authorization") String authToken);
    @GET("users/getAllVouchers")
    Call<List<Voucher>> getAllVouchers(@Header("Authorization") String authToken);
}
