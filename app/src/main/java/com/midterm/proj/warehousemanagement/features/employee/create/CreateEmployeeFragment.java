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
import androidx.fragment.app.Fragment;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.features.employee.EmployeeCrudListener;
import com.midterm.proj.warehousemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeFragment extends Fragment {
    private EditText edtEmployeeName, edtEmployeePhone;
    private Button btnCreateEmployee;
    private static EmployeeCrudListener employeeCrudListener;
    Bundle args;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        args = this.getArguments();
        return inflater.inflate(R.layout.fragment_create_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setControl() {
        edtEmployeeName = getView().findViewById(R.id.edt_create_employee_name);
        edtEmployeePhone = getView().findViewById(R.id.edt_create_employee_phone);
        btnCreateEmployee = getView().findViewById(R.id.btn_submit_new_employee);
    }

    private void setEvent() {
        btnCreateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtEmployeeName.getText().toString().trim();
                String phone = edtEmployeePhone.getText().toString().trim();

                createEmployee(name,phone);

                onDetach();
            }
        });
    }

    void createEmployee(String name, String phone){
        if(name.length()==0){
            Toast.makeText(getActivity(), "Tên nhân viên không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.length()==0){
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
        for(Employee w : e){
            if (w.getName().equals(name) && w.getPhone().equals(phone)){
                Toast.makeText(getActivity(), "Thông tin nhân viên đã tồn tại", Toast.LENGTH_LONG).show();
                return;
        }
    }
        Employee employee = new Employee(name,phone);
        employeeQuery.createEmployee(employee, new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if(args!=null){
                    int number_of_warehouse = args.getInt("number_of_employee");
                    if(number_of_warehouse != 0){
                        // no warehouse created
                        employeeCrudListener.onEmployeeListUpdate(data);
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
