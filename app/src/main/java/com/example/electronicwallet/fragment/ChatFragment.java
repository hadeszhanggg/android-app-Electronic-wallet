package com.example.electronicwallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.electronicwallet.R;
import com.example.electronicwallet.UserAdapter;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.example.electronicwallet.network.NodeJsApiService;
import com.example.electronicwallet.ChatActivity;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private User user;
    private List<User> friendList;
    private ListView listViewUsers;
    private UserAdapter userAdapter;
    private NodeJsApiService apiService;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(User user) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    private void fetchFriends() {
        if (user == null) {
            Toast.makeText(getContext(), "User is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String authToken = "Bearer " + user.getAccesssToken();
        apiService.getAllFriends(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList.clear();
                    friendList.addAll(response.body());
                    userAdapter = new UserAdapter(getContext(), R.layout.item_user, friendList);
                    listViewUsers.setAdapter(userAdapter);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch friends", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listViewUsers = view.findViewById(R.id.listViewUsers);
        friendList = new ArrayList<>();
        apiService = NodeJsApiClient.getNodeJsApiService();
        fetchFriends();

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = (User) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("currentUser", user);
                intent.putExtra("selectedUser", selectedUser);
                startActivity(intent);
            }
        });

        return view;
    }
}
