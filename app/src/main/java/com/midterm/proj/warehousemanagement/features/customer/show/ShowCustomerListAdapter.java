package com.midterm.proj.warehousemanagement.features.customer.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.model.Product;

import java.util.ArrayList;

public class ShowCustomerListAdapter extends ArrayAdapter<Customer> {
    private Context mContext;
    private ArrayList<Customer> customerArrayList = new ArrayList<>();

    public ShowCustomerListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Customer> list){
        super(context, 0 , list);
        mContext = context;
        customerArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.customer_list_item, parent, false);
        }
        Customer customer = customerArrayList.get(position);
        TextView name = listItem.findViewById(R.id.tv_customer_name);
        TextView phone = listItem.findViewById(R.id.tv_customer_phone);
        name.setText(customer.getName());
        phone.setText(customer.getPhone());
        return listItem;
    }
}
