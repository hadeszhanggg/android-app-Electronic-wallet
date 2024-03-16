package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Signup extends AppCompatActivity {
    private Spinner spinnerProvince;
    private Button btnSignup, btnTransferToSigninForm;
    private EditText edtName, edtEmail,  edtDofB, edtPass, edtRePass;
    private RadioGroup rdGroupGender;
    private String Gender, Province;
    private List<String> provinceNames = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        apiService = ApiClient.getApiService();
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
                    Gender = "Male";
                } else {
                    Gender = "Female";
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPass.getText().toString().isEmpty() || edtName.getText().toString().isEmpty() || Gender == null || Province == null || edtEmail.getText().toString().isEmpty() || edtDofB.getText().toString().isEmpty() || edtRePass.getText().toString().isEmpty()) {
                    Toast.makeText(Signup.this, "Fill full information, please!", Toast.LENGTH_SHORT).show();
                } else if (!edtPass.getText().toString().equals(edtRePass.getText().toString())) {
                    Toast.makeText(Signup.this, "Password and confirm password must be same!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Signup.this, "Signup account successfully!", Toast.LENGTH_SHORT).show();
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
    }

    private void fetchProvinces() {
        // Gọi API để lấy danh sách tỉnh thành
        Call<JsonObject> call = apiService.getProvinces();
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
