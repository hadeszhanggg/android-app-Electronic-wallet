package com.example.electronicwallet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicwallet.DepositActivity;
import com.example.electronicwallet.HomeActivity;
import com.example.electronicwallet.InvestActivity;
import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.ListVoucherActivity;
import com.example.electronicwallet.MainActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.Signup;
import com.example.electronicwallet.models.DataModel;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private DataModel dataViewModel;
    private  User user;
    private Wallet wallet;
    Button btnShow;
    TextView txtShow, txtSoDu,txtUser;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    LinearLayout customBtnShow, btnDeposit, btnListBill,btnVouchers,btnInvest,btnLottery,btnNews;
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addControl(view);
        addEvent();
        observeViewModel();
        return view;
    }
    private void observeViewModel() {
        dataViewModel.getUser().observe(getViewLifecycleOwner(), user -> {

        });

        dataViewModel.getWallet().observe(getViewLifecycleOwner(), wallet -> {

        });
    }
    public void addControl(View view){
        customBtnShow=view.findViewById(R.id.customBtnShow);
        btnShow=customBtnShow.findViewById(R.id.btnShow);
        txtShow=customBtnShow.findViewById(R.id.txtShow);
        txtSoDu=view.findViewById(R.id.txtSoDu);
        btnDeposit=view.findViewById(R.id.btnDeposit);
        btnInvest=view.findViewById(R.id.btnInvest);
        btnListBill=view.findViewById(R.id.btnListBill);
       btnVouchers=view.findViewById(R.id.btnVouchers);
       btnLottery=view.findViewById(R.id.btnLottery);
       btnNews=view.findViewById(R.id.btnNews);
    }
  public void addEvent()
  {
      customBtnShow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (txtShow.getText() == "Show") {
                  btnShow.setBackgroundResource(R.drawable.eye_close);
                  txtShow.setText("Hide");
                  txtSoDu.setText(decimalFormat.format(dataViewModel.getWallet().getValue().getAccount_balance()) + " - VNĐ");
              } else {
                  btnShow.setBackgroundResource(R.drawable.eye_open);
                  txtShow.setText("Show");
                  txtSoDu.setText("******");
              }
          }
      });
      btnListBill.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(), ListBillActivity.class);
              intent.putExtra("User", user);
              intent.putExtra("Wallet", wallet);
              startActivity(intent);
          }
      });
      btnVouchers.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(), ListVoucherActivity.class);
              intent.putExtra("User", user);
              startActivity(intent);
          }
      });
      btnDeposit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(), DepositActivity.class);
              intent.putExtra("User", user);
              startActivity(intent);
          }
      });
      btnInvest.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(), InvestActivity.class);
              startActivity(intent);
          }
      });
  }
}