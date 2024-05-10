package com.example.electronicwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.electronicwallet.models.Passbook;

import java.util.List;

public class PassbookAdapter extends ArrayAdapter<Passbook> {
    private Context context;
    private int resource;
    private List<Passbook> passbooks;

    public PassbookAdapter(Context context, int resource, List<Passbook> passbooks) {
        super(context, resource, passbooks);
        this.context = context;
        this.resource = resource;
        this.passbooks = passbooks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Passbook currentPassbook = passbooks.get(position);

        TextView passbookNameTextView = listItemView.findViewById(R.id.passbookNameTextView);
        TextView interestRateTextView = listItemView.findViewById(R.id.interestRateTextView);
        TextView periodTextView = listItemView.findViewById(R.id.periodTextView);
        ImageView passbookImageView = listItemView.findViewById(R.id.passbookImageView);

        passbookNameTextView.setText(currentPassbook.getPassbook_name());
        // Chuyển đổi từ số thập phân sang phần trăm
        double interestRatePercent = currentPassbook.getInterest_rate() * 100;
        // Sử dụng định dạng phần trăm để hiển thị
        interestRateTextView.setText(String.format("%.2f%%", interestRatePercent));
        periodTextView.setText(String.valueOf(currentPassbook.getPeriod()+" Tháng"));
        String passbookName = currentPassbook.getPassbook_name();
        if (passbookName != null) {
            if (passbookName.equals("Tài lộc đầy nhà"))
                passbookImageView.setImageResource(R.drawable.tailocdaynha);
            else if (passbookName.equals("Heo đỏ tài lộc"))
                passbookImageView.setImageResource(R.drawable.heodotailoc);
            else if (passbookName.equals("Heo vàng tài lộc"))
                passbookImageView.setImageResource(R.drawable.heovangtailoc);
            else
                passbookImageView.setImageResource(R.drawable.cayvangtailoc);
        }

        return listItemView;
    }
}
