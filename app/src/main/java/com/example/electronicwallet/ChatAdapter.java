package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.electronicwallet.models.ChatMessage;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    public ChatAdapter(Context context, List<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        com.google.android.material.textview.MaterialTextView textViewMessage, textViewSender, textViewTimestamp;
        textViewMessage = convertView.findViewById(R.id.textViewMessage);
         textViewSender = convertView.findViewById(R.id.textViewSender);
        textViewTimestamp = convertView.findViewById(R.id.textViewTimestamp);

        textViewMessage.setText(message.getContent());
        textViewSender.setText(message.getSenderName());
        textViewTimestamp.setText(message.getTimestamp());

        return convertView;
    }
}
