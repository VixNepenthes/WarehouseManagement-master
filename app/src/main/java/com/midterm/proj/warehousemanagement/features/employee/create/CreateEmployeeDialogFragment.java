package com.midterm.proj.warehousemanagement.features.employee.create;

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
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.features.employee.EmployeeCrudListener;
import com.midterm.proj.warehousemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeDialogFragment extends DialogFragment {
    private static EmployeeCrudListener employeeCrudListener;

    private EditText edtEmployeeName;
    private EditText edtEmployeePhone;
    private Button btnSubmitNewEmployee;
    private Button btnCancelNewEmployee;

    private String strEmployeeName, strWarehousePhone;

    public CreateEmployeeDialogFragment(){}

    public static CreateEmployeeDialogFragment newInstance(String title, EmployeeCrudListener listener){
        employeeCrudListener = listener;
        CreateEmployeeDialogFragment createEmployeeDialogFragment = new CreateEmployeeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        createEmployeeDialogFragment.setArguments(args);
        createEmployeeDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return createEmployeeDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_employee,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        edtEmployeeName = view.findViewById(R.id.edt_create_employee_name);
        edtEmployeePhone = view.findViewById(R.id.edt_create_employee_phone);
        btnSubmitNewEmployee = view.findViewById(R.id.btn_submit_new_employee);
        btnCancelNewEmployee = view.findViewById(R.id.btn_cancel_new_employee);
        String title = getArguments().getString("title");
        getDialog().setTitle(title);

    }

    private void setEvent() {
        btnSubmitNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmployeeName = edtEmployeeName.getText().toString().trim();
                strWarehousePhone = edtEmployeePhone.getText().toString().trim();
                createNewEmployee(strEmployeeName,strWarehousePhone);
            }
        });

        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.anyEmployeeCreated(new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                btnCancelNewEmployee.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(String message) {
                btnCancelNewEmployee.setVisibility(View.VISIBLE);
            }
        });

        btnCancelNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void createNewEmployee(String name, String phone){
        if(name.length()==0){
            Toast.makeText(getActivity(), "Tên nhân viên không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.length()==0 ){
            Toast.makeText(getActivity(), "SĐT nhân viên không được trống", Toast.LENGTH_LONG).show();
            return;
        }

        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        ArrayList<Employee> e = new ArrayList<>();
        employeeQuery.readAllEmployee(new QueryResponse<List<Employee>>() {
            @Override
            public void onSuccess(List<Employee> data) {
                e.addAll(data);
            }
            @Override
            public void onFailure(String message) {

            }
        });
        for(Employee w : e) {
            if (w.getName().equals(name) && w.getPhone().equals(phone)) {
                Toast.makeText(getActivity(), "Thông tin nhân viên đã tồn tại", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Employee employee = new Employee(name,phone);
        employeeQuery.createEmployee(employee, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                //if(args!=null){
                //   int number_of_warehouse = args.getInt("number_of_employee");
                //    if(number_of_warehouse != 0){
                        // no warehouse created
                        employeeCrudListener.onEmployeeListUpdate(data);
                //    }
                //}
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
