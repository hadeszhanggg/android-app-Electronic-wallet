package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.models.Passbook;
import com.example.electronicwallet.models.Transaction;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    private int resource;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, int resource, List<Transaction> transactions) {
        super(context, resource, transactions);
        this.context = context;
        this.resource = resource;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

       Transaction currentTransaction = transactions.get(position);

        TextView txtContent = listItemView.findViewById(R.id.txtContent);
        TextView txtAmount = listItemView.findViewById(R.id.txtAmount);
        TextView txtDate = listItemView.findViewById(R.id.txtDate);
        ImageView imageTran = listItemView.findViewById(R.id.imageTran);

        txtContent.setText(currentTransaction.getContent());
        txtAmount.setText(String.valueOf(currentTransaction.getAmount()));
        txtDate.setText(currentTransaction.getDate());
        int type = currentTransaction.getType();
            if (type==1)
                imageTran.setImageResource(R.drawable.deposit);
            else if (type==2)
                imageTran.setImageResource(R.drawable.transfer_money);
            else if (type==3)
                imageTran.setImageResource(R.drawable.pay);
            else
                imageTran.setImageResource(R.drawable.pay);
        return listItemView;
    }
}
