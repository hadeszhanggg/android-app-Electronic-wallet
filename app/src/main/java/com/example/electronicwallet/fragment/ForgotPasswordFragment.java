package com.example.electronicwallet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.MainActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.Signup;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.network.ApiClient;
import com.example.electronicwallet.network.ApiService;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {
    private Spinner spinnerProvince;
    private Button btnConfirm, btnTransferToSigninForm;
    private com.google.android.material.textfield.TextInputEditText edtName, edtEmail,  edtDofB, edtPass, edtRePass;
    private RadioGroup rdGroupGender;
    private Boolean Gender;
    private List<String> provinceNames = new ArrayList<>();
    private ApiService apiGetProvinces;
    private String  Province;
    private NodeJsApiService apiForgotPass;
    public ForgotPasswordFragment() {

    }


    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        apiGetProvinces = ApiClient.getProvinces();
        apiForgotPass=NodeJsApiClient.getNodeJsApiService();
        addControl(view);
        fetchProvinces();
        addEvent();
        return view;
    }
    private void addControl(View view) {
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnTransferToSigninForm = view.findViewById(R.id.btnTransferToSigninForm);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtDofB = view.findViewById(R.id.edtDofB);
        edtPass = view.findViewById(R.id.edtPass);
        edtRePass = view.findViewById(R.id.edtRePass);
        rdGroupGender = view.findViewById(R.id.grRdGender);
        spinnerProvince = view.findViewById(R.id.spinnerProvince);
    }
    private void addEvent(){
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
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPass.getText().equals(edtRePass.getText())){
                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("username", edtName.getText());
                        requestBody.put("password", edtPass.getText());
                        requestBody.put("address",Province);
                        requestBody.put("email",edtEmail.getText());
                        requestBody.put("gender",Gender);
                        requestBody.put("date_of_birth",edtDofB.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().forgotPassword(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Update new password successfully!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                String errorMessage = "";
                                try {
                                    JSONObject errorBody = new JSONObject(response.errorBody().string());
                                    errorMessage = errorBody.getString("message");
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                Log.e("API_CALL", "Unsuccessful response: " + response.code());
                                Log.e("API_CALL", "Unsuccessful response: " + response.message());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("Edit personal information", "Failed when Update new password!: " + t.getMessage());                }
                    });
                }else  Toast.makeText(getContext(), "Your Repass must like pass!!!", Toast.LENGTH_LONG).show();
            }
        });
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Choose province, please!", Toast.LENGTH_LONG).show();
            }
        });
        btnTransferToSigninForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
    }
    private void closeFragment() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onFragmentClosed();
        }
        getActivity().getSupportFragmentManager().popBackStack();
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
                    Toast.makeText(getContext(), "Failed to fetch provinces", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
    }
}