package com.example.electronicwallet.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.electronicwallet.HomeActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    protected User user;
    protected Wallet wallet;
    LinearLayout btnSave;
    ImageView imgAvatar, unblockUsername, unblockEmail, unblockPass, unblockGender, unblockDateOfBirth, unblockAddress;
    TextView txtName;
    RadioGroup rdGrGender;
    RadioButton rdMale, rdFemale;
    EditText inputUsername, inputEmail, inputPass, inputDateOfBirth, inputAddress;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    public PersonalFragment() {

    }

    public static PersonalFragment newInstance(User user, Wallet wallet) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putSerializable("User", user);
        args.putSerializable("Wallet", wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
            wallet = (Wallet) getArguments().getSerializable("Wallet");
        }
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        addControl(view);
        txtName.setText(user.getUsername());
        inputUsername.setText(user.getUsername());
        inputEmail.setText(user.getEmail());
        inputDateOfBirth.setText(user.getDateOfBirth());
        inputAddress.setText(user.getAddress());
        if (user.getGender() == true)
            rdMale.setChecked(true);
        else rdFemale.setChecked(true);

        loadAvatar();

        addEvent();
        return view;
    }

    public void addControl(View view) {
        txtName = (TextView) view.findViewById(R.id.txtTenUser);
        unblockUsername = view.findViewById(R.id.unblockUsername);
        unblockPass = view.findViewById(R.id.unblockPass);
        unblockEmail = view.findViewById(R.id.unblockEmail);
        unblockGender = view.findViewById(R.id.unblockGender);
        unblockDateOfBirth = view.findViewById(R.id.unblockDateOfBirth);
        unblockAddress = view.findViewById(R.id.unblockAddress);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPass = view.findViewById(R.id.inputPassword);
        inputAddress = view.findViewById(R.id.inputAddress);
        inputDateOfBirth = view.findViewById(R.id.inputDateOfBirth);
        rdGrGender = view.findViewById(R.id.grRdGender);
        rdMale = view.findViewById(R.id.rdMale);
        rdFemale = view.findViewById(R.id.rdFemale);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnSave=view.findViewById(R.id.btnSave);
    }

    public void addEvent() {
        unblockUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputUsername.isEnabled() == false)
                    inputUsername.setEnabled(true);
                else inputUsername.setEnabled(false);
            }
        });
        unblockPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPass.isEnabled() == false) {
                    inputPass.setText(user.getPassword());
                    inputPass.setEnabled(true);
                } else {
                    inputPass.setText("******");
                    inputPass.setEnabled(false);
                }
            }
        });
        unblockEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail.isEnabled() == false)
                    inputEmail.setEnabled(true);
                else inputEmail.setEnabled(false);
            }
        });
        unblockDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputDateOfBirth.isEnabled() == false)
                    inputDateOfBirth.setEnabled(true);
                else inputDateOfBirth.setEnabled(false);
            }
        });
        unblockGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdFemale.isEnabled() == false && rdMale.isEnabled() == false) {
                    rdFemale.setEnabled(true);
                    rdMale.setEnabled(true);
                } else {
                    rdFemale.setEnabled(false);
                    rdMale.setEnabled(false);
                }
            }
        });
        unblockAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputAddress.isEnabled() == false)
                    inputAddress.setEnabled(true);
                else inputAddress.setEnabled(false);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                    Log.e("Permission denied", "No provided permission access to external media file");
                    openImagePicker();
                } else {
                    openImagePicker();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authToken = "Bearer " + user.getAccesssToken();
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("username", inputUsername.getText());
                    requestBody.put("password", inputPass.getText());
                    requestBody.put("address",inputAddress.getText());
                    requestBody.put("email",inputEmail.getText());
                    String gender;
                    if(rdMale.isChecked()==true)
                        gender="true";
                    else gender="false";
                    requestBody.put("gender",gender);
                    requestBody.put("date_of_birth",inputDateOfBirth.getText());
                    requestBody.put("avatar",user.getAvatar());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Gọi API RegisterPassbook
                Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().editInformation(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Edit your information successfully!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String errorMessage = "";
                            try {
                                JSONObject errorBody = new JSONObject(response.errorBody().string());
                                errorMessage = errorBody.getString("message");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Edit your information FAILED!", Toast.LENGTH_LONG).show();
                            Log.e("API_CALL", "Unsuccessful response: " + response.code());
                            Log.e("API_CALL", "Unsuccessful response: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Edit personal information", "Failed when Edit personal information!: " + t.getMessage());                }
                });
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                // Quyền bị từ chối
                Log.e("Permission denied", "No provided permission access to external media file");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                uploadImageToFirebase(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String imageName = user.getUsername() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.setAvatar(imageName);
                        // Lưu đối tượng user vào Firebase Database nếu cần
                        loadAvatar();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xử lý khi upload ảnh thất bại
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAvatar() {
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
                    // set anh mặc định nếu không tải được ảnh
                    imgAvatar.setImageResource(R.drawable.default_avatar);
                }
            });
        } else {
            //set ảnh mặc định nếu user chưa có ảnh
            imgAvatar.setImageResource(R.drawable.default_avatar);
        }
    }
}