package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.net.URISyntaxException;

public class Signup extends AppCompatActivity {
    private Button btnSignup, btnTransferToSigninForm;
    private EditText edtName, edtEmail, edtAddress, edtGender,edtPass, edtRePass;
    private RadioButton rdMale, rdFemale;
    private RadioGroup Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    private void addControl(){
        btnSignup=findViewById(R.id.btnSignup);
        btnTransferToSigninForm=findViewById(R.id.btnTransferToSigninForm);
        edtName=findViewById(R.id.edtName);
        edtEmail=findViewById(R.id.edtEmail);
        edtAddress=findViewById(R.id.edtAddress);
        edtPass=findViewById(R.id.edtPass);
        edtRePass=findViewById(R.id.edtRePass);
    }
}