package com.midterm.proj.warehousemanagement.features.supplier.update;

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
import com.midterm.proj.warehousemanagement.database.daoImplementation.SupplierQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.supplier.SupplierCrudListener;
import com.midterm.proj.warehousemanagement.features.warehouse.WarehouseCrudListener;
import com.midterm.proj.warehousemanagement.features.warehouse.update.UpdateWarehouseDialogFragment;
import com.midterm.proj.warehousemanagement.model.Supplier;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.MyApp;

public class UpdateSupplierDialogFragment extends DialogFragment{
    private static SupplierCrudListener supplierCrudListener;

    private EditText edtSupplierName;
    private EditText edtSupplierAddress;
    private Button btnSubmit;
    private Button btnCancel;

    private String strSupplierName, strSupplierAddress;
    private Supplier supplier = new Supplier();
    private static int supplierID;

    public UpdateSupplierDialogFragment(){}

    public static UpdateSupplierDialogFragment newInstance(String title, int pos, SupplierCrudListener listener){
        supplierCrudListener = listener;
        UpdateSupplierDialogFragment updateSupplierDialogFragment = new UpdateSupplierDialogFragment();
        Bundle args = new Bundle();
        supplierID=pos;
        args.putString("title", title);
        updateSupplierDialogFragment.setArguments(args);
        updateSupplierDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return updateSupplierDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_supplier,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        edtSupplierName = view.findViewById(R.id.edt_supplier_name);
        edtSupplierAddress = view.findViewById(R.id.edt_supplier_address);
        btnSubmit= view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        setCurrentSupplierInfo();
    }

    private void setCurrentSupplierInfo() {
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.readSupplier(supplierID, new QueryResponse<Supplier>() {
            @Override
            public void onSuccess(Supplier data) {
                edtSupplierName.setHint(data.getName());
                edtSupplierAddress.setHint(data.getAddress());
                supplier.setName(data.getName());
                supplier.setAddress(data.getAddress());
                supplier.setID_Supplier(data.getID_Supplier());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSupplierName = edtSupplierName.getText().toString().trim();
                strSupplierAddress = edtSupplierAddress.getText().toString().trim();
                updateSupplier(strSupplierName,strSupplierAddress);
                getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void updateSupplier(String name, String address){
        if(name.length()!=0){
            supplier.setName(name);
        }
        if(address.length()!=0){
            supplier.setAddress(address);
        }
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.updateSupplier(supplier, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                supplierCrudListener.onSupplierListUpdate(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
