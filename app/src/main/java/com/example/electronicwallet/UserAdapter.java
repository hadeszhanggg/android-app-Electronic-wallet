package com.example.electronicwallet;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.electronicwallet.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> users;
    private List<User> filteredUsers;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private de.hdodenhof.circleimageview.CircleImageView imgAvatar;

    public UserAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.context = context;
        this.resource = resource;
        this.users = users;
        this.filteredUsers = new ArrayList<>(users);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public int getCount() {
        return filteredUsers.size();
    }

    @Override
    public User getItem(int position) {
        return filteredUsers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        User currentUser = getItem(position);
        TextView txtUsername = listItemView.findViewById(R.id.txtName);
        TextView txtEmail = listItemView.findViewById(R.id.txtEmail);
         imgAvatar = listItemView.findViewById(R.id.imgAvatar);

        txtUsername.setText(currentUser.getUsername());
        txtEmail.setText(currentUser.getEmail());
        imgAvatar.setImageResource(R.drawable.default_avatar);
        if(currentUser.getAvatar()!="default")
            loadAvatar(currentUser, imgAvatar);
        else imgAvatar.setImageResource(R.drawable.default_avatar);
        return listItemView;
    }

    private void loadAvatar(User user, ImageView imgAvatar) {
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            String imageName = user.getAvatar();
            StorageReference imageRef = storageRef.child(imageName);
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imgAvatar);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imgAvatar.setImageResource(R.drawable.default_avatar);
                }
            });
        } else {
            imgAvatar.setImageResource(R.drawable.default_avatar);
        }
    }
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(users);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User user : users) {
                    if (user.getUsername().toLowerCase().contains(filterPattern) || user.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredUsers.clear();
            filteredUsers.addAll((List<User>) results.values);
            notifyDataSetChanged();
        }
    };
}
