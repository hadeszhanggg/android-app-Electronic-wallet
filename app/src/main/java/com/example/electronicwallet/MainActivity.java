package com.example.electronicwallet;

import com.example.electronicwallet.fragment.ForgotPasswordFragment;
import com.example.electronicwallet.fragment.RegisterPassbookFragment;
import com.example.electronicwallet.models.Passbook;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.electronicwallet.network.NodeJsApiService;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.controllers.locate;
import com.example.electronicwallet.models.Wallet;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private Button btnSignin, btnTransferToSignupForm;
    private NodeJsApiService apiSignin;
    private LinearLayout layoutIn;
    private locate locate;
    private com.google.android.material.textfield.TextInputEditText edtName,edtPass;
    private com.google.android.material.textview.MaterialTextView txtForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiSignin = NodeJsApiClient.getNodeJsApiService();
        locate = new locate(this);
        Location location = locate.getCurrentLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("Latitude: ", String.valueOf(latitude));
            Log.d("Longittude: ", String.valueOf(longitude));
        } else {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
        }
        addControl();
        addEvent();

    }

    private void addControl() {
        btnSignin = findViewById(R.id.btnSignin);
        btnTransferToSignupForm = findViewById(R.id.btnTransferToSignupForm);
        edtPass = findViewById(R.id.edtPass);
        edtName = findViewById(R.id.edtName);
        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        layoutIn=findViewById(R.id.layoutIn);
    }

    private void addEvent() {
        btnTransferToSignupForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Signup.class);
                startActivity(it);
                Toast.makeText(getApplicationContext(), "Close signin form", Toast.LENGTH_SHORT).show();
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordFragment();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill full information for login, please!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("username", edtName.getText().toString());
                        jsonParams.put("password", edtPass.getText().toString());
                        // Gửi JSON đến API đăng nhập
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonParams.toString());
                        Call<ResponseBody> call = apiSignin.signin(requestBody);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        // Sử dụng JSONObject từ org.json
                                        JSONObject jsonResponse = new JSONObject(response.body().string());
                                        String accessToken = jsonResponse.getString("accessToken");
                                        String refreshToken = jsonResponse.getString("refreshToken");
                                        String id = jsonResponse.getString("id");
                                        String username = jsonResponse.getString("username");
                                        String email = jsonResponse.getString("email");
                                        String address = jsonResponse.getString("address");
                                        String avatar = jsonResponse.getString("avatar");
                                        boolean gender = jsonResponse.getBoolean("gender");
                                        String dateOfBirth = jsonResponse.getString("date_of_birth");
                                        String wallet_id = jsonResponse.getString("wallet_id");
                                        String prestige_score = jsonResponse.getString("prestige_score");
                                        String account_balance = jsonResponse.getString("account_balance");
                                        // Lưu thông tin người dùng vào đối tượng User
                                        User user = new User();
                                        Wallet wallet = new Wallet();
                                        user.setId(id);
                                        user.setUsername(username);
                                        user.setEmail(email);
                                        user.setAddress(address);
                                        user.setGender(gender);
                                        user.setDateOfBirth(dateOfBirth);
                                        user.setAvatar(avatar);
                                        // Lưu accessToken và refreshToken vào đối tượng User
                                        user.setAccessToken(accessToken);
                                        user.setRefreshToken(refreshToken);
                                        user.setPassword(edtPass.getText().toString());
                                        wallet.setId(wallet_id);
                                        wallet.setPrestige_score(Integer.parseInt(prestige_score));
                                        wallet.setAccount_balance(Double.parseDouble(account_balance));
                                        Toast.makeText(MainActivity.this, "Signin successfully!", Toast.LENGTH_SHORT).show();
                                        sendDataToNextActivity(user, wallet);
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String errorMessage = "";
                                    try {
                                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                                        errorMessage = errorBody.getString("message");
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(MainActivity.this, "Failed to signin: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    Log.e("API_CALL", "Unsuccessful response: " + response.code());
                                    Log.e("API_CALL", "Unsuccessful response: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                // Xử lý khi gặp lỗi trong quá trình gọi API
                                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("API_CALL", "Error: " + t.getMessage());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void showForgotPasswordFragment() {
        ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        layoutIn.setAlpha(0.7f);
    }
    public void onFragmentClosed() {
        layoutIn.setAlpha(1.0f);
    }
    private void sendDataToNextActivity(User user, Wallet wallet) {
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("User", user);
            intent.putExtra("Wallet", wallet);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "User object is null", Toast.LENGTH_SHORT).show();
        }
    }
}
