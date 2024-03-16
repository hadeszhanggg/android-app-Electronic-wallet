package com.example.electronicwallet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import com.example.electronicwallet.models.User;

public interface ApiService {
    @POST("auth/signup")
    Call<ResponseBody> signUp(@Body User user);
}
