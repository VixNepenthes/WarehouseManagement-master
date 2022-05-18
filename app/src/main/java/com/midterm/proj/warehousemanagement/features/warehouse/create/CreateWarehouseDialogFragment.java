package com.midterm.proj.warehousemanagement.features.warehouse.create;

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
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.features.warehouse.WarehouseCrudListener;
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class CreateWarehouseDialogFragment extends DialogFragment {
    private static WarehouseCrudListener warehouseCrudListener;

    private EditText edtWarehouseName;
    private EditText edtWarehouseAddress;
    private Button btnSubmitNewWarehouse;
    private Button btnCancelNewWarehouse;

    private String strWarehouseName, strWarehouseAddress;

    public CreateWarehouseDialogFragment(){}

    public static CreateWarehouseDialogFragment newInstance(String title, WarehouseCrudListener listener){
        warehouseCrudListener = listener;
        CreateWarehouseDialogFragment createWarehouseDialogFragment = new CreateWarehouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        createWarehouseDialogFragment.setArguments(args);
        createWarehouseDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return createWarehouseDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_warehouse,container,false);
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

    }

    private void setEvent() {
        btnSubmitNewWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strWarehouseName = edtWarehouseName.getText().toString().trim();
                strWarehouseAddress = edtWarehouseAddress.getText().toString().trim();
                createWarehouse(strWarehouseName,strWarehouseAddress);
            }
        });

        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.anyWarehouseCreated(new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                btnCancelNewWarehouse.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(String message) {
                btnCancelNewWarehouse.setVisibility(View.VISIBLE);
            }
        });

        btnCancelNewWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void createWarehouse(String name, String address){
        if(name.length()==0){
            Toast.makeText(getActivity(), "Tên kho không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if(address.length()==0){
            Toast.makeText(getActivity(), "Địa chỉ kho không được trống", Toast.LENGTH_LONG).show();
            return;
        }

        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        ArrayList<Warehouse> e = new ArrayList<>();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                e.addAll(data);
            }
            @Override
            public void onFailure(String message) {

            }
        });
        for(Warehouse w : e) {
            if (w.getName().equals(name) && w.getAddress().equals(address)) {
                Toast.makeText(getActivity(), "Thông tin kho đã tồn tại", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Warehouse warehouse = new Warehouse(name,address);
        warehouseQuery.createWarehouse(warehouse, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                getDialog().dismiss();
                warehouseCrudListener.onWarehouseListUpdate(data);
            }

            @Override
            public void onFailure(String message) {
                warehouseCrudListener.onWarehouseListUpdate(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
