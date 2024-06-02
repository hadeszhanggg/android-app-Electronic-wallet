package com.example.electronicwallet.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import com.example.electronicwallet.Interface.DataShared;
import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Voucher;
import com.example.electronicwallet.models.Wallet;
import com.example.electronicwallet.network.NodeJsApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayFragment extends Fragment {
    private static DataShared listener;
    private ImageView BillImageView, btnVoucher;
    private TextView expiryDaysTextView, descriptionTextView, totalTextView;
    private Button btnPay, btnClose;
    protected Bill bill;
    protected User user;
    protected Wallet wallet;
    private List<Voucher> allVouchers;
    private Dialog voucherDialog;
    private Voucher selectedVoucher;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance(Bill bill, User user, Wallet wallet, DataShared dataShared) {
        listener = dataShared;
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
        btnVoucher = view.findViewById(R.id.btnVoucher);
    }

    protected void addEvent() {
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
        voucherDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        voucherDialog.setContentView(R.layout.dialog_use_voucher);
        voucherDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        voucherDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnClose = voucherDialog.findViewById(R.id.btnClose);
        RecyclerView listViewRequests = voucherDialog.findViewById(R.id.listViewRequests);
        listViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        UseVoucherAdapter adapter = new UseVoucherAdapter(getContext(), allVouchers, new UseVoucherAdapter.OnItemClickListener() {
            @Override
            public void onConfirmClick(Voucher voucher) {
                applyVoucher(voucher);
                voucherDialog.dismiss();
            }
        });

        listViewRequests.setAdapter(adapter);
        btnClose.setOnClickListener(v -> voucherDialog.dismiss());
        voucherDialog.show();
    }

    private void applyVoucher(Voucher voucher) {
        selectedVoucher = voucher;
        double discountAmount = bill.getTotal() * voucher.getDiscount();
        String totalText = decimalFormat.format(bill.getTotal());
        totalTextView.setText(totalText + " (-" + decimalFormat.format(discountAmount) + "đ)");
    }

    private void closeFragment() {
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
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("billId", bill.getId());
            if (selectedVoucher != null) {
                requestBody.put("voucherId", selectedVoucher.getId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = NodeJsApiClient.getNodeJsApiService().payBill(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    double discountAmount = 0;
                    if (selectedVoucher != null) {
                        discountAmount = bill.getTotal() * selectedVoucher.getDiscount();
                    }
                    wallet.setAccount_balance(wallet.getAccount_balance() - (bill.getTotal() - discountAmount));
                    Toast.makeText(getContext(), "Pay this bill successfully!", Toast.LENGTH_LONG).show();
                    listener.dataShared(wallet);
                    ((ListBillActivity) getActivity()).removeBillFromList(bill);
                    closeFragment();
                } else {
                    String errorMessage = "";
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        errorMessage = errorBody.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
            totalTextView.setText(decimalFormat.format(bill.getTotal())+ " - VNĐ");
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
