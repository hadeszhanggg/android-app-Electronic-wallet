package com.example.electronicwallet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnconfirmFriendList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnconfirmFriendList extends Fragment {
    protected List<User> users;

    public UnconfirmFriendList() {
        // Required empty public constructor
    }

    public static UnconfirmFriendList newInstance(List<User> users) {
        UnconfirmFriendList fragment = new UnconfirmFriendList();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unconfirm_friend_list, container, false);
    }
}