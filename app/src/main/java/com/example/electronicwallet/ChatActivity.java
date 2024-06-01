package com.example.electronicwallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electronicwallet.models.ChatMessage;
import com.example.electronicwallet.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ChatActivity extends AppCompatActivity {
    private User currentUser;
    private User selectedUser;
    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private DatabaseReference chatRef;
    private String chatDocId;
    private ChatAdapter chatAdapter;
    private Gson gson;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private LinearLayout btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        addControl();
        gson = new Gson();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("currentUser");
        selectedUser = (User) intent.getSerializableExtra("selectedUser");

        chatDocId = currentUser.getID().compareTo(selectedUser.getID()) < 0 ?
                currentUser.getID() + "_" + selectedUser.getID() : selectedUser.getID() + "_" + currentUser.getID();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-wallet-8e979-default-rtdb.asia-southeast1.firebasedatabase.app");
        chatRef = database.getReference().child("chats").child(chatDocId);

        chatAdapter = new ChatAdapter(this, new ArrayList<>(), currentUser);
        listViewMessages.setAdapter(chatAdapter);
        loadChatHistory();
        addEvent();
    }
    private void addControl(){
        btnBack=findViewById(R.id.btnBack);
        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
    }
    private void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString();
            if (!messageContent.isEmpty()) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                ChatMessage chatMessage = new ChatMessage(currentUser.getID(), currentUser.getUsername(), messageContent, timestamp);
                sendMessage(chatMessage);
            }
        });
    }
    private void loadChatHistory() {
        String fileName = chatDocId + ".txt";
        StorageReference fileRef = storageRef.child("Chat/" + fileName);

        fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            String content = new String(bytes, StandardCharsets.UTF_8);
            //Khai báo listType với kiểu "ChatMessage" để lấy ra danh sách các tin nhánh
            Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
            //Đọc theo json lưu vào list messages
            List<ChatMessage> messages = gson.fromJson(content, listType);
            chatAdapter.clear();
            chatAdapter.addAll(messages);
        }).addOnFailureListener(e -> {
            Toast.makeText(ChatActivity.this, "Failed to load chat history from storage", Toast.LENGTH_SHORT).show();
            Log.e("ChatActivity", "Failed to load chat history", e);
        });

        //lắng nghe thay đổi từ Firebase Realtime Database để cập nhật tin nhắn mới
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatMessage> messages = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    messages.add(message);
                }
                chatAdapter.clear();
                chatAdapter.addAll(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to load chat history from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(ChatMessage message) {
        String messageId = chatRef.push().getKey(); // Tạo một ID duy nhất cho tin nhắn
        chatRef.child(messageId).setValue(message)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ChatActivity", "Message sent successfully");
                    editTextMessage.setText(""); // Xóa nội dung tin nhắn sau khi gửi
                })
                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());

        uploadChatHistory(); // Gọi hàm này để lưu trữ lịch sử chat lên Firebase Storage mỗi khi gửi tin nhắn mới
    }

    private void uploadChatHistory() {
        chatRef.get().addOnSuccessListener(dataSnapshot -> {
            List<ChatMessage> messages = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                ChatMessage message = snapshot.getValue(ChatMessage.class);
                messages.add(message);
            }
            String content = gson.toJson(messages);
            uploadChatFile(content);
        }).addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to retrieve chat history for upload", Toast.LENGTH_SHORT).show());
    }

    private void uploadChatFile(String content) {
        String fileName = chatDocId + ".txt";
        StorageReference fileRef = storageRef.child("Chat/" + fileName);
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Log.d("Upload txt chat file", "uploadChatFile: success!");
        UploadTask uploadTask = fileRef.putStream(stream);
        uploadTask.addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("Upload txt chat file", "uploadChatFile: success!");
            Toast.makeText(ChatActivity.this, "Chat history saved", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(exception -> {
            Toast.makeText(ChatActivity.this, "Failed to save chat history", Toast.LENGTH_SHORT).show();
            Log.e("Upload txt chat file", "uploadChatFile: failed!");
        });
    }
}
