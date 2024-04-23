package com.example.electronicwallet.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private  User user;
    Button btnShow;
    TextView txtShow, txtSoDu,txtUser;
    LinearLayout customBtnShow,customBtnPersonal;
    public HomeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(User user) {
        HomeFragment fragment = new HomeFragment();
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
        txtUser=view.findViewById(R.id.txtUser);
        customBtnPersonal=view.findViewById(R.id.customBtnPersonal);
    }
  public void addEvent()
  {
      customBtnShow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(txtShow.getText()=="Show"){
                  btnShow.setBackgroundResource(R.drawable.eye_close);
                  txtShow.setText("Hide");
                  txtSoDu.setText("100.000"+" - VNĐ");
                  txtUser.setText(user.getEmail());
              }else {
                  btnShow.setBackgroundResource(R.drawable.eye_open);
                  txtShow.setText("Show");
                  txtSoDu.setText("***");
              }

          }
      });
      customBtnPersonal.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
  }
}