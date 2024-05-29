package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicwallet.models.User;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private Context context;
    private List<User> requestList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onConfirmClick(User user);
        void onCancelClick(User user);
    }

    public FriendRequestAdapter(Context context, List<User> requestList, OnItemClickListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = requestList.get(position);
        holder.txtName.setText(user.getUsername());
        holder.txtEmail.setText(user.getEmail());

        holder.btnConfirm.setOnClickListener(v -> {
            listener.onConfirmClick(user);
            requestList.remove(user);
            notifyDataSetChanged();
        });
        holder.btnCancel.setOnClickListener(v -> {
            listener.onCancelClick(user);
            requestList.remove(user);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail;
        ImageButton btnConfirm, btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
