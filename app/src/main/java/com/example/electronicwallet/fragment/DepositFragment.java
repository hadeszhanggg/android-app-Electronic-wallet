package com.example.electronicwallet.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepositFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepositFragment extends Fragment {
    private  User user;

    public DepositFragment() {
    }
    public static DepositFragment newInstance(User user) {
        DepositFragment fragment = new DepositFragment();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deposit, container, false);
    }
}