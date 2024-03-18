package com.example.electronicwallet;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import java.net.URISyntaxException;
public class chat extends AppCompatActivity {
    private Socket mSocket;
    private TextView textViewMessages;
    private EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try {
            mSocket = IO.socket(getResources().getString(R.string.server_url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //textViewMessages = findViewById(R.id.textViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
       // Button btnSend = findViewById(R.id.btnSend);

        mSocket.connect();

        /*btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    mSocket.emit("chat message", message);
                    editTextMessage.setText(""); // Xóa nội dung sau khi gửi
                } else {
                    Toast.makeText(chat.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mSocket.on("chat message", onChatMessage);
    }

    private Emitter.Listener onChatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    textViewMessages.append(message + "\n");
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("chat message", onChatMessage);
    }
}