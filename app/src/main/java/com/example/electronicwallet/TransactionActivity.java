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
import com.example.electronicwallet.models.Transaction;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {
    private NodeJsApiService nodeJsApiService;
    private User user;
    private Wallet wallet;
    private ListView listView;
    private TransactionAdapter tranAdapter;
    private TextView txtNoTrans;
    LinearLayout btnBack,layoutAc;
    private Spinner spinnerType;
    private List<Transaction> allTransactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            user = (User) intent.getSerializableExtra("User");
            wallet = (Wallet) intent.getSerializableExtra("Wallet");
        } else {
            Log.e("ERROR", "No user object passed to ListBillActivity");
            finish();
        }
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        fetchTrans();
        addControl();
        addEvent();
    }

    private void addControl() {
        listView = findViewById(R.id.listView);
        txtNoTrans = findViewById(R.id.txtNoBill);
        spinnerType = findViewById(R.id.spinnerType);
        btnBack=findViewById(R.id.btnBack);
        layoutAc= findViewById(R.id.layoutAc);
    }
    private void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction selectedTran = allTransactions.get(position);
                //showBillDetailFragment(selectedBill, user, wallet);
            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedType = spinnerType.getSelectedItem().toString();
               filterTransactionsByType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void fetchTrans() {
        String authToken = "Bearer " + user.getAccesssToken();

        Call<List<Transaction>> call = nodeJsApiService.getTransactions(authToken);
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    List<Transaction> transactions = response.body();
                    allTransactions = transactions;
                    if (transactions != null && !transactions.isEmpty()) {
                        listView.setVisibility(View.VISIBLE);
                        txtNoTrans.setVisibility(View.GONE);
                        tranAdapter = new TransactionAdapter(TransactionActivity.this, R.layout.item_transaction, transactions);
                        listView.setAdapter(tranAdapter);
                        // Cập nhật dữ liệu cho Combobox
                        updateSpinnerData(transactions);
                    } else {
                        listView.setVisibility(View.GONE);
                        txtNoTrans.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("API_CALL", "Failed to fetch bills: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.e("API_CALL", "Failed to fetch Transactions: " + t.getMessage());
            }
        });
    }
    private void updateSpinnerData(List<Transaction> transactions) {
        List<String> types = new ArrayList<>();
        types.add("All");
        for (Transaction transaction : transactions) {
            if (!types.contains(transaction.getType())&&transaction.getType()==1) {
                types.add("Deposit");
            }else if(!types.contains(transaction.getType())&&transaction.getType()==2) {
                types.add("Transfer");
            }else types.add("Pay");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }
    private void filterTransactionsByType(String type) {
        List<Transaction> filteredTransaction = new ArrayList<>();
        int typeId;
        if(type.equals("Deposit"))
            typeId=1;
        else if(type.equals("Transfer"))
            typeId=2;
        else typeId=3;
        //đặt allBills thành danh sách tất cả bill, xét type để set lại adapter
        if ("All".equals(type)) {
            filteredTransaction = allTransactions;
        } else {
            for (Transaction transaction : allTransactions) {
                if (transaction.getType()==typeId) {
                    filteredTransaction.add(transaction);
                }
            }
        }
        // Cập nhật lại ListView với danh sách hóa đơn đã lọc
        tranAdapter = new TransactionAdapter(TransactionActivity.this, R.layout.item_transaction, filteredTransaction);
        listView.setAdapter(tranAdapter);
    }
}