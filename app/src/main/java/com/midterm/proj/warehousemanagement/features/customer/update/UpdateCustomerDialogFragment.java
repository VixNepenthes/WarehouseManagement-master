package com.midterm.proj.warehousemanagement.features.customer.update;

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
import com.midterm.proj.warehousemanagement.database.daoImplementation.CustomerQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.customer.CustomerCrudListener;
import com.midterm.proj.warehousemanagement.features.customer.update.UpdateCustomerDialogFragment;
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.util.MyApp;

public class UpdateCustomerDialogFragment extends DialogFragment {
    private static CustomerCrudListener customerCrudListener;
    private EditText edtCustomerName;
    private EditText edtCustomerPhone;
    private Button btnSubmitNewCustomer;
    private Button btnCancelNewCustomer;
    Customer customer = new Customer();
    private String strCustomerName, strWarehousePhone;
    private static int customerID;

    public UpdateCustomerDialogFragment(){}

    public static UpdateCustomerDialogFragment newInstance(String title, int pos, CustomerCrudListener listener){
        customerCrudListener = listener;
        customerID = pos;
        UpdateCustomerDialogFragment updateCustomerDialogFragment = new UpdateCustomerDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        updateCustomerDialogFragment.setArguments(args);
        updateCustomerDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return updateCustomerDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_customer,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        edtCustomerName = view.findViewById(R.id.edt_create_customer_name);
        edtCustomerPhone = view.findViewById(R.id.edt_create_customer_phone);
        btnSubmitNewCustomer = view.findViewById(R.id.btn_submit_new_customer);
        btnCancelNewCustomer = view.findViewById(R.id.btn_cancel_new_customer);
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        setCustomerCurrentInfo();
    }

    private void setCustomerCurrentInfo() {
        DAO.CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.readCustomer(customerID, new QueryResponse<Customer>() {
            @Override
            public void onSuccess(Customer data) {
                edtCustomerName.setHint(data.getName());
                edtCustomerPhone.setHint(data.getPhone());
                customer.setCustomerID(data.getCustomerID());
                customer.setName(data.getName());
                customer.setPhone(data.getPhone());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setEvent() {
        btnSubmitNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCustomerName = edtCustomerName.getText().toString().trim();
                strWarehousePhone = edtCustomerPhone.getText().toString().trim();
                updateCustomer(strCustomerName,strWarehousePhone);
                getDialog().dismiss();
            }
        });

        btnCancelNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void updateCustomer(String name, String phone){
        if(name.length()!=0){
            customer.setName(name);
        }
        if(phone.length()!=0){
            customer.setPhone(phone);
        }
        else if(phone.length() != 10){
            Toast.makeText(MyApp.context, "Kiểm tra lại SĐT khách hàng.", Toast.LENGTH_LONG).show();
            return;
        }
        DAO.CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.updateCustomer(customer, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                customerCrudListener.onCustomerListUpdate(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
