        package com.example.electronicwallet.fragment;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.text.InputType;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.electronicwallet.Interface.PassbookRegisteredListener;
        import com.example.electronicwallet.InvestActivity;
        import com.example.electronicwallet.R;
        import com.example.electronicwallet.models.Passbook;
        import com.example.electronicwallet.models.User;
        import com.example.electronicwallet.models.Wallet;
        import com.example.electronicwallet.network.NodeJsApiClient;

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
         * Use the {@link RegisterPassbookFragment#newInstance} factory method to
         * create an instance of this fragment.
         */
        public class RegisterPassbookFragment extends Fragment {
            private static PassbookRegisteredListener listener;
            private ImageView passbookImageView;
            private TextView passbookNameTextView, descriptionTextView, interestRateTextView, periodTextView;
            private Button btnRegister, btnClose;
            private Passbook passbook;
            protected User user;
            protected Wallet wallet;
            public RegisterPassbookFragment() {
            }

            public static RegisterPassbookFragment newInstance(Passbook passbook, User user, Wallet wallet, PassbookRegisteredListener passbookRegisteredListener) {
                listener = passbookRegisteredListener;
                RegisterPassbookFragment fragment = new RegisterPassbookFragment();
                Bundle args = new Bundle();
                args.putSerializable("passbook", passbook);
                args.putSerializable("user", user);
                args.putSerializable("wallet", wallet);
                fragment.setArguments(args);
                return fragment;
            }
            public void setPassbook() {
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                if (getArguments() != null) {
                    passbook = (Passbook) getArguments().getSerializable("passbook");
                    user = (User) getArguments().getSerializable("user");
                    wallet = (Wallet) getArguments().getSerializable("wallet");
                }
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_register_passbook, container, false);
                initView(view);
                populatePassbookData();
                addEvent();
                return view;
            }

            private void initView(View view) {
                passbookImageView = view.findViewById(R.id.passbookImageView);
                passbookNameTextView = view.findViewById(R.id.passbookNameTextView);
                descriptionTextView = view.findViewById(R.id.descriptionTextView);
                interestRateTextView = view.findViewById(R.id.interestRateTextView);
                periodTextView = view.findViewById(R.id.periodTextView);
                btnRegister = view.findViewById(R.id.btnRegister);
                btnClose = view.findViewById(R.id.btnClose);
            }
            protected void addEvent(){
                // Xử lý sự kiện khi nút đóng fragment được click
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeFragment();
                    }
                });
                // Xử lý sự kiện khi nút đăng ký được click
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogConfirmation();
                    }
                });
            }
            private void closeFragment(){
                if (getActivity() != null && getActivity() instanceof InvestActivity) {
                    ((InvestActivity) getActivity()).onFragmentClosed();
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
            // Phương thức để hiển thị dialog xác nhận đăng ký
            private void showDialogConfirmation() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to register this passbook?");
                // Thêm EditText vào dialog
                final EditText editTextAmount = new EditText(getContext());
                editTextAmount.setHint("Enter amount deposit");
                editTextAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(editTextAmount);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amount_deposit = editTextAmount.getText().toString();
                        registerPassbook(amount_deposit);
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            // Phương thức để gọi API RegisterPassbook
            private void registerPassbook(String amount_deposit) {
                // Lấy authToken từ SharedPreferences hoặc từ nơi lưu trữ khác
                String authToken = "Bearer " + user.getAccesssToken();
                // Tạo JSONObject chứa dữ liệu cần gửi đi
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("amount_deposit", amount_deposit);
                    requestBody.put("passbookId", passbook.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Gọi API RegisterPassbook
                Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().RegisterPassbook(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Register passbook successfully!", Toast.LENGTH_LONG).show();
                            wallet.setAccount_balance(wallet.getAccount_balance()-Float.parseFloat(amount_deposit));
                            listener.passbookRegistered(wallet);
                            closeFragment();
                        }
                        else {
                            String errorMessage = "";
                            try {
                                JSONObject errorBody = new JSONObject(response.errorBody().string());
                                errorMessage = errorBody.getString("message");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Register passbook FAILED!", Toast.LENGTH_LONG).show();
                            Log.e("API_CALL", "Unsuccessful response: " + response.code());
                            Log.e("API_CALL", "Unsuccessful response: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Register passbook", "Failed when register passbook!: " + t.getMessage());                }
                });
            }
            private void populatePassbookData() {
                if (passbook != null) {
                    if (passbook.getPassbook_name().equals("Tài lộc đầy nhà"))
                        passbookImageView.setImageResource(R.drawable.tailocdaynha);
                    else if (passbook.getPassbook_name().equals("Heo đỏ tài lộc"))
                        passbookImageView.setImageResource(R.drawable.heovangtailoc);
                    else if (passbook.getPassbook_name().equals("Heo vàng tài lộc"))
                        passbookImageView.setImageResource(R.drawable.heovangtailoc);
                    else
                        passbookImageView.setImageResource(R.drawable.cayvangtailoc);
                    passbookNameTextView.setText(passbook.getPassbook_name());
                    descriptionTextView.setText(passbook.getDescription());
                    interestRateTextView.setText(String.valueOf(passbook.getInterest_rate()));
                    periodTextView.setText(String.valueOf(passbook.getPeriod()));
                }
            }
        }