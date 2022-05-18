package com.midterm.proj.warehousemanagement.features.employee.update;

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
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.employee.EmployeeCrudListener;
import com.midterm.proj.warehousemanagement.features.employee.create.CreateEmployeeDialogFragment;
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class UpdateEmployeeDialogFragment extends DialogFragment {
    private static EmployeeCrudListener employeeCrudListener;
    private EditText edtEmployeeName;
    private EditText edtEmployeePhone;
    private Button btnSubmitNewEmployee;
    private Button btnCancelNewEmployee;
    Employee employee = new Employee();
    private String strEmployeeName, strWarehousePhone;
    private static int employeeID;

    public UpdateEmployeeDialogFragment(){}

    public static UpdateEmployeeDialogFragment newInstance(String title, int pos, EmployeeCrudListener listener){
        employeeCrudListener = listener;
        employeeID = pos;
        UpdateEmployeeDialogFragment updateEmployeeDialogFragment = new UpdateEmployeeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        updateEmployeeDialogFragment.setArguments(args);
        updateEmployeeDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        return updateEmployeeDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_employee,container,false);
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
        setEmployeeCurrentInfo();
    }

    private void setEmployeeCurrentInfo() {
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readEmployee(employeeID, new QueryResponse<Employee>() {
            @Override
            public void onSuccess(Employee data) {
                edtEmployeeName.setHint(data.getName());
                edtEmployeePhone.setHint(data.getPhone());
                employee.setID_Employee(data.getID_Employee());
                employee.setName(data.getName());
                employee.setPhone(data.getPhone());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setEvent() {
        btnSubmitNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmployeeName = edtEmployeeName.getText().toString().trim();
                strWarehousePhone = edtEmployeePhone.getText().toString().trim();
                updateEmployee(strEmployeeName,strWarehousePhone);
                getDialog().dismiss();
            }
        });

        btnCancelNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    void updateEmployee(String name, String phone){
        if(name.length()!=0){
            employee.setName(name);
        }
        if(phone.length()!=0){
            employee.setPhone(phone);
        }
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.updateEmployee(employee, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                employeeCrudListener.onEmployeeListUpdate(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
