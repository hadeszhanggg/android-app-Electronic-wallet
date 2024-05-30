package com.example.electronicwallet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electronicwallet.models.ChatMessage;
import com.example.electronicwallet.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ChatActivity extends AppCompatActivity {
    private User currentUser;
    private User selectedUser;
    private ListView listViewMessages;
    private com.google.android.material.textfield.TextInputEditText editTextMessage;
    private Button buttonSend;
    private FirebaseFirestore db;
    private String chatDocId;
    private ChatAdapter chatAdapter;
    private Gson gson;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        db = FirebaseFirestore.getInstance();
        gson = new Gson();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("currentUser");
        selectedUser = (User) intent.getSerializableExtra("selectedUser");

        chatDocId = "chat_" + (currentUser.getID().compareTo(selectedUser.getID()) < 0 ? currentUser.getID() + "_" + selectedUser.getID() : selectedUser.getID() + "_" + currentUser.getID());

        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        listViewMessages.setAdapter(chatAdapter);

        loadChatFile();

        buttonSend.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString();
            if (!messageContent.isEmpty()) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                ChatMessage chatMessage = new ChatMessage(currentUser.getID(), currentUser.getUsername(), messageContent, timestamp);
                sendMessage(chatMessage);
            }
        });
    }
    private void uploadChatFile(String content) {
        String fileName = chatDocId + ".txt";
        StorageReference fileRef = storageRef.child(fileName);
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Log.d("Upload txt chat file", "uploadChatFile: success!");
        UploadTask uploadTask = fileRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Upload txt chat file", "uploadChatFile: success!");
                        Toast.makeText(ChatActivity.this, "Chat history saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatActivity.this, "Failed to save chat history", Toast.LENGTH_SHORT).show();
                Log.e("Upload txt chat file", "uploadChatFile: failed!");
            }
        });
    }

    private void loadChatFile() {
        String fileName = chatDocId + ".txt";
        StorageReference fileRef = storageRef.child("Chat/" + fileName);

        fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            String content = new String(bytes, StandardCharsets.UTF_8);
            Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
            List<ChatMessage> messages = gson.fromJson(content, listType);
            chatAdapter.clear();
            chatAdapter.addAll(messages);
        }).addOnFailureListener(e -> {
            Toast.makeText(ChatActivity.this, "Failed to load chat file", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendMessage(ChatMessage message) {
        DocumentReference chatDocRef = db.collection("chats").document(chatDocId);
        chatDocRef.get().addOnSuccessListener(snapshot -> {
            Log.d("Upload txt chat file", "uploadChatFile: success!");
            List<ChatMessage> messages = new ArrayList<>();
            if (snapshot.exists()) {
                String chatContent = snapshot.getString("messages");
                Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
                messages = gson.fromJson(chatContent, listType);
            }
            messages.add(message);
            String updatedChatContent = gson.toJson(messages);
            chatDocRef.set(new Chat(messages), SetOptions.merge())
                    .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());
            uploadChatFile(updatedChatContent);
        });
        chatAdapter.add(message);
        editTextMessage.setText("");
    }


    private static class Chat {
        private List<ChatMessage> messages;

        public Chat(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public List<ChatMessage> getMessages() {
            return messages;
        }

        public void setMessages(List<ChatMessage> messages) {
            this.messages = messages;
        }
    }
}
