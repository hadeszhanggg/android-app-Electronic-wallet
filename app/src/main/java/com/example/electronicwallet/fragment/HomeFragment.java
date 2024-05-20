    package com.example.electronicwallet.fragment;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;

    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;

    import java.io.Serializable;
    import java.text.DecimalFormat;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import com.example.electronicwallet.DepositActivity;
    import com.example.electronicwallet.InvestActivity;
    import com.example.electronicwallet.ListBillActivity;
    import com.example.electronicwallet.ListVoucherActivity;
    import com.example.electronicwallet.R;
    import com.example.electronicwallet.TransactionActivity;
    import com.example.electronicwallet.models.User;
    import com.example.electronicwallet.models.Wallet;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link HomeFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class HomeFragment extends Fragment {
        private  static  final int MY_REQUEST_CODE=12;
        private  User user;
        private Wallet wallet;
        Button btnShow;
        TextView txtShow, txtSoDu,txtUser;
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        LinearLayout customBtnShow, btnDeposit, btnListBill,btnVouchers,btnInvest,btnTransactions,btnTransfer;
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
            btnTransactions=view.findViewById(R.id.btnTransactions);
            btnTransfer=view.findViewById(R.id.btnTransfer);
        }
      public void addEvent()
      {
          customBtnShow.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (txtShow.getText() == "Show") {
                      btnShow.setBackgroundResource(R.drawable.eye_close);
                      txtShow.setText("Hide");
                      txtSoDu.setText(decimalFormat.format(wallet.getAccount_balance()) + " - VNĐ");
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
                  startActivityForResult(intent, MY_REQUEST_CODE);
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
                  intent.putExtra("User", user);
                  intent.putExtra("Wallet", wallet);
                  startActivityForResult(intent, MY_REQUEST_CODE);
              }
          });
            btnTransactions.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TransactionActivity.class);
                    intent.putExtra("User", user);
                    intent.putExtra("Wallet", wallet);
                    startActivityForResult(intent, MY_REQUEST_CODE);
                }
            });
      }
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (MY_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK && data != null) {
                Serializable serializableExtra = data.getSerializableExtra("Wallet");
                if (serializableExtra instanceof Wallet) {
                    wallet = (Wallet) serializableExtra;
                }
            }
        }
    }