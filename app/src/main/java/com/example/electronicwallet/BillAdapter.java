package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.models.Bill;

import java.util.List;

public class BillAdapter extends ArrayAdapter<Bill> {
    private Context context;
    private int resource;
    private List<Bill> bills;

    public BillAdapter(Context context, int resource, List<Bill> bills) {
        super(context, resource, bills);
        this.context = context;
        this.resource = resource;
        this.bills = bills;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Bill currentBill = bills.get(position);

        // Lấy các View trong item_bill_layout
        ImageView imageBill = listItemView.findViewById(R.id.imageBill);
        TextView textDescription = listItemView.findViewById(R.id.textDescription);
        TextView textTotal = listItemView.findViewById(R.id.textTotal);
        TextView textType = listItemView.findViewById(R.id.textType);
        if ("electric".equals(currentBill.getType())) {
            imageBill.setImageResource(R.drawable.electric_icon);
        } else if ("water".equals(currentBill.getType())) {
            imageBill.setImageResource(R.drawable.water_icon);
        } else {
            imageBill.setImageResource(R.drawable.wifi_router_icon);
        }
        textDescription.setText(currentBill.getDescription());
        textTotal.setText(String.valueOf(currentBill.getTotal()));
        textType.setText(String.valueOf(currentBill.getType()));
        return listItemView;
    }
}
