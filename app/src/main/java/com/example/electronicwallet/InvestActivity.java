package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.electronicwallet.fragment.RegisterPassbookFragment;
import com.example.electronicwallet.models.DataModel;
import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class InvestActivity extends AppCompatActivity {
    private DataModel dataViewModel;
    LinearLayout btnBack,layoutHeader;
    private NodeJsApiService nodeJsApiService;
    private PassbookAdapter passbookAdapter;
    private GridView passbookGridView;
    List<Passbook> passbooks;
    private View contentView;
    private User user;
    private Wallet wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        Intent intent = getIntent();
        dataViewModel = new ViewModelProvider(this).get(DataModel.class);
        addControl();
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        fetchPassbooks();
        addEvent();
    }
    protected void addControl(){
        passbookGridView = findViewById(R.id.passbookGridView);
        btnBack=findViewById(R.id.btnBack);
        layoutHeader=findViewById(R.id.layoutHeader);
        contentView = findViewById(android.R.id.content);
    }
    protected void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        passbookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Passbook selectedPassbook = passbooks.get(position);
                showPassbookDetailFragment(selectedPassbook);
            }
        });
    }
    private void showPassbookDetailFragment(Passbook passbook) {
        RegisterPassbookFragment fragment = RegisterPassbookFragment.newInstance(passbook);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        findViewById(R.id.layoutHeader).setAlpha(0.7f);
        findViewById(R.id.passbookGridView).setAlpha(0.7f);
    }

    public void onFragmentClosed() {
        // Set lại độ trong suốt của các view
        findViewById(R.id.layoutHeader).setAlpha(1.0f);
        findViewById(R.id.passbookGridView).setAlpha(1.0f);
    }
    private void fetchPassbooks() {
        Call<List<Passbook>> call = nodeJsApiService.getAllPassbook();
        call.enqueue(new Callback<List<Passbook>>() {
            @Override
            public void onResponse(Call<List<Passbook>> call, Response<List<Passbook>> response) {
                if (response.isSuccessful()) {
                    passbooks = response.body();
                    if (passbooks != null && !passbooks.isEmpty()) {
                        passbookAdapter = new PassbookAdapter(InvestActivity.this, R.layout.item_passbook, passbooks);
                        passbookGridView.setAdapter(passbookAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Passbook>> call, Throwable t) {
                Log.e("FetchPassbooks", "Failed to fetch passbooks: " + t.getMessage());
            }
        });
    }
}