package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.electronicwallet.models.ChatMessage;
import com.example.electronicwallet.models.User;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private User user;

    public ChatAdapter(Context context, List<ChatMessage> messages, User user) {
        super(context, 0, messages);
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        LinearLayout layoutMessageCurrentUser = convertView.findViewById(R.id.layoutMessageCurrentUser);
        LinearLayout layoutMessageOtherUser = convertView.findViewById(R.id.layoutMessageOtherUser);

        if (message.getSenderId().equals(user.getID())) {
            layoutMessageCurrentUser.setVisibility(View.VISIBLE);
            layoutMessageOtherUser.setVisibility(View.GONE);
            
            TextView textViewMessageCurrentUser = convertView.findViewById(R.id.textViewMessageCurrentUser);
            TextView textViewSenderCurrentUser = convertView.findViewById(R.id.textViewSenderCurrentUser);
            TextView textViewTimestampCurrentUser = convertView.findViewById(R.id.textViewTimestampCurrentUser);

            textViewMessageCurrentUser.setText(message.getContent());
            textViewSenderCurrentUser.setText(message.getSenderName());
            textViewTimestampCurrentUser.setText(message.getTimestamp());
        } else {
            layoutMessageCurrentUser.setVisibility(View.GONE);
            layoutMessageOtherUser.setVisibility(View.VISIBLE);

            TextView textViewMessageOtherUser = convertView.findViewById(R.id.textViewMessageOtherUser);
            TextView textViewSenderOtherUser = convertView.findViewById(R.id.textViewSenderOtherUser);
            TextView textViewTimestampOtherUser = convertView.findViewById(R.id.textViewTimestampOtherUser);

            textViewMessageOtherUser.setText(message.getContent());
            textViewSenderOtherUser.setText(message.getSenderName());
            textViewTimestampOtherUser.setText(message.getTimestamp());
        }

        return convertView;
    }
}
