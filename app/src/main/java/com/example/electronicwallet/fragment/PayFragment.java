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

import com.example.electronicwallet.InvestActivity;
import com.example.electronicwallet.ListBillActivity;
import com.example.electronicwallet.R;
import com.example.electronicwallet.models.Bill;
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
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {
    private ImageView BillImageView;
    private TextView expiryDaysTextView, descriptionTextView, totalTextView;
    private Button btnPay, btnClose;
    protected Bill bill;
    protected User user;
    protected Wallet wallet;
    public PayFragment() {

    }
    public static PayFragment newInstance(Bill bill,User user, Wallet wallet) {
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
                    wallet.setAccount_balance( wallet.getAccount_balance()-bill.getTotal());
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