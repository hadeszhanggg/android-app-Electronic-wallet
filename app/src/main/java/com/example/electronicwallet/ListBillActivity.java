package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.electronicwallet.fragment.PayFragment;
import com.example.electronicwallet.fragment.RegisterPassbookFragment;
import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import com.example.electronicwallet.Interface.PassbookRegisteredListener;
public class ListBillActivity extends AppCompatActivity implements PassbookRegisteredListener{
    private NodeJsApiService nodeJsApiService;
    private User user;
    private ListView listView;
    private BillAdapter billAdapter;
    private TextView txtNoBill;
    LinearLayout btnBack,layoutAc;
    private Spinner spinnerType;
    private List<Bill> allBills;
private Wallet wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);

        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            user = (User) intent.getSerializableExtra("User");
            wallet = (Wallet) intent.getSerializableExtra("Wallet");
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
        layoutAc= findViewById(R.id.layoutAc);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill selectedBill = allBills.get(position);
                showBillDetailFragment(selectedBill, user, wallet);
            }
        });
    }
    @Override
    public void passbookRegistered(Wallet updatedWallet) {
        this.wallet = updatedWallet;
    }
    private void showBillDetailFragment(Bill bill, User user, Wallet wallet) {
        PayFragment fragment = PayFragment.newInstance(bill, user, wallet,this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
       layoutAc.setAlpha(0.7f);
    }

    public void onFragmentClosed() {
        layoutAc.setAlpha(1.0f);
    }
    private void fetchBills() {
        String authToken = "Bearer " + user.getAccesssToken();

        Call<List<Bill>> call = nodeJsApiService.getAllBills(authToken);
        call.enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful()) {
                    List<Bill> bills = response.body();
                    allBills = bills;
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
        //đặt allBills thành danh sách tất cả bill, xét type để set lại adapter
        if ("All".equals(type)) {
            filteredBills = allBills;
        } else {
            for (Bill bill : allBills) {
                if (bill.getType().equals(type)) {
                    filteredBills.add(bill);
                }
            }
        }
        // Cập nhật lại ListView với danh sách hóa đơn đã lọc
        billAdapter = new BillAdapter(ListBillActivity.this, R.layout.item_bill, filteredBills);
        listView.setAdapter(billAdapter);
    }
    // Phương thức để xóa bill khỏi danh sách
    public void removeBillFromList(Bill billToRemove) {
        allBills.remove(billToRemove);
        billAdapter.notifyDataSetChanged();
    }
}