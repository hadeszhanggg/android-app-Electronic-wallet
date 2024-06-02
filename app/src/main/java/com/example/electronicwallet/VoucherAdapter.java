package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.models.Bill;
import com.example.electronicwallet.models.Voucher;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VoucherAdapter extends ArrayAdapter<Voucher> {
    private Context context;
    private int resource;
    private List<Voucher> vouchers;

    public VoucherAdapter(Context context, int resource, List<Voucher> vouchers) {
        super(context, resource, vouchers);
        this.context = context;
        this.resource = resource;
        this.vouchers = vouchers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Voucher currentVoucher = vouchers.get(position);
        //Gan cac view
        ImageView imageVoucher = listItemView.findViewById(R.id.imageVoucher);
        TextView textDescription = listItemView.findViewById(R.id.textDescription);
        TextView textDiscount = listItemView.findViewById(R.id.textDiscount);
        TextView textType = listItemView.findViewById(R.id.textType);
        TextView txtExp=listItemView.findViewById(R.id.txtExp);
        if ("electric".equals(currentVoucher.getType())) {
            imageVoucher.setImageResource(R.drawable.voucher_electric);
        } else if ("water".equals(currentVoucher.getType())) {
            imageVoucher.setImageResource(R.drawable.voucher_water);
        } else if ("telecom".equals(currentVoucher.getType())) {
            imageVoucher.setImageResource(R.drawable.voucher_telecom);
        }else
                imageVoucher.setImageResource(R.drawable.voucher_wifi);
        textDescription.setText(currentVoucher.getDescription());
        textDiscount.setText(String.format("%.2f%%",currentVoucher.getDiscount()));
        textType.setText(String.valueOf(currentVoucher.getType()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)", Locale.getDefault());
        String formattedDate = dateFormat.format(currentVoucher.getExp());
        txtExp.setText("Exp: " + formattedDate);
        return listItemView;
    }
}

