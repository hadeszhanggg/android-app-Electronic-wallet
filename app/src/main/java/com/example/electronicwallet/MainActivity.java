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
import android.widget.Toast;
import java.net.URISyntaxException;
public class MainActivity extends AppCompatActivity {
    private Button btnSignin, btnTransferToSignupForm;
    private EditText edtName, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
    }
private void addControl()
{
    btnSignin=findViewById(R.id.btnSignin);
    btnTransferToSignupForm=findViewById(R.id.btnTransferToSignupForm);
    edtName=findViewById(R.id.edtName);
    edtPass=findViewById(R.id.edtPass);
}
private void addEvent()
{
    btnTransferToSignupForm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getApplicationContext(), Signup.class);
            startActivity(it);
            // Quick Notification
            Toast.makeText(getApplicationContext(), "Close signin form", Toast.LENGTH_SHORT).show();
        }
    });

    btnSignin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_LONG).show();
        }
    });
}
}
