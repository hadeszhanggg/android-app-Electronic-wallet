package com.example.electronicwallet.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private  User user;
    private Wallet wallet;
    Button btnShow;
    TextView txtShow, txtSoDu,txtUser;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    LinearLayout customBtnShow, btnDeposit, btnListBill,btnVouchers,btnInvest,btnLottery,btnNews;
    public HomeFragment() {
    }

    public static HomeFragment newInstance(User user, Wallet wallet) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        args.putSerializable("Wallet", wallet);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
            wallet= (Wallet) getArguments().getSerializable("Wallet");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addControl(view);
        addEvent();
        return view;
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
              if(txtShow.getText()=="Show"){
                  btnShow.setBackgroundResource(R.drawable.eye_close);
                  txtShow.setText("Hide");
                  txtSoDu.setText(decimalFormat.format(wallet.getAccount_balance())+" - VNĐ");
              }else {
                  btnShow.setBackgroundResource(R.drawable.eye_open);
                  txtShow.setText("Show");
                  txtSoDu.setText("******");
              }

          }
      });

  }
}