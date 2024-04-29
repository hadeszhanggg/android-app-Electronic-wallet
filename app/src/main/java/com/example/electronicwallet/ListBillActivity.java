package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
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
    private TextView txtNoBill;
    LinearLayout btnBack;
    private Spinner spinnerType;
    private List<Bill> allBills;

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

        addControl();
        addEvent();
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        fetchBills();
    }

    private void addControl() {
        listView = findViewById(R.id.listView);
        txtNoBill = findViewById(R.id.txtNoBill);
        spinnerType = findViewById(R.id.spinnerType);
        btnBack=findViewById(R.id.btnBack);
    }

    private void addEvent() {
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = spinnerType.getSelectedItem().toString();
                filterBillsByType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchBills() {
        String authToken = "Bearer " + user.getAccesssToken();

        Call<List<Bill>> call = nodeJsApiService.getAllBills(authToken);
        call.enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful()) {
                    List<Bill> bills = response.body();
                    allBills = bills; // Lưu trữ danh sách hóa đơn gốc
                    if (bills != null && !bills.isEmpty()) {
                        listView.setVisibility(View.VISIBLE);
                        txtNoBill.setVisibility(View.GONE);
                        billAdapter = new BillAdapter(ListBillActivity.this, R.layout.item_bill, bills);
                        listView.setAdapter(billAdapter);

                        // Cập nhật dữ liệu cho Combobox
                        updateSpinnerData(bills);
                    } else {
                        listView.setVisibility(View.GONE);
                        txtNoBill.setVisibility(View.VISIBLE);
                    }
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

    private void updateSpinnerData(List<Bill> bills) {
        List<String> types = new ArrayList<>();
        types.add("All");
        for (Bill bill : bills) {
            if (!types.contains(bill.getType())) {
                types.add(bill.getType());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void filterBillsByType(String type) {
        List<Bill> filteredBills = new ArrayList<>();
        if ("All".equals(type)) {
            filteredBills = allBills; // Nếu chọn All, hiển thị tất cả hóa đơn
        } else {
            for (Bill bill : allBills) {
                if (bill.getType().equals(type)) {
                    filteredBills.add(bill); // Lọc các hóa đơn theo loại được chọn
                }
            }
        }
        // Cập nhật lại ListView với danh sách hóa đơn đã lọc
        billAdapter = new BillAdapter(ListBillActivity.this, R.layout.item_bill, filteredBills);
        listView.setAdapter(billAdapter);
    }
}