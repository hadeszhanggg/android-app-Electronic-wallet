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
    private Socket mSocket;
    private EditText editTextUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            mSocket = IO.socket("http://192.168.11.16:333");
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        editTextUsername = findViewById(R.id.editTextUsername);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                if (!username.isEmpty()) {
                    mSocket.emit("login", username);
                    Intent intent = new Intent(MainActivity.this, chat.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    private Emitter.Listener onChatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    Toast.makeText(MainActivity.this, message+"\n", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
