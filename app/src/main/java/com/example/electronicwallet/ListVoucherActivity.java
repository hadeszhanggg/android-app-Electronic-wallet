package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.electronicwallet.models.Voucher;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListVoucherActivity extends AppCompatActivity {
    private NodeJsApiService nodeJsApiService;
    private User user;
    private ListView listView;
    private VoucherAdapter voucherAdapter;
    private TextView txtNoVoucher;
    LinearLayout btnBack;
    private Spinner spinnerType;
    private List<Voucher> allVouchers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_voucher);
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
        txtNoVoucher = findViewById(R.id.txtNoVoucher);
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
                onBackPressed();
            }
        });
    }

    private void fetchBills() {
        String authToken = "Bearer " + user.getAccesssToken();

        Call<List<Voucher>> call = nodeJsApiService.getAllVouchers(authToken);
        call.enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.isSuccessful()) {
                    List<Voucher> vouchers = response.body();
                    allVouchers = vouchers;
                    if (vouchers != null && !vouchers.isEmpty()) {
                        listView.setVisibility(View.VISIBLE);
                        txtNoVoucher.setVisibility(View.GONE);
                        voucherAdapter = new VoucherAdapter(ListVoucherActivity.this, R.layout.item_voucher, vouchers);
                        listView.setAdapter(voucherAdapter);
                        updateSpinnerData(vouchers);
                    } else {
                        listView.setVisibility(View.GONE);
                        txtNoVoucher.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("API_CALL", "Failed to fetch bills: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Log.e("API_CALL", "Failed to fetch bills: " + t.getMessage());
            }
        });
    }

    private void updateSpinnerData(List<Voucher> vouchers) {
        List<String> types = new ArrayList<>();
        types.add("All");
        for (Voucher voucher : vouchers) {
            if (!types.contains(voucher.getType())) {
                types.add(voucher.getType());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void filterBillsByType(String type) {
        List<Voucher> filteredVouchers = new ArrayList<>();
        //đặt allBills thành danh sách tất cả bill, xét type để set lại adapter
        if ("All".equals(type)) {
            filteredVouchers = allVouchers;
        } else {
            for (Voucher voucher : allVouchers) {
                if (voucher.getType().equals(type)) {
                    filteredVouchers.add(voucher);
                }
            }
        }
        // Cập nhật lại ListView với danh sách hóa đơn đã lọc
        voucherAdapter = new VoucherAdapter(ListVoucherActivity.this, R.layout.item_voucher, filteredVouchers);
        listView.setAdapter(voucherAdapter);
    }
}