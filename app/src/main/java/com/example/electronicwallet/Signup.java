package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import com.example.electronicwallet.network.ApiClient;
import com.example.electronicwallet.network.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.electronicwallet.network.NodeJsApiClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import com.example.electronicwallet.network.NodeJsApiService;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.json.JSONException;
public class Signup extends AppCompatActivity {
    private Spinner spinnerProvince;
    private Button btnSignup, btnTransferToSigninForm;
    private EditText edtName, edtEmail,  edtDofB, edtPass, edtRePass;
    private RadioGroup rdGroupGender;
    private String  Province;
    private Boolean Gender;
    private List<String> provinceNames = new ArrayList<>();
    private ApiService apiGetProvinces;
    private NodeJsApiService apiSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        apiGetProvinces = ApiClient.getProvinces();
        apiSignup=NodeJsApiClient.getNodeJsApiService();
        fetchProvinces();
        addControl();
        addEvent();
    }
    private void addControl() {
        btnSignup = findViewById(R.id.btnSignup);
        btnTransferToSigninForm = findViewById(R.id.btnTransferToSigninForm);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtDofB = findViewById(R.id.edtDofB);
        edtPass = findViewById(R.id.edtPass);
        edtRePass = findViewById(R.id.edtRePass);
        rdGroupGender = findViewById(R.id.grRdGender);
        spinnerProvince = findViewById(R.id.spinnerProvince);
    }
    private void addEvent() {
        rdGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Xử lý sự kiện khi RadioButton được chọn
                if (checkedId == R.id.rdMale) {
                    Gender = true;
                } else {
                    Gender = false;
                }
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra đầy đủ thông tin
                if (edtPass.getText().toString().isEmpty() || edtName.getText().toString().isEmpty() || Gender == null || Province == null || edtEmail.getText().toString().isEmpty() || edtDofB.getText().toString().isEmpty() || edtRePass.getText().toString().isEmpty()) {
                    Toast.makeText(Signup.this, "Fill full information, please!", Toast.LENGTH_SHORT).show();
                } else if (!edtPass.getText().toString().equals(edtRePass.getText().toString())) { // Kiểm tra pass và confirm pass
                    Toast.makeText(Signup.this, "Password and confirm password must be same!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Tạo JSONObject từ các trường dữ liệu
                        org.json.JSONObject jsonParams = new org.json.JSONObject();
                        jsonParams.put("username", edtName.getText().toString());
                        jsonParams.put("password", edtPass.getText().toString());
                        jsonParams.put("email", edtEmail.getText().toString());
                        jsonParams.put("address", Province);
                        jsonParams.put("gender", Gender);
                        jsonParams.put("date_of_birth", edtDofB.getText().toString());

                        // Gửi JSON đến API đăng ký
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonParams.toString());
                        Call<ResponseBody> call = apiSignup.signup(requestBody);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    // Xử lý khi nhận được phản hồi thành công từ server
                                    Toast.makeText(Signup.this, "Signup account successfully!", Toast.LENGTH_LONG).show();
                                } else {
                                    // Xử lý khi nhận được phản hồi không thành công từ server
                                    String errorMessage = "";
                                    try {
                                        org.json.JSONObject errorBody = new org.json.JSONObject(response.errorBody().string());
                                        errorMessage = errorBody.getString("message");
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(Signup.this, "Failed to signup account: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    Log.e("API_CALL", "Unsuccessful response: " + response.code());
                                    Log.e("API_CALL", "Unsuccessful response: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                // Xử lý khi gặp lỗi trong quá trình gọi API
                                Toast.makeText(Signup.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("API_CALL", "Error: " + t.getMessage());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                            Toast.makeText(Signup.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Signup.this, "Choose province, please!", Toast.LENGTH_LONG).show();
            }
        });
        btnTransferToSigninForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(it);
                finish();
                // Quick Notification
                Toast.makeText(getApplicationContext(), "Close signup form", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchProvinces() {
        // Gọi API để lấy danh sách tỉnh thành
        Call<JsonObject> call =apiGetProvinces.getProvinces();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null && jsonObject.has("results")) {
                        JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                        for (JsonElement element : resultsArray) {
                            JsonObject provinceObject = element.getAsJsonObject();
                            if (provinceObject.has("province_name")) {
                                String provinceName = provinceObject.get("province_name").getAsString();
                                provinceNames.add(provinceName);
                            }
                        }
                        updateSpinner();
                    }
                } else {
                    Toast.makeText(Signup.this, "Failed to fetch provinces", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Signup.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
    }
}
