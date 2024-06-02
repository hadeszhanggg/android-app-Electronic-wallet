package com.example.electronicwallet.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicwallet.FriendRequestAdapter;
import com.example.electronicwallet.Interface.DataShared;
import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.ListVoucherActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.VoucherAdapter;
import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Voucher;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.electronicwallet.network.NodeJsApiService;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {
    private static DataShared listener;
    private ImageView BillImageView,btnVoucher;
    private TextView expiryDaysTextView, descriptionTextView, totalTextView;
    private Button btnPay, btnClose;
    protected Bill bill;
    protected User user;
    protected Wallet wallet;
    private List<Voucher> allVouchers;
    public PayFragment() {

    }
    public static PayFragment newInstance(Bill bill,User user, Wallet wallet, DataShared passbookRegisteredListener) {
        listener = passbookRegisteredListener;
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putSerializable("bill", bill);
        args.putSerializable("user", user);
        args.putSerializable("wallet", wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bill = (Bill) getArguments().getSerializable("bill");
            user = (User) getArguments().getSerializable("user");
            wallet = (Wallet) getArguments().getSerializable("wallet");
        }
    }
    private void initView(View view) {
        BillImageView = view.findViewById(R.id.billImageView);
        expiryDaysTextView = view.findViewById(R.id.expiryDaysTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        totalTextView = view.findViewById(R.id.totalTextView);
        btnPay = view.findViewById(R.id.btnPay);
        btnClose = view.findViewById(R.id.btnClose);
        btnVoucher=view.findViewById(R.id.btnVoucher);
    }
    protected void addEvent(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmation();
            }
        });
        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchVouchersByType(bill.getType());
            }
        });
    }
    private void fetchVouchersByType(String type) {
        String authToken = "Bearer " + user.getAccesssToken();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<List<Voucher>> call = NodeJsApiClient.getNodeJsApiService().getAllVouchersByType(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);

        call.enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allVouchers = response.body();
                    showVoucherDialog();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch vouchers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showVoucherDialog() {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_use_voucher);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        RecyclerView listViewRequests = dialog.findViewById(R.id.listViewRequests);
        listViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        UseVoucherAdapter adapter = new UseVoucherAdapter(getContext(), allVouchers, new UseVoucherAdapter.OnItemClickListener() {
            @Override
            public void onConfirmClick(Voucher voucher) {
                // Handle confirm voucher logic here
            }
        });

        listViewRequests.setAdapter(adapter);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void closeFragment(){
        if (getActivity() != null && getActivity() instanceof ListBillActivity) {
            ((ListBillActivity) getActivity()).onFragmentClosed();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
    private void showDialogConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to pay this bill?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payTheBill();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void payTheBill() {
        String authToken = "Bearer " + user.getAccesssToken();
        // Tạo JSONObject chứa dữ liệu cần gửi đi
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("billId", bill.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Gọi API RegisterPassbook
        Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().payBill(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Pay this bill successfully!", Toast.LENGTH_LONG).show();
                    wallet.setAccount_balance(wallet.getAccount_balance()-bill.getTotal());
                    listener.dataShared(wallet);
                    Log.d("API_CALL","Acount balance: "+ wallet.getAccount_balance().toString() );
                    ((ListBillActivity) getActivity()).removeBillFromList(bill);
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
                    Toast.makeText(getContext(), "Pay this bill FAILED!", Toast.LENGTH_LONG).show();
                    Log.e("API_CALL", "Unsuccessful response: " + response.code());
                    Log.e("API_CALL", "Unsuccessful response: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Register passbook", "Failed when pay this bill!: " + t.getMessage());                }
        });
    }
    private void populateBillData() {
        if (bill != null) {
            if (bill.getType().equals("electric"))
                BillImageView.setImageResource(R.drawable.electronic_bill);
            else if (bill.getType().equals("water"))
                BillImageView.setImageResource(R.drawable.water_bill);
            else BillImageView.setImageResource(R.drawable.telecom_bill);
            expiryDaysTextView.setText(bill.getExpiryDay().toString());
            descriptionTextView.setText(bill.getDescription());
            totalTextView.setText(String.valueOf(bill.getTotal()));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        initView(view);
        populateBillData();
        addEvent();
        return view;
    }
}