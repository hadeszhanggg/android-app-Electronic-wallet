package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestActivity extends AppCompatActivity {
    private NodeJsApiService nodeJsApiService;
    private PassbookAdapter passbookAdapter;
    private GridView passbookGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        nodeJsApiService = NodeJsApiClient.getNodeJsApiService();
        passbookGridView = findViewById(R.id.passbookGridView);

        fetchPassbooks();
    }
    private void fetchPassbooks() {
        Call<List<Passbook>> call = nodeJsApiService.getAllPassbook();
        call.enqueue(new Callback<List<Passbook>>() {
            @Override
            public void onResponse(Call<List<Passbook>> call, Response<List<Passbook>> response) {
                if (response.isSuccessful()) {
                    List<Passbook> passbooks = response.body();
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