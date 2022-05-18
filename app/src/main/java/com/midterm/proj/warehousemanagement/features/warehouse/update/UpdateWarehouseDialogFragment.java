package com.midterm.proj.warehousemanagement.features.warehouse.update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.warehouse.WarehouseCrudListener;
import com.midterm.proj.warehousemanagement.features.warehouse.create.CreateWarehouseDialogFragment;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class UpdateWarehouseDialogFragment extends DialogFragment {
    private static WarehouseCrudListener warehouseCrudListener;

    private EditText edtWarehouseName;
    private EditText edtWarehouseAddress;
    private Button btnSubmitNewWarehouse;
    private Button btnCancelNewWarehouse;

    private String strWarehouseName, strWarehouseAddress;
    private Warehouse warehouse = new Warehouse();
    private static int warehouseID;

    public UpdateWarehouseDialogFragment(){}

    public static UpdateWarehouseDialogFragment newInstance(String title,int pos, WarehouseCrudListener listener){
        warehouseCrudListener = listener;
        UpdateWarehouseDialogFragment updateWarehouseDialogFragment = new UpdateWarehouseDialogFragment();
        Bundle args = new Bundle();
        warehouseID=pos;
        args.putString("title", title);
        updateWarehouseDialogFragment.setArguments(args);
        updateWarehouseDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return updateWarehouseDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_warehouse,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        edtWarehouseName = view.findViewById(R.id.edt_create_warehouse_name);
        edtWarehouseAddress = view.findViewById(R.id.edt_create_warehouse_address);
        btnSubmitNewWarehouse = view.findViewById(R.id.btn_submit_new_warehouse);
        btnCancelNewWarehouse = view.findViewById(R.id.btn_cancel_new_warehouse);
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        setCurrentWarehouseInfo();
    }

    private void setCurrentWarehouseInfo() {
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readWarehouse(warehouseID, new QueryResponse<Warehouse>() {
            @Override
            public void onSuccess(Warehouse data) {
                edtWarehouseName.setHint(data.getName());
                edtWarehouseAddress.setHint(data.getAddress());
                warehouse.setName(data.getName());
                warehouse.setAddress(data.getAddress());
                warehouse.setID_Warehouse(data.getID_Warehouse());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setEvent() {
        btnSubmitNewWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strWarehouseName = edtWarehouseName.getText().toString().trim();
                strWarehouseAddress = edtWarehouseAddress.getText().toString().trim();
                updateWarehouse(strWarehouseName,strWarehouseAddress);
                getDialog().dismiss();
            }
        });


        btnCancelNewWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void updateWarehouse(String name, String address){
        if(name.length()!=0){
            warehouse.setName(name);
        }
        if(address.length()!=0){
            warehouse.setAddress(address);
        }
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.updateWarehouse(warehouse, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                warehouseCrudListener.onWarehouseListUpdate(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
            }
        });


    }
}
