package com.example.electronicwallet.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicwallet.Interface.DataShared;
import com.example.electronicwallet.InvestActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.TransferActivity;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferMoneyFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferMoneyFragment extends Fragment {
    private static DataShared listener;
    private ImageView avatar;
    private TextView userName, userEmail;
    private Button btnTransfer, btnClose;
    protected User user, selectedUser;
    protected Wallet wallet;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public TransferMoneyFragment () {
        // Required empty public constructor
    }

    public static TransferMoneyFragment newInstance(User user, User selectedUser, Wallet wallet, DataShared walletDatasharedListener) {
        listener = walletDatasharedListener;
        TransferMoneyFragment fragment = new TransferMoneyFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putSerializable("wallet", wallet);
        args.putSerializable("selectedUser", selectedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
            selectedUser = (User) getArguments().getSerializable("selectedUser");
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_money, container, false);
        initView(view);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        Log.d("selected user", "onCreateView: " + selectedUser.getUsername());
        populateUser(avatar);
        addEvent();
        return view;
    }

    private void initView(View view) {
        avatar = view.findViewById(R.id.avatar);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        btnTransfer = view.findViewById(R.id.btnTransfer);
        btnClose = view.findViewById(R.id.btnClose);
    }

    private void populateUser(ImageView imgAvatar) {
        if (selectedUser != null) {
            loadAvatar(selectedUser, imgAvatar);
            userName.setText(selectedUser.getUsername());
            userEmail.setText(selectedUser.getEmail());
        }
    }

    private void loadAvatar(User selectedUser, ImageView imgAvatar) {
        if (selectedUser.getAvatar() != null && !selectedUser.getAvatar().isEmpty()) {
            String imageName = selectedUser.getAvatar();
            StorageReference imageRef = storageRef.child(imageName);
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(imgAvatar))
                    .addOnFailureListener(exception -> imgAvatar.setImageResource(R.drawable.default_avatar));
        } else {
            imgAvatar.setImageResource(R.drawable.default_avatar);
        }
    }

    protected void addEvent() {
        // Xử lý sự kiện khi nút đóng fragment được click
        btnClose.setOnClickListener(v -> closeFragment());
        // Xử lý sự kiện khi nút đăng ký được click
        btnTransfer.setOnClickListener(v -> showDialogConfirmation());
    }

    private void closeFragment() {
        if (getActivity() != null && getActivity() instanceof TransferActivity) {
            ((TransferActivity) getActivity()).onFragmentClosed();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }

    // Phương thức để hiển thị dialog xác nhận đăng ký
    private void showDialogConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to transfer this amount?");
        // Thêm EditText vào dialog
        final com.google.android.material.textfield.TextInputEditText inputAmount = new com.google.android.material.textfield.TextInputEditText(getContext());
        final com.google.android.material.textfield.TextInputEditText inputContent = new com.google.android.material.textfield.TextInputEditText(getContext());
        inputAmount.setHint("Enter the amount you want to transfer");
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputContent.setHint("Enter the content you want to transfer");
        inputContent.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputAmount);
        layout.addView(inputContent);
        builder.setView(layout);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            String amount = inputAmount.getText().toString();
            String content = inputContent.getText().toString();
            transferMoney(amount, content, selectedUser);
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void transferMoney(String amount, String content, User selectedUser) {
        String authToken = "Bearer " + user.getAccesssToken();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("amount", amount);
            requestBody.put("recipientUsernameOrEmail", selectedUser.getUsername());
            requestBody.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().transferMoney(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Transfer money successfully!", Toast.LENGTH_LONG).show();
                    wallet.setAccount_balance(wallet.getAccount_balance() - Float.parseFloat(amount));
                    listener.dataShared(wallet);
                    closeFragment();
                } else {
                    String errorMessage = "";
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        errorMessage = errorBody.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Transfer money FAILED!", Toast.LENGTH_LONG).show();
                    Log.e("API_CALL", "Unsuccessful response: " + response.code());
                    Log.e("API_CALL", "Unsuccessful response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Transfer money", "Failed when transfer money!: " + t.getMessage());
            }
        });
    }
}
