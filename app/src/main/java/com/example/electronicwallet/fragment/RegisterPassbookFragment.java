package com.example.electronicwallet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.Passbook;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPassbookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPassbookFragment extends Fragment {
    private ImageView passbookImageView;
    private TextView passbookNameTextView, descriptionTextView, interestRateTextView, periodTextView;
    private Button btnRegister, btnClose;
    private Passbook passbook;
    public RegisterPassbookFragment() {
        // Required empty public constructor
    }
    public static RegisterPassbookFragment newInstance(Passbook passbook) {
        RegisterPassbookFragment fragment = new RegisterPassbookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void setPassbook(Passbook passbook) {
        this.passbook = passbook;
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
        View view = inflater.inflate(R.layout.fragment_register_passbook, container, false);
        initView(view);
        populatePassbookData();
        return view;
    }

    private void initView(View view) {
        passbookImageView = view.findViewById(R.id.passbookImageView);
        passbookNameTextView = view.findViewById(R.id.passbookNameTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        interestRateTextView = view.findViewById(R.id.interestRateTextView);
        periodTextView = view.findViewById(R.id.periodTextView);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnClose = view.findViewById(R.id.btnClose);
    }

    private void populatePassbookData() {
        if (passbook != null) {
            if (passbook.getPassbook_name().equals("Tài lộc đầy nhà"))
                passbookImageView.setImageResource(R.drawable.tuitien_icon);
            else if (passbook.getPassbook_name().equals("Heo đỏ tài lộc"))
                passbookImageView.setImageResource(R.drawable.heodo_icon);
            else if (passbook.getPassbook_name().equals("Heo vàng tài lộc"))
                passbookImageView.setImageResource(R.drawable.heovang_icon);
            else
                passbookImageView.setImageResource(R.drawable.latien_icon);
            passbookNameTextView.setText(passbook.getPassbook_name());
            descriptionTextView.setText(passbook.getDescription());
            interestRateTextView.setText(String.valueOf(passbook.getInterest_rate()));
            periodTextView.setText(String.valueOf(passbook.getPeriod()));
            btnClose.setBackgroundResource(R.drawable.close_icon);
            btnRegister.setBackgroundResource(R.drawable.pay_cash);
        }
    }
}