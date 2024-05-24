package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<User> friendList;
    private Wallet wallet;
    private SearchView searchView;
    private NodeJsApiService apiService;
    private User user;
    private LinearLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            user = (User) intent.getSerializableExtra("User");
            wallet = (Wallet) intent.getSerializableExtra("Wallet");
        } else {
            Log.e("ERROR", "No user object passed to TransferActivity");
            finish();
        }
        addControl();
        userList = new ArrayList<>();
        friendList = new ArrayList<>();
        apiService = NodeJsApiClient.getNodeJsApiService();
        fetchFriends();
        addEvent();
    }

    protected void addControl() {
        listViewUsers = findViewById(R.id.listViewUsers);
        searchView = findViewById(R.id.searchView);
        btnBack = findViewById(R.id.btnBack);
    }

    protected void addEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fetchFriends();
                } else {
                    fetchUsers(newText);
                }
                return true;
            }
        });
    }

    private void fetchFriends() {
        String authToken = "Bearer " + user.getAccesssToken();
        apiService.getUnconfirmedFriends(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList.clear();
                    friendList.addAll(response.body());
                    userAdapter = new UserAdapter(TransferActivity.this, R.layout.item_user, friendList);
                    listViewUsers.setAdapter(userAdapter);
                } else {
                    Toast.makeText(TransferActivity.this, "Failed to fetch friends", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(TransferActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsers(String query) {
        String authToken = "Bearer " + user.getAccesssToken();
        apiService.getAllUser(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.clear();
                    userAdapter.addAll(userList);
                    userAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(TransferActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(TransferActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
