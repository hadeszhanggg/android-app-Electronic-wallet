package com.example.electronicwallet.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.InvestActivity;
import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {
    private ImageView BillImageView;
    private TextView expiryDaysTextView, descriptionTextView, totalTextView;
    private Button btnPay, btnClose;
    protected Bill bill;
    protected User user;
    protected Wallet wallet;
    public PayFragment() {

    }
    public static PayFragment newInstance(Bill bill,User user, Wallet wallet) {
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putSerializable("bill", bill);
        args.putSerializable("user", user);
        args.putSerializable("wallet", wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bill = (Bill) getArguments().getSerializable("bill");
            user = (User) getArguments().getSerializable("user");
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }
    }
    private void initView(View view) {
        BillImageView = view.findViewById(R.id.billImageView);
        expiryDaysTextView = view.findViewById(R.id.expiryDaysTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        totalTextView = view.findViewById(R.id.totalTextView);
        btnPay = view.findViewById(R.id.btnPay);
        btnClose = view.findViewById(R.id.btnClose);
    }
    protected void addEvent(){
        // Xử lý sự kiện khi nút đóng fragment được click
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        // Xử lý sự kiện khi nút đăng ký được click
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmation();
            }
        });
    }
    private void closeFragment(){
        if (getActivity() != null && getActivity() instanceof ListBillActivity) {
            ((ListBillActivity) getActivity()).onFragmentClosed();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
    private void showDialogConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to register this passbook?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void populateBillData() {
        if (bill != null) {
            if (bill.getType().equals("electric"))
                BillImageView.setImageResource(R.drawable.electronic_bill);
            else if (bill.getType().equals("water"))
                BillImageView.setImageResource(R.drawable.water_bill);
            else BillImageView.setImageResource(R.drawable.telecom_bill);
            expiryDaysTextView.setText(bill.getExpiryDay().toString());
            descriptionTextView.setText(bill.getDescription());
            totalTextView.setText(String.valueOf(bill.getTotal()));
            btnClose.setBackgroundResource(R.drawable.close_icon);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        initView(view);
        populateBillData();
        addEvent();
        return view;
    }
}