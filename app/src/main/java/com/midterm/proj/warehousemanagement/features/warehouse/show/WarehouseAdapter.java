package com.midterm.proj.warehousemanagement.features.warehouse.show;

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
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;

public class WarehouseAdapter extends ArrayAdapter<Warehouse> {
    private Context mContext;
    private ArrayList<Warehouse> warehouseList = new ArrayList<>();

    public WarehouseAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Warehouse> list) {
        super(context, 0 , list);
        mContext = context;
        warehouseList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.warehouse_list_item, parent, false);
        Warehouse warehouse = warehouseList.get(position);
        TextView warehouseName = listItem.findViewById(R.id.tv_warehouse_name);
        TextView warehouseAddress = listItem.findViewById(R.id.tv_warehouse_address);
        TextView warehouseID = listItem.findViewById(R.id.tv_warehouse_id);
        warehouseName.setText(warehouse.getName());
        warehouseAddress.setText(warehouse.getAddress());
        warehouseID.setText( String.valueOf(warehouse.getID_Warehouse()));
        return listItem;
    }
}
