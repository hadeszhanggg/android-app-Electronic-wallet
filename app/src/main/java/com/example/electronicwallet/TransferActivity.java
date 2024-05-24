package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.example.electronicwallet.Interface.DataShared;
public class TransferActivity extends AppCompatActivity implements DataShared {
    private ListView listViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<User> friendList, unconfirmFriendList;
    private Wallet wallet;
    private SearchView searchView;
    private NodeJsApiService apiService;
    private User user;
    private ImageView imgNotify;
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
        unconfirmFriendList = new ArrayList<>();
        apiService = NodeJsApiClient.getNodeJsApiService();
        fetchFriends();
        fetchUnconfirmFriends();
        addEvent();
    }

    protected void addControl() {
        listViewUsers = findViewById(R.id.listViewUsers);
        searchView = findViewById(R.id.searchView);
        btnBack = findViewById(R.id.btnBack);
        imgNotify=findViewById(R.id.imgNotify);
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
        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriendRequestsDialog();
            }
        });
    }

    private void showFriendRequestsDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_friend_request);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        RecyclerView listViewRequests = dialog.findViewById(R.id.listViewRequests);
        listViewRequests.setLayoutManager(new LinearLayoutManager(this));

        FriendRequestAdapter adapter = new FriendRequestAdapter(this, unconfirmFriendList, new FriendRequestAdapter.OnItemClickListener() {
            @Override
            public void onConfirmClick(User user) {

            }

            @Override
            public void onCancelClick(User user) {
                // Handle cancel click
            }
        });

        listViewRequests.setAdapter(adapter);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    //Interface shared data giua cac activity va fragment nham dam bao du lieu wallet luon chinh xac!
    @Override
    public void dataShared(Wallet updatedWallet) {
        this.wallet = updatedWallet;
    }
    private void fetchUnconfirmFriends() {
        String authToken = "Bearer " + user.getAccesssToken();
        apiService.getUnconfirmedFriends(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unconfirmFriendList.clear();
                    unconfirmFriendList.addAll(response.body());
                    if(unconfirmFriendList.size() >= 1)
                        imgNotify.setImageResource(R.drawable.notify_news);
                    else
                        imgNotify.setImageResource(R.drawable.notify);
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

    private void fetchFriends() {
        String authToken = "Bearer " + user.getAccesssToken();
        apiService.getAllFriends(authToken).enqueue(new Callback<List<User>>() {
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