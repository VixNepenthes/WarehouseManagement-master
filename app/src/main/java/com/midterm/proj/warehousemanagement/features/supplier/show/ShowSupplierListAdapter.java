package com.midterm.proj.warehousemanagement.features.supplier.show;

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
import com.midterm.proj.warehousemanagement.model.Supplier;

import java.util.ArrayList;

public class ShowSupplierListAdapter extends ArrayAdapter<Supplier> {
    private Context mContext;
    private ArrayList<Supplier> supplierArrayList = new ArrayList<>();

    public ShowSupplierListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Supplier> list){
        super(context, 0 , list);
        mContext = context;
        supplierArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.supplier_list_item, parent, false);
        }
        Supplier supplier = supplierArrayList.get(position);
        TextView name = listItem.findViewById(R.id.tv_supplier_name);
        TextView address = listItem.findViewById(R.id.tv_supplier_address);
        name.setText(supplier.getName());
        address.setText(supplier.getAddress());
        return listItem;
    }
}
