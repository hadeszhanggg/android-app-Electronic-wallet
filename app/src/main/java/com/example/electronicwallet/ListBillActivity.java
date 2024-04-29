package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
public class ListBillActivity extends AppCompatActivity {
    private NodeJsApiService nodeJsApiService;
    private User user;
    private ListView listView;
    private BillAdapter billAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            user = (User) intent.getSerializableExtra("User");
        } else {
            Log.e("ERROR", "No user object passed to ListBillActivity");
            finish();
        }

        listView = findViewById(R.id.listView);
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        fetchBills();
    }
    private void fetchBills() {
        String authToken = "Bearer " + user.getAccesssToken();

        Call<List<Bill>> call = nodeJsApiService.getAllBills(authToken);
        call.enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful()) {
                    List<Bill> bills = response.body();
                    // Khởi tạo Adapter và gán nó cho ListView
                    billAdapter = new BillAdapter(ListBillActivity.this, R.layout.item_bill, bills);
                    listView.setAdapter(billAdapter);
                } else {
                    Log.e("API_CALL", "Failed to fetch bills: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Log.e("API_CALL", "Failed to fetch bills: " + t.getMessage());
            }
        });
    }
}