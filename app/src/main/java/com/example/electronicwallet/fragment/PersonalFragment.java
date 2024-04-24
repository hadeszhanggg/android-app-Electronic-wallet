package com.example.electronicwallet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.electronicwallet.HomeActivity;
import com.example.electronicwallet.MainActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    protected User user;
    protected Wallet wallet;
    LinearLayout btnBack;
    ImageView unblockUsername, unblockEmail, unblockPass, unblockGender, unblockDateOfBirth, unblockAddress;
    TextView txtName;
    RadioGroup rdGrGender;
    RadioButton rdMale, rdFemale;
    EditText inputUsername, inputEmail, inputPass,  inputDateOfBirth, inputAddress;
    public PersonalFragment() {

    }

    public static PersonalFragment newInstance(User user, Wallet wallet) {
        PersonalFragment fragment = new PersonalFragment();
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
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        addControl(view);
        txtName.setText(user.getUsername());
        inputUsername.setText(user.getUsername());
        inputEmail.setText(user.getEmail());
        inputDateOfBirth.setText(user.getDateOfBirth());
        inputAddress.setText(user.getAddress());
        if(user.getGender()==true)
            rdMale.setChecked(true);
        else rdFemale.setChecked(true);
        addEvent();
        return view;
    }
    public void addControl(View view){
        txtName=(TextView) view.findViewById(R.id.txtTenUser);
        unblockUsername=view.findViewById(R.id.unblockUsername);
        unblockPass=view.findViewById(R.id.unblockPass);
        unblockEmail=view.findViewById(R.id.unblockEmail);
        unblockGender=view.findViewById(R.id.unblockGender);
        unblockDateOfBirth=view.findViewById(R.id.unblockDateOfBirth);
        unblockAddress=view.findViewById(R.id.unblockAddress);
        inputUsername=view.findViewById(R.id.inputUsername);
        inputEmail=view.findViewById(R.id.inputEmail);
        inputPass=view.findViewById(R.id.inputPassword);
        inputAddress=view.findViewById(R.id.inputAddress);
        inputDateOfBirth=view.findViewById(R.id.inputDateOfBirth);
        rdGrGender=view.findViewById(R.id.grRdGender);
        rdMale=view.findViewById(R.id.rdMale);
        rdFemale=view.findViewById(R.id.rdFemale);
        btnBack=view.findViewById(R.id.btnBack);
    }
    public void addEvent(){
        unblockUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputUsername.isEnabled()==false)
                    inputUsername.setEnabled(true);
                else inputUsername.setEnabled(false);
            }
        });
        unblockPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputPass.isEnabled()==false) {
                    inputPass.setText(user.getPassword());
                    inputPass.setEnabled(true);
                }
                else {
                    inputPass.setText("******");
                    inputPass.setEnabled(false);
                }
            }
        });
        unblockEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputEmail.isEnabled()==false)
                    inputEmail.setEnabled(true);
                else inputEmail.setEnabled(false);
            }
        });
        unblockDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputDateOfBirth.isEnabled()==false)
                    inputDateOfBirth.setEnabled(true);
                else inputDateOfBirth.setEnabled(false);
            }
        });
        unblockGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdFemale.isEnabled()==false&&rdMale.isEnabled()==false)
                {
                    rdFemale.setEnabled(true);
                    rdMale.setEnabled(true);
                }
                else{
                    rdFemale.setEnabled(false);
                    rdMale.setEnabled(false);
                }
            }
        });
        unblockAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputAddress.isEnabled()==false)
                    inputAddress.setEnabled(true);
                else inputAddress.setEnabled(false);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}