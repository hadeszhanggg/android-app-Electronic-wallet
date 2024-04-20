package com.example.electronicwallet;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;
import java.io.IOException;
import com.example.electronicwallet.network.NodeJsApiService;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.models.User;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private Button btnSignin, btnTransferToSignupForm;
    private EditText edtName, edtPass;
    private NodeJsApiService apiSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiSignin=NodeJsApiClient.getNodeJsApiService();
        addControl();
        addEvent();
    }
private void addControl()
{
    btnSignin=findViewById(R.id.btnSignin);
    btnTransferToSignupForm=findViewById(R.id.btnTransferToSignupForm);
    edtName=findViewById(R.id.edtName);
    edtPass=findViewById(R.id.edtPass);
}
private void addEvent()
{
    btnTransferToSignupForm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getApplicationContext(), Signup.class);
            startActivity(it);
            // Quick Notification
            Toast.makeText(getApplicationContext(), "Close signin form", Toast.LENGTH_SHORT).show();
        }
    });

    btnSignin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtName.getText().toString().isEmpty()||edtPass.getText().toString().isEmpty())
                Toast.makeText(getApplicationContext(), "Fill full information for login, please!", Toast.LENGTH_LONG).show();
            else{
                try{
                    org.json.JSONObject jsonParams = new org.json.JSONObject();
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
                                    boolean gender = jsonResponse.getBoolean("gender");
                                    String dateOfBirth = jsonResponse.getString("date_of_birth");
                                    // Lưu thông tin người dùng vào đối tượng User
                                    User user = new User();
                                    user.setId(id);
                                    user.setUsername(username);
                                    user.setEmail(email);
                                    user.setAddress(address);
                                    user.setGender(gender);
                                    user.setDateOfBirth(dateOfBirth);
                                    // Lưu accessToken và refreshToken vào đối tượng User
                                    user.setAccessToken(accessToken);
                                    user.setRefreshToken(refreshToken);
                                    Toast.makeText(MainActivity.this, "Signin successfully!", Toast.LENGTH_SHORT).show();
                                    sendDataToNextActivity(user);
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Xử lý khi nhận được phản hồi không thành công từ server
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
                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
}
    private void sendDataToNextActivity(User user) {
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, chat.class);
            intent.putExtra("User", user);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "User object is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
