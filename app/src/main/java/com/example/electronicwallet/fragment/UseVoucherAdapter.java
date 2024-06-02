package com.example.electronicwallet.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicwallet.R;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Voucher;

import java.text.DecimalFormat;
import java.util.List;

public class UseVoucherAdapter extends RecyclerView.Adapter<UseVoucherAdapter.ViewHolder> {
    private Context context;
    private List<Voucher> voucherList;
    private OnItemClickListener listener;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    public interface OnItemClickListener {
        void onConfirmClick(Voucher voucher);
    }

    public UseVoucherAdapter(Context context, List<Voucher> voucherList, OnItemClickListener listener) {
        this.context = context;
        this.voucherList = voucherList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_use_voucher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        if(voucher.getType().equals("water"))
            holder.imgVoucher.setImageResource(R.drawable.voucher_water);
        else if(voucher.getType().equals("telecom"))
            holder.imgVoucher.setImageResource(R.drawable.voucher_telecom);
        else holder.imgVoucher.setImageResource(R.drawable.voucher_electric);
        holder.txtName.setText(voucher.getVoucherName());
        holder.txtDiscount.setText(String.format("%.2f%%",voucher.getDiscount()));
        holder.btnConfirm.setOnClickListener(v -> {
            listener.onConfirmClick(voucher);
            voucherList.remove(voucher);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtDiscount;
        ImageButton btnConfirm, btnCancel;
        ImageView imgVoucher;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVoucher=itemView.findViewById(R.id.imgVoucher);
            txtName = itemView.findViewById(R.id.txtName);
            txtDiscount = itemView.findViewById(R.id.txtDiscount);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
