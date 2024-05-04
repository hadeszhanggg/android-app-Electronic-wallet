package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.electronicwallet.fragment.RegisterPassbookFragment;
import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestActivity extends AppCompatActivity {
    LinearLayout btnBack;
    private NodeJsApiService nodeJsApiService;
    private PassbookAdapter passbookAdapter;
    private GridView passbookGridView;
    List<Passbook> passbooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        addControl();
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        fetchPassbooks();
        addEvent();
    }
    protected void addControl(){
        passbookGridView = findViewById(R.id.passbookGridView);
        btnBack=findViewById(R.id.btnBack);
    }
    protected void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
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
    //Show fragment function
    private void showPassbookDetailFragment(Passbook passbook) {
        RegisterPassbookFragment fragment = new RegisterPassbookFragment();
        fragment.setPassbook(passbook);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.layoutContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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