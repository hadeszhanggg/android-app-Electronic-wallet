package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.example.electronicwallet.models.User;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import com.example.electronicwallet.network.Constants;
public class chat extends AppCompatActivity {
    private User user;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.23:333");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private EditText mInputMessageView;
    private ListView listViewMessages;
    private ArrayAdapter<String> messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        if (user != null) {
            // Tiếp tục xử lý với đối tượng user
            mSocket.connect();
            mSocket.emit("joinRoom", user.getID());
        } else {
            Toast.makeText(chat.this, "User object is null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit("joinRoom", user.getID());
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Handle disconnect event
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Handle connect error
                }
            });
        }
    };

    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        message = data.getString("message");
                        messageAdapter.add(message);
                        messageAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void sendMessage(View view) {
        String message = mInputMessageView.getText().toString().trim();
        if (!message.isEmpty()) {
            mSocket.emit("message", message);
            mInputMessageView.setText("");
        }
    }
}
