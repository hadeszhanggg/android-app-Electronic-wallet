    package com.example.electronicwallet;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.example.electronicwallet.models.Bill;

    import java.text.DecimalFormat;
    import java.text.SimpleDateFormat;
    import java.util.List;
    import java.util.Locale;

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
            TextView exp = listItemView.findViewById(R.id.txtExp);
            if ("electric".equals(currentBill.getType())) {
                imageBill.setImageResource(R.drawable.electronic_bill);
            } else if ("water".equals(currentBill.getType())) {
                imageBill.setImageResource(R.drawable.water_bill);
            } else if ("telecom".equals(currentBill.getType())) {
                imageBill.setImageResource(R.drawable.telecom_bill);
            }else imageBill.setImageResource(R.drawable.bill_wifi);
            textDescription.setText(currentBill.getDescription());
            DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
            textTotal.setText(String.valueOf(decimalFormat.format(currentBill.getTotal())+" VNĐ"));
            textType.setText(String.valueOf(currentBill.getType()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)", Locale.getDefault());
            String formattedDate = dateFormat.format(currentBill.getExpiryDay());
            exp.setText("Exp: " + formattedDate);
            return listItemView;
        }
    }
