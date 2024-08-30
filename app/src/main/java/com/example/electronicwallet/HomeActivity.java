package com.example.electronicwallet;

import static androidx.fragment.app.FragmentStatePagerAdapter.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.electronicwallet.fragment.ViewPagerAdapter;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.electronicwallet.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private  static  final int MY_REQUEST_CODE=12;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private User user;
    private Wallet wallet;
    private NodeJsApiService apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiClient = NodeJsApiClient.getNodeJsApiService();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        wallet=(Wallet) intent.getSerializableExtra("Wallet");
        addControl();
        FirebaseApp.initializeApp(this);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), user, wallet);
        viewPager.setAdapter(adapter);
        addEvent();
        //Initialize firebase
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    sendTokenToServer(token);

                    Log.d("FCM", "Token: " + token);
                });
    }
    private void sendTokenToServer(String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                jsonObject.toString()
        );
        String authToken = "Bearer " + user.getAccesssToken();
        Call<ResponseBody> call = apiClient.sendTokenToServer(requestBody, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Token sent to server successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to send token to server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void addControl(){
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    protected void addEvent() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_personal).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    viewPager.setCurrentItem(0);
                    return true;
                }else if (itemId == R.id.navigation_chat) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_personal) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}